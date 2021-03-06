package org.tiger.command.machine_learning;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.Standard;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskResourceUse;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.util.CollectionUtil;
import org.tiger.persistence.DataPersistence;
import org.tiger.persistence.database.dao.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.tiger.common.Constant.COUNT_SQL;
import static org.tiger.common.Constant.THREAD_COUNT;

/**
 * 参数计算（TODO 这块的计算相当复杂，得设计好算法，先来个最简单的）
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
@SingletonBean
public class Calculator {

    private Logger logger = LoggerFactory.getLogger(Calculator.class);

    @InjectCustomBean
    private DataPersistence<TigerTaskResourceUse> systemMonitorFileDataPersistence;

    @InjectCustomBean
    private DataPersistence<Standard> standardDataPersistence;

    @Inject
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据CPU使用计算分片参数
     *
     * @return 分片参数（fromId,toId,线程数）
     */
    public ShardingParameter getShardingParameter(TigerTask tigerTask) {
        Standard searchCondition = new Standard();
        searchCondition.setTaskId(tigerTask.getId());
        Standard standard = standardDataPersistence.findOne(searchCondition);
        ShardingParameter shardingParameter = new ShardingParameter();
        shardingParameter.setThreadCount(calculateThreadSize(tigerTask, standard));
        List<Long> fromToId = getFromIdAndToId(tigerTask);
        shardingParameter.setFromId(fromToId.get(0));
        shardingParameter.setToId(fromToId.get(1));
        return shardingParameter;
    }

    /**
     * 计算线程数
     *
     * @param tigerTask 任务
     * @param standard  标准
     * @return int
     */
    @SuppressWarnings("unchecked")
    private int calculateThreadSize(TigerTask tigerTask, Standard standard) {
        logger.info("开始计算线程数:{}, 标准:{}", tigerTask.getTaskName(), JSON.toJSONString(standard));
        int threadTotal = 1;
        TigerTaskResourceUse tigerTaskResourceUse = new TigerTaskResourceUse();
        tigerTaskResourceUse.searchParam().put("taskId", tigerTask.getId());
        List<TigerTaskResourceUse> list = systemMonitorFileDataPersistence.findList(tigerTaskResourceUse);
        if (CollectionUtil.isNotEmpty(list)) {
            BigDecimal avg;
            BigDecimal total = BigDecimal.ZERO;
            for (TigerTaskResourceUse taskResourceUse : list) {
                total = total.add(taskResourceUse.getCpuUse());
            }
            avg = total.divide(new BigDecimal(list.size()), BigDecimal.ROUND_HALF_UP, 2);
            logger.info("任务:{}的平均CPU使用率为:{}", tigerTask.getTaskName(), avg);
            //上次执行的线程数
            Map<String, Object> param = JSON.parseObject(tigerTask.getTaskParameter(), Map.class);
            int threadCount = param.get(THREAD_COUNT) == null ? 1 : (Integer) param.get(THREAD_COUNT);
            if (standard.getCpuUse().compareTo(avg) == 0) {
                threadTotal = threadCount;
            } else if (standard.getCpuUse().compareTo(avg) > 0) { //实例使用率小于标准
                //需要增加的线程数 = (标准CPU使用率-平均使用率)/平均使用率 * 上次线程数
                BigDecimal addThread = standard.getCpuUse().subtract(avg)
                        .divide(avg, BigDecimal.ROUND_HALF_UP, 2)
                        .multiply(new BigDecimal(threadCount))
                        .setScale(0, RoundingMode.HALF_UP);
                threadTotal = threadCount + addThread.intValue();
                logger.info("需要增加的线程数:{}", threadTotal);
            } else {
                //需要减少的线程数 = (平均使用率 - 标准CPU使用率) /平均使用率 * 上次线程数
                BigDecimal reduceThread = avg.subtract(standard.getCpuUse())
                        .divide(standard.getCpuUse(), BigDecimal.ROUND_HALF_UP, 2)
                        .multiply(new BigDecimal(threadCount))
                        .setScale(0, RoundingMode.HALF_UP);
                threadTotal = threadCount - reduceThread.intValue();
                logger.info("需要减少的线程数:{}", threadTotal);
            }
        }
        logger.info("计算出的{}线程数为:{}", tigerTask.getTaskName(), threadTotal);
        return threadTotal;
    }

    /**
     * 获取FromId,ToId
     * 根据总数和总IP数来确定自己的那一个fromToId
     *
     * @return List<Long>
     */
    private List<Long> getFromIdAndToId(TigerTask tigerTask) {
        logger.info("开始获取:{}任务的fromId,toId", tigerTask.getTaskName());
        List<Long> fromToId = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(tigerTask.getTaskParameter());
        if (jsonObject != null) {
            String countSql = jsonObject.getString(COUNT_SQL);
            List<String> ipOrder = MemoryShareDataRegion.ipOrder;
            long count;
            if (ProcessMode.readFromDB(tigerTask.getTaskProcessMode())) {
                //从数据库获取总条数
                count = jdbcTemplate.getCount(countSql);
                logger.info("从数据库获取总条数,sql:{}, count:{}", countSql, count);
            } else {
                //TODO 通过文件来获取要处理的总条数，这里先不支持，后面再说
                throw new RuntimeException("当前只支持数据库模式");
            }
            if (count == 0) {
                logger.info("从数据库获取总条数为0");
                return fromToId;
            } else if (ipOrder.size() >= count) {
                //一个IP只需要处理一条记录
                logger.info("一个IP只需要处理一条记录,ipOrder:{},count:{},localIp:{}", JSON.toJSONString(ipOrder), count, MemoryShareDataRegion.localIp);
                long start = 0;
                List<Long> myFromToId = null;
                for (String ip : ipOrder) {
                    if (ip.equals(MemoryShareDataRegion.localIp)) {
                        fromToId.add(start);
                        fromToId.add(start);
                        myFromToId = fromToId;
                        break;
                    }
                    start++;
                }
                if (myFromToId == null) {
                    return fromToId;
                } else {
                    return myFromToId;
                }
            } else {
                logger.info("一个IP处理多条记录,ipOrder:{},count:{},localIp:{}", JSON.toJSONString(ipOrder), count, MemoryShareDataRegion.localIp);
                //一个IP处理多条记录
                long avg = count / ipOrder.size();
                long left = count % ipOrder.size();
                //先均分
                long start = 0;
                if (left > 0) {
                    //求余有剩余则补到能整除，等价于avg +1
                    avg = avg + 1;
                }
                for (String ip : ipOrder) {
                    //都均分了，肯定能找到属于自己IP的
                    if (ip.equals(MemoryShareDataRegion.localIp)) {
                        fromToId.add(start);
                        fromToId.add(start + avg);
                        return fromToId;
                    }
                    start += avg;
                }
            }
        } else {
            fromToId.add(0L);
            fromToId.add(0L);
        }
        return fromToId;
    }
}

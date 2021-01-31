package org.tiger.command.machine_learning;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.Standard;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskResourceUse;
import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.SingletonBean;
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

    private DataPersistence<TigerTaskResourceUse> dataPersistence;

    @InjectByType
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据CPU使用计算分片参数
     *
     * @return 分片参数
     */
    public ShardingParameter getShardingParameter(TigerTask tigerTask, Standard standard) {
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
     * @return
     */
    private int calculateThreadSize(TigerTask tigerTask, Standard standard) {
        TigerTaskResourceUse tigerTaskResourceUse = new TigerTaskResourceUse();
        tigerTaskResourceUse.searchParam().put("taskExecuteId", "");
        List<TigerTaskResourceUse> list = dataPersistence.findList(tigerTaskResourceUse);
        BigDecimal avg;
        BigDecimal total = BigDecimal.ZERO;
        list.forEach((use) -> {
            total.add(use.getCpuUse());
        });
        avg = total.divide(new BigDecimal(list.size()));

        //上次执行的线程数
        Map<String, Object> param = JSON.parseObject(tigerTask.getTaskParameter(), Map.class);
        int threadCount = (Integer) param.get(THREAD_COUNT);
        if (standard.getCpuUse().compareTo(avg) == 0) {
            return threadCount;
        } else if (standard.getCpuUse().compareTo(avg) > 0) { //实例使用率小于标准
            //需要增加的线程数 = (标准CPU使用率-平均使用率)/平均使用率 * 上次线程数
            BigDecimal addThread = standard.getCpuUse().subtract(avg)
                    .divide(avg).multiply(new BigDecimal(threadCount))
                    .setScale(0, RoundingMode.HALF_UP);
            return threadCount + addThread.intValue();
        } else {
            //需要减少的线程数 = (平均使用率 - 标准CPU使用率) /平均使用率 * 上次线程数
            BigDecimal reduceThread = avg.subtract(standard.getCpuUse())
                    .divide(standard.getCpuUse()).multiply(new BigDecimal(threadCount))
                    .setScale(0, RoundingMode.HALF_UP);
            return threadCount - reduceThread.intValue();
        }
    }

    /**
     * 获取FromId,ToId
     * 根据总数和总IP数来确定自己的那一个fromToId
     *
     * @return
     */
    private List<Long> getFromIdAndToId(TigerTask tigerTask) {
        List<Long> fromToId = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(tigerTask.getTaskParameter());
        String countSql = jsonObject.getString(COUNT_SQL);
        List<String> ipOrder = MemoryShareDataRegion.ipOrder;
        long count;
        if (ProcessMode.FILE_TO_API.readFromDB(tigerTask.getTaskProcessMode())) {
            count = jdbcTemplate.getCount(countSql);
        } else {
            //TODO 通过文件来获取要处理的总条数，这里先不支持，后面再说
            throw new RuntimeException("当前只支持数据库模式");
        }
        if (count == 0) {
            return fromToId;
        } else if (ipOrder.size() >= count) {
            //一个IP只需要处理一条记录
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
        return fromToId;
    }
}

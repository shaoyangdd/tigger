package org.tigger.command.machine_learning;

import com.alibaba.fastjson.JSON;
import org.tigger.common.datastruct.Standard;
import org.tigger.common.datastruct.TigerTask;
import org.tigger.common.datastruct.TigerTaskResourceUse;
import org.tigger.persistence.DataPersistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static org.tigger.common.Constant.THREAD_COUNT;

/**
 * 参数计算（TODO 这块的计算相当复杂，得设计好算法，先来个最简单的）
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
public class Calculator {

    private DataPersistence<TigerTaskResourceUse> dataPersistence;

    /**
     * 根据CPU使用计算分片参数
     *
     * @return 分片参数
     */
    public ShardingParameter getShardingParameter(TigerTask tigerTask, Standard standard) {
        ShardingParameter shardingParameter = new ShardingParameter();
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
            shardingParameter.setThreadCount(threadCount);
        } else if (standard.getCpuUse().compareTo(avg) > 0) { //实例使用率小于标准
            //需要增加的线程数 = (标准CPU使用率-平均使用率)/平均使用率 * 上次线程数
            BigDecimal addThread = standard.getCpuUse().subtract(avg)
                    .divide(avg).multiply(new BigDecimal(threadCount))
                    .setScale(0, RoundingMode.HALF_UP);
            shardingParameter.setThreadCount(addThread.intValue());
        } else {
            //需要减少的线程数 = (平均使用率 - 标准CPU使用率) /平均使用率 * 上次线程数
            BigDecimal reduceThread = avg.subtract(standard.getCpuUse())
                    .divide(standard.getCpuUse()).multiply(new BigDecimal(threadCount))
                    .setScale(0, RoundingMode.HALF_UP);
        }
        return shardingParameter;
    }
}

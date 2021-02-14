package org.tiger.command;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TigerTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务拆分策略
 * 1. 不同的作业拆到不同的机器
 * 2. 同一个任务
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public abstract class AbstractTaskSplitStrategy implements TaskSplitStrategy {

    private Logger logger = LoggerFactory.getLogger(AbstractTaskSplitStrategy.class);

    /**
     * 拆分任务流，并行执行的作业拆到不同的机器
     * 几种情况：
     * 1. 任务多机器少
     * 2. 任务少机器多
     * @param tigerTask tiger作业流
     * @return Map<String, TigerTask> ip和作业的映射
     */
    @Override
    public Map<String, List<TigerTask>> splitTaskFlow(List<TigerTask> tigerTask) {
        logger.info("任务拆分开始:{}", JSON.toJSONString(tigerTask));
        //TODO 任务分配算法，发扑克牌算法，IP相当于人，任务相当于扑克牌（这个算法比较low，先这么来，后面研究更牛逼的算法）
        int ipCount = MemoryShareDataRegion.ipOrder.size();
        int taskCount = tigerTask.size();
        Map<String, List<TigerTask>> map = new HashMap<>();
        if (ipCount == taskCount) {
            //机器数等于任务数，这种概率很低
            for (int i = 0; i<ipCount; i++) {
                List<TigerTask> tigerTaskList = new ArrayList<>();
                tigerTaskList.add(tigerTask.get(i));
                map.put(MemoryShareDataRegion.ipOrder.get(i),tigerTaskList);
            }
        } else if (ipCount > taskCount) {
            //这是最常见的情况，机器数大于任务数，一个任务可能会分到多个机器上
            for (int i=0; i<ipCount; i++) {
                List<TigerTask> tigerTaskList = new ArrayList<>();
                tigerTaskList.add(tigerTask.get(i>taskCount-1?i%taskCount:i));
                map.put(MemoryShareDataRegion.ipOrder.get(i),tigerTaskList);
            }
        } else {
            //机器数小于任务数，一般是小规模的系统这么玩，一个机器执行多个任务
            for (int i=0; i<taskCount; i++) {
                int index = i>ipCount-1?i%ipCount:i;
                String key = MemoryShareDataRegion.ipOrder.get(index);
                List<TigerTask> tigerTasks = map.get(key);
                if (tigerTasks == null) {
                    List<TigerTask> tigerTaskList = new ArrayList<>();
                    tigerTaskList.add(tigerTask.get(i));
                    map.put(key,tigerTaskList);
                } else {
                    tigerTasks.add(tigerTask.get(i));
                }
            }
        }
        logger.info("任务拆分结束:{}", JSON.toJSONString(map));
        return map;
    }
}

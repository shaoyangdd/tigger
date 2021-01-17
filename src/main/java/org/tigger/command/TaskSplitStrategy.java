package org.tigger.command;


import org.tigger.db.dao.entity.TigerTask;
import org.tigger.db.dao.entity.TigerTaskFlow;

import java.util.List;
import java.util.Map;

/**
 * 任务拆分策略
 * 1. 不同的作业拆到不同的机器
 * 2. 同一个任务
 * @author kangshaofei
 * @date 2020-01-16
 */
public interface TaskSplitStrategy {

    /**
     * 拆分任务流，并行执行的作业拆到不同的机器
     * @param tigerTask tiger作业流
     * @return Map<String, TigerTask> ip和作业的映射
     */
    Map<String, List<TigerTask>> splitTaskFlow(List<TigerTask> tigerTask);

    /**
     * 拆分任务，作业拆到不同的机器
     * 多个机器同时计算，本机器只计算出属于自己的TigerTask，主要是把分片参数算出来
     * @param tigerTask tiger作业
     * @return Map<String, TigerTask> ip和作业的映射
     */
    Map<String, TigerTask> splitTask(TigerTask tigerTask);

}

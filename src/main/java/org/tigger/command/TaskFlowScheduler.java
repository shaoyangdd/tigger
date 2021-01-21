package org.tigger.command;

import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.LogicTaskNode;
import org.tigger.common.threadpool.ThreadPool;
import org.tigger.database.dao.entity.TigerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * tiger调度器  (最核心的部分)
 *
 * @author kangshaofei
 * @date 2020-01-19
 */
public class TaskFlowScheduler {

    private Logger logger = Logger.getLogger(TaskFlowScheduler.class.getSimpleName());

    /**
     * 遍历并执行任务
     *
     * @param head 任务头节点
     */
    public void iterateAndExecute(LogicTaskNode head) {
        //LOOP 任务遍历
        List<LogicTaskNode> nextNodeList = head.getNextTigerTaskList();
        if (nextNodeList == null || nextNodeList.size() == 0) {
            //没有下一个节点时结束
            return;
        }
        List<TigerTask> tigerTaskList = new ArrayList<>();
        for (LogicTaskNode logicTaskNode : nextNodeList) {
            //TODO 前面有N个，这N个里面有没有执行完的，要等一等，等执行的执行机完了之后，大伙再重新分一下这个任务

            tigerTaskList.add(logicTaskNode.getCurrentTigerTask());
        }
        //1. 任务拆分
        List<TigerTask> myTaskList = splitTask(tigerTaskList);
        if (myTaskList == null || myTaskList.size() == 0) {
            //没有本IP的任务，不用干活
            return;
        }
        //2. 任务执行(本IP上多线程执行）
        for (TigerTask tigerTask : myTaskList) {
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                execute(tigerTask);
            });
        }
        // TODO 等上面的都执行完之后，使用countDownLunch
        //3. 递归并发遍历
        for (LogicTaskNode logicTaskNode : nextNodeList) {
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                iterateAndExecute(logicTaskNode);
            });
        }
    }

    /**
     * 任务拆分，找到属于自己IP的任务集合
     *
     * @param tigerTaskList 所有执行机要执行的任务，可能是一个，也可能是并行的N个
     * @return 属于自己IP的任务集合
     */
    private List<TigerTask> splitTask(List<TigerTask> tigerTaskList) {
        //TODO 任务拆分策略提供默认的，也可以自定义
        TestTaskSplitStrategy testTaskSplitStrategy = new TestTaskSplitStrategy();
        Map<String, List<TigerTask>> map = testTaskSplitStrategy.splitTaskFlow(tigerTaskList);
        return map.get(MemoryShareDataRegion.localIp);
    }

    /**
     * 执行用户自己的业务逻辑
     *
     * @param tigerTask 任务
     */
    private void execute(TigerTask tigerTask) {
        ObjectFactory.instance().getTigerExecutor().executeTask(tigerTask);
    }
}

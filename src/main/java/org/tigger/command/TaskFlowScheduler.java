package org.tigger.command;

import org.tigger.command.monitor.Event;
import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.LogicTaskNode;
import org.tigger.common.datastruct.TaskExecuteStatus;
import org.tigger.common.datastruct.TaskStatus;
import org.tigger.common.threadpool.ThreadPool;
import org.tigger.database.dao.entity.TigerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        List<LogicTaskNode> nodeListForExecute = head.getNextTigerTaskList();
        if (nodeListForExecute == null || nodeListForExecute.size() == 0) {
            //没有下一个节点时结束
            return;
        }
        //0. 可执行任务和等待任务(需要等待依赖的前面任务完成的任务)拆分
        List<TigerTask> tigerTaskList = new ArrayList<>();
        List<LogicTaskNode> waitingTaskList = new ArrayList<>();
        for (LogicTaskNode logicTaskNode : nodeListForExecute) {
            //前面有N个，这N个里面有没有执行完的，要等一等，等执行的执行机完了之后，大伙再重新分一下这个任务
            List<TigerTask> previousList = logicTaskNode.getPreviousTigerTaskList()
                    .stream().map(LogicTaskNode::getCurrentTigerTask)
                    .collect(Collectors.toList());
            if (hasExecutingTask(previousList)) {
                //等待任务
                waitingTaskList.add(logicTaskNode);
            } else {
                //可执行任务
                tigerTaskList.add(logicTaskNode.getCurrentTigerTask());
            }
        }
        //扔到外面线程池里去，你去等着吧，啥时候你那边前面的节点都执行完了你再过来，大伙一块分一下这个任务
        monitor(waitingTaskList);

        //1. 任务拆分，找到属于本IP的任务
        List<TigerTask> myTaskList = splitTask(tigerTaskList);
        if (myTaskList == null || myTaskList.size() == 0) {
            //没有本IP的任务，不用干活
            return;
        }
        //2. 任务执行(本IP上多线程执行）
        for (TigerTask tigerTask : myTaskList) {
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                //通知其它IP上本任务开始运行
                ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
                execute(tigerTask);
                //通知其它IP本任务运行结束
                ObjectFactory.instance().getEventListener().listen(Event.TASK_COMPLETE, null);
            });
        }
        //等上面的都执行完
        while (!(tigerTaskList.size() == 0 && waitingTaskList.size() == 0)) {
            //自旋等上面任务完成
        }
        //3. 递归并发遍历去执行下一批节点
        for (LogicTaskNode logicTaskNode : nodeListForExecute) {
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

    /**
     * 判断是不是还有IP没执行完
     *
     * @param previousList 前面任务
     * @return false:前面任务都已完成，true:前面任务非都已完成
     */
    private boolean hasExecutingTask(List<TigerTask> previousList) {
        for (TigerTask tigerTask : previousList) {
            List<TaskExecuteStatus> list1 = MemoryShareDataRegion.taskExecuteStatus.stream()
                    .filter(taskExecuteStatus -> taskExecuteStatus.getTigerTask().getId() == tigerTask.getId())
                    .collect(Collectors.toList());
            for (TaskExecuteStatus taskExecuteStatus : list1) {
                if (taskExecuteStatus.getTaskStatus() != TaskStatus.COMPLETE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 监控未完成的任务，完成后移出未完成队列
     *
     * @param waitingTaskList 未完成任务
     */
    private void monitor(List<LogicTaskNode> waitingTaskList) {
        ThreadPool.getThreadPoolExecutor().execute(() -> {
            for (LogicTaskNode logicTaskNode : waitingTaskList) {
                List<TigerTask> list = logicTaskNode.getPreviousTigerTaskList()
                        .stream().map(LogicTaskNode::getCurrentTigerTask).collect(Collectors.toList());
                if (!hasExecutingTask(list)) {
                    waitingTaskList.remove(logicTaskNode);
                }
            }
        });
    }
}

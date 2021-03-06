package org.tiger.command;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.monitor.EventListener;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.LogicTaskNode;
import org.tiger.common.datastruct.TaskExecuteStatus;
import org.tiger.common.datastruct.TaskStatus;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.thread.ThreadPool;
import org.tiger.common.util.ThreadUtil;
import org.tiger.common.util.TigerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * tiger调度器  (最核心的部分)
 *
 * @author kangshaofei
 * @date 2020-01-19
 */
@SingletonBean
public class TaskFlowScheduler {

    private Logger logger = LoggerFactory.getLogger(TaskFlowScheduler.class.getSimpleName());

    @Inject
    private TigerTaskExecutor tigerTaskExecutor;

    @Inject
    private EventListener eventListener;

    @Inject
    private DefaultTaskSplitStrategy defaultTaskSplitStrategy;

    /**
     * 遍历并执行任务
     *
     * @param head 任务头节点
     */
    public void iterateAndExecute(LogicTaskNode head) {
        String taskName = head.getCurrentTigerTask() == null ? null : head.getCurrentTigerTask().getTaskName();
        logger.info("遍历并执行{}后面的任务", taskName);
        //LOOP 任务遍历
        List<LogicTaskNode> nodeListForExecute = head.getNextTigerTaskList();
        if (nodeListForExecute == null || nodeListForExecute.size() == 0) {
            //没有下一个节点时结束
            logger.info("没有下一个（批）节点,任务执行结束。");
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
            if (hasExecutingTask(previousList)) {// TODO 有个BUG，
                //等待任务
                logger.info("此任务前面有正在执行的任务，进入等待队列..{}", logicTaskNode.getCurrentTigerTask().getTaskName());
                waitingTaskList.add(logicTaskNode);
            } else {
                //可执行任务
                logger.info("此任务可执行:{}", logicTaskNode.getCurrentTigerTask().getTaskName());
                tigerTaskList.add(logicTaskNode.getCurrentTigerTask());
            }
        }
        //扔到外面线程池里去，你去等着吧，啥时候你那边前面的节点都执行完了你再过来，大伙一块分一下这个任务
        monitor(waitingTaskList);

        //1. 任务拆分，找到属于本IP的任务
        List<TigerTask> myTaskList = splitTask(tigerTaskList);
        if (myTaskList == null || myTaskList.size() == 0) {
            //没有本IP的任务，不用干活
            logger.info("没有本IP的任务，不用干活");
            return;
        }
        //2. 任务执行(本IP上多线程执行）
        for (TigerTask tigerTask : myTaskList) {
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                //通知其它IP上本任务开始运行
                //TODO 创建 TigerTaskExecute实例入库
                Map<String, Object> param = TigerUtil.buildTigerTaskExecutionParamMap(null, tigerTask);
                eventListener.listen(Event.TASK_START, param);
                logger.info("开始执行{}任务", tigerTask.getTaskName());
                execute(tigerTask);
                logger.info("执行{}任务结束", tigerTask.getTaskName());
                //通知其它IP本任务运行结束
                eventListener.listen(Event.TASK_COMPLETE, param);
                synchronized (this) {
                    myTaskList.remove(tigerTask);
                }
            });
        }
        //等上面的都执行完
        while (!(myTaskList.size() == 0 && waitingTaskList.size() == 0)) {
            //自旋等上面任务完成
            logger.info("等待以上任务执行完成...myTaskList{},waitingTaskList.size{}", JSON.toJSONString(myTaskList), waitingTaskList.size());
            ThreadUtil.sleep(1000);
        }
        logger.info("执行{}后面的任务结束,递归并发遍历去执行下一批节点", taskName);
        //3. 递归并发遍历去执行下一批节点，一批的话是多个，肯定是并行
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
        Map<String, List<TigerTask>> map = defaultTaskSplitStrategy.splitTaskFlow(tigerTaskList);
        return map.get(MemoryShareDataRegion.localIp);
    }

    /**
     * 执行用户自己的业务逻辑
     *
     * @param tigerTask 任务
     */
    private void execute(TigerTask tigerTask) {
        tigerTaskExecutor.executeTask(tigerTask);
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
                    .filter(taskExecuteStatus -> taskExecuteStatus.getTigerTask().getId().equals(tigerTask.getId()))
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
        for (LogicTaskNode logicTaskNode : waitingTaskList) {
            ThreadPool.getThreadPoolExecutor().execute(() -> {
                while (true) {
                    logger.info("监控未完成的任务{}", logicTaskNode.getCurrentTigerTask().getTaskName());
                    ThreadUtil.sleep(1000);
                    List<TigerTask> list = logicTaskNode.getPreviousTigerTaskList()
                            .stream().map(LogicTaskNode::getCurrentTigerTask).collect(Collectors.toList());
                    if (!hasExecutingTask(list)) {
                        waitingTaskList.remove(logicTaskNode);
                        logger.info("监控未完成的任务{}已完成", logicTaskNode.getCurrentTigerTask().getTaskName());
                        break;
                    }
                }
            });
        }
    }
}

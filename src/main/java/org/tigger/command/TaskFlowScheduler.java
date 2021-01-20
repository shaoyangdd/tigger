package org.tigger.command;

import org.tigger.command.monitor.Event;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.ObjectFactory;
import org.tigger.database.dao.TigerTaskDao;
import org.tigger.database.dao.TigerTaskExecuteDao;
import org.tigger.database.dao.TigerTaskFlowDao;
import org.tigger.database.dao.entity.TigerTask;
import org.tigger.database.dao.entity.TigerTaskExecute;
import org.tigger.database.dao.entity.TigerTaskFlow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * tiger调度器  核心部分
 * @author kangshaofei
 * @date 2020-01-19
 */
public class TaskFlowScheduler {

    private static Logger logger = Logger.getLogger(TaskFlowScheduler.class.getSimpleName());

    public void execute(String previousId) {
        //TODO 下面这一堆代码要改成使用TaskFlowGraph来遍历
        List<TigerTaskFlow> tigerTaskFlowList = TigerTaskFlowDao.getTigerTaskFlowByPreviousId(previousId);
        if (tigerTaskFlowList.size() == 0) {
            return;
        }
        List<TigerTask> tigerTaskList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        for (TigerTaskFlow tigerTaskFlow : tigerTaskFlowList) {
            tigerTaskList.add(TigerTaskDao.getTigerTaskByName(tigerTaskFlow.getTaskName()));
            idList.add(tigerTaskFlow.getId()+"");
        }
        TestTaskSplitStrategy testTaskSplitStrategy = new TestTaskSplitStrategy();
        Map<String, List<TigerTask>> map = testTaskSplitStrategy.splitTaskFlow(tigerTaskList);
        List<TigerTask> tigerTaskList1 = map.get(MemoryShareDataRegion.localIp);
        // 一个IP上执行并行tigerTaskList1.size()个任务
        for (TigerTask tigerTask : tigerTaskList1) {
            //插入一条任务执行、
            long id = insertRunning(tigerTask);
            //启另一个线程去记录资源状态
            ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
            //使用用户自定义的执行器执行任务(执行业务逻辑)
            boolean result;
            try {
                result = ObjectFactory.instance().getTaskExecutor().execute(tigerTask.getTaskName(), tigerTask.getTaskParameter());
            } catch (Exception e) {
                logger.info(e.getMessage());
                result = false;
            }
            // 更新执行状态记录耗时
            TigerTaskExecuteDao.updateAfterComplete(id, result);
            // 记录资源状态
            ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
        }
        for (String id : idList) {
            execute(id);
        }
    }

    private static long insertRunning (TigerTask tigerTask) {
        TigerTaskExecute tigerTaskExecute = new TigerTaskExecute();
        tigerTaskExecute.setTaskId(tigerTask.getId());
        tigerTaskExecute.setTaskExecutorIp(MemoryShareDataRegion.localIp);
        tigerTaskExecute.setStartTime(new Timestamp(System.currentTimeMillis()));
        tigerTaskExecute.setEndTime(null);
        tigerTaskExecute.setTaskStatus("R");
        tigerTaskExecute.setTaskParameter(tigerTask.getTaskParameter());
        return TigerTaskExecuteDao.insert(tigerTaskExecute);
    }
}

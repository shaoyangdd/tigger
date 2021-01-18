package org.tigger.command;

import org.tigger.command.monitor.Event;
import org.tigger.common.MemoryShareDataRegion;
import org.tigger.common.ObjectFactory;
import org.tigger.db.dao.TigerTaskDao;
import org.tigger.db.dao.TigerTaskExecuteDao;
import org.tigger.db.dao.TigerTaskFlowDao;
import org.tigger.db.dao.entity.TigerTask;
import org.tigger.db.dao.entity.TigerTaskExecute;
import org.tigger.db.dao.entity.TigerTaskFlow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TaskFlowScheduler {

    private static Logger logger = Logger.getLogger(TaskFlowScheduler.class.getSimpleName());

    public static void execute(String previousId) {
        List<TigerTaskFlow> tigerTaskFlowList = TigerTaskFlowDao.getTigerTaskFlow(previousId);
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
            ObjectFactory.getEventListener().listen(Event.TASK_START, null);
            // TODO 写个匿名内部类来实现，实际应该用实现类
            TaskExecutor taskExecutor = (taskName, parameter) -> {
                // TODO 此处可以调用业务逻辑的代码，比如用springBatch可以调起springBatchJob
                // TODO 根据taskName调用业务逻辑代码，传入参数。此处省略，用打印任务名和参数来模拟
                System.out.println("本IP:" + MemoryShareDataRegion.ipOrder + "执行:" +taskName + "任务,参数:" + parameter);
                return 4/2 == 2;
            };
            boolean result;
            try {
                result = taskExecutor.execute(tigerTask.getTaskName(), tigerTask.getTaskParameter());
            } catch (Exception e) {
                logger.info(e.getMessage());
                result = false;
            }
            // 更新执行状态记录耗时
            TigerTaskExecuteDao.updateAfterComplete(id, result);
            // 记录资源状态
            ObjectFactory.getEventListener().listen(Event.TASK_START, null);
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

package org.tigger.command;

import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.TigerTask;
import org.tigger.common.datastruct.TigerTaskExecute;
import org.tigger.persistence.database.dao.TigerTaskExecuteDao;

import java.sql.Timestamp;
import java.util.logging.Logger;

public class TigerTaskExecutor {

    private static Logger logger = Logger.getLogger(TaskFlowScheduler.class.getSimpleName());

    private TigerTaskExecuteDao tigerTaskExecuteDao;

    public boolean executeTask(TigerTask tigerTask) {
        //插入一条任务执行、
        long id = insertRunning(tigerTask);
        //启另一个线程去记录资源状态
        //ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
        boolean result;
        try {
            //使用用户自定义的执行器执行任务(执行业务逻辑)
            result = ObjectFactory.instance().getTaskExecutor().execute(tigerTask.getTaskName(), tigerTask.getTaskParameter());
        } catch (Exception e) {
            logger.info(e.getMessage());
            result = false;
        }
        // 更新执行状态记录耗时
        tigerTaskExecuteDao.updateAfterComplete(id, result);
        // 任务结束监听操作（广播状态等）
        //ObjectFactory.instance().getEventListener().listen(Event.TASK_COMPLETE, null);
        return result;
    }


    private long insertRunning(TigerTask tigerTask) {
        TigerTaskExecute tigerTaskExecute = new TigerTaskExecute();
        tigerTaskExecute.setTaskId(tigerTask.getId());
        tigerTaskExecute.setTaskExecutorIp(MemoryShareDataRegion.localIp);
        tigerTaskExecute.setStartTime(new Timestamp(System.currentTimeMillis()));
        tigerTaskExecute.setEndTime(null);
        tigerTaskExecute.setTaskStatus("R");
        tigerTaskExecute.setTaskParameter(tigerTask.getTaskParameter());
        return tigerTaskExecuteDao.insert(tigerTaskExecute);
    }

}

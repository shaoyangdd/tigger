package org.tiger.command;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.tiger.command.machine_learning.Calculator;
import org.tiger.command.machine_learning.ShardingParameter;
import org.tiger.common.ObjectFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.ioc.InjectByType;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.database.dao.TigerTaskExecuteDao;

import java.sql.Timestamp;
import java.util.logging.Logger;

import static org.tiger.common.Constant.SHARDING_PARAMETER_KEY;

/**
 * 任务执行器
 * 其实是一个模板，主要是调用用户的处理逻辑
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
@SingletonBean
public class TigerTaskExecutor {

    private static Logger logger = Logger.getLogger(TaskFlowScheduler.class.getSimpleName());

    private TigerTaskExecuteDao tigerTaskExecuteDao;

    @InjectByType
    private Calculator calculator;

    /**
     * 执行任务
     * @param tigerTask
     * @return
     */
    public boolean executeTask(TigerTask tigerTask) {
        //插入一条任务执行、
        long id = insertRunning(tigerTask);
        //启另一个线程去记录资源状态
        //ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
        boolean result;
        try {
            //计算分片参数
            ShardingParameter shardingParameter = calculator.getShardingParameter(tigerTask, MemoryShareDataRegion.standard);
            JSONObject jsonObject = JSON.parseObject(tigerTask.getTaskParameter());
            jsonObject.put(SHARDING_PARAMETER_KEY, shardingParameter);
            //使用用户自定义的执行器执行任务(执行业务逻辑)
            result = ObjectFactory.instance().getTaskExecutor().execute(tigerTask.getTaskName(), jsonObject.toJSONString());
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

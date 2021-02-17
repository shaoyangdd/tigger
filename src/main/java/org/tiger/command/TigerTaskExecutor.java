package org.tiger.command;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.machine_learning.Calculator;
import org.tiger.command.machine_learning.ShardingParameter;
import org.tiger.common.ObjectFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.ioc.Inject;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.persistence.file.FileDataPersistence;

import java.sql.Timestamp;

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

    private static Logger logger = LoggerFactory.getLogger(TaskFlowScheduler.class.getSimpleName());

    @InjectCustomBean
    private FileDataPersistence<TigerTaskExecute> tigerTaskExecuteDataPersistence;

    @Inject
    private Calculator calculator;

    /**
     * 执行任务
     *
     * @param tigerTask 任务
     * @return boolean 执行结果 true成功； false失败
     */
    public boolean executeTask(TigerTask tigerTask) {
        //插入一条运行状态的任务
        logger.info("插入一条运行状态的任务");
        long id = insertRunning(tigerTask);
        //启另一个线程去记录资源状态
        //ObjectFactory.instance().getEventListener().listen(Event.TASK_START, null);
        boolean result;
        try {
            //计算分片参数
            logger.info("计算分片参数");
            ShardingParameter shardingParameter = calculator.getShardingParameter(tigerTask);
            JSONObject jsonObject = tigerTask.getTaskParameter() == null ? new JSONObject() : JSON.parseObject(tigerTask.getTaskParameter());
            jsonObject.put(SHARDING_PARAMETER_KEY, shardingParameter);
            logger.info("执行业务逻辑,分片参数:{}", JSON.toJSONString(shardingParameter));
            //使用用户自定义的执行器执行任务(执行业务逻辑)
            result = ObjectFactory.instance().getTaskExecutor().execute(tigerTask.getTaskName(), jsonObject.toJSONString());
        } catch (Exception e) {
            logger.error("执行任务失败", e);
            result = false;
        }
        // 更新执行状态记录耗时
        logger.info("更新执行状态记录耗时,result:{}", result);
        TigerTaskExecute tigerTaskExecute = new TigerTaskExecute();
        tigerTaskExecute.setId(id);
        tigerTaskExecute.setTaskStatus(result ? "S" : "F");
        tigerTaskExecuteDataPersistence.update(tigerTaskExecute);
        // 任务结束监听操作（广播状态等）
        //ObjectFactory.instance().getEventListener().listen(Event.TASK_COMPLETE, null);
        return result;
    }

    /**
     * 插入一条正在运行状态的任务
     *
     * @param tigerTask 任务
     * @return long 主键ID
     */
    private long insertRunning(TigerTask tigerTask) {
        TigerTaskExecute tigerTaskExecute = new TigerTaskExecute();
        tigerTaskExecute.setTaskId(tigerTask.getId());
        tigerTaskExecute.setTaskExecutorIp(MemoryShareDataRegion.localIp);
        tigerTaskExecute.setStartTime(new Timestamp(System.currentTimeMillis()));
        tigerTaskExecute.setEndTime(null);
        tigerTaskExecute.setTaskStatus("R");
        tigerTaskExecute.setTaskParameter(tigerTask.getTaskParameter());
        return tigerTaskExecuteDataPersistence.insert(tigerTaskExecute);
    }
}

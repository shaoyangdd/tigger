package org.tiger.command.receive_event_handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TaskExecuteStatus;
import org.tiger.common.datastruct.TaskStatus;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.util.TigerUtil;

import java.util.Map;

import static org.tiger.common.Constant.IP;

/**
 * 任务结束监听动作
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
public class TaskCompleteEventHandler implements EventHandler {

    private Logger logger = LoggerFactory.getLogger(TaskCompleteEventHandler.class.getSimpleName());

    @Override
    public void handle(Map<String, ?> parameter) {
        String ip = (String) parameter.get(IP);
        TigerTask tigerTask = (TigerTask) parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_KEY);
        logger.info("任务结束监听动作:" + JSON.toJSONString(parameter));
        for (TaskExecuteStatus taskExecuteStatus : MemoryShareDataRegion.taskExecuteStatus) {
            if (ip.equals(taskExecuteStatus.getIp()) && taskExecuteStatus.getTigerTask().getTaskName()
                    .equals(tigerTask.getTaskName())) {
                logger.info("更新状态为complete:" + tigerTask.getTaskName());
                taskExecuteStatus.setTaskStatus(TaskStatus.COMPLETE);
                break;
            }
        }
    }
}

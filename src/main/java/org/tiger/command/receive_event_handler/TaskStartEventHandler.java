package org.tiger.command.receive_event_handler;

import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.datastruct.TaskExecuteStatus;
import org.tiger.common.datastruct.TaskStatus;
import org.tiger.common.datastruct.TigerTask;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.util.TigerUtil;

import java.util.Map;

import static org.tiger.common.Constant.IP;

/**
 * 任务开始监听动作
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
@SingletonBean
public class TaskStartEventHandler implements EventHandler {

    @Override
    public void handle(Map<String, ?> parameter) {
        TaskExecuteStatus taskExecuteStatus = new TaskExecuteStatus();
        taskExecuteStatus.setIp((String) parameter.get(IP));
        taskExecuteStatus.setTigerTask((TigerTask) parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_KEY));
        taskExecuteStatus.setTaskStatus(TaskStatus.RUNNING);
        MemoryShareDataRegion.taskExecuteStatus.add(taskExecuteStatus);
    }
}

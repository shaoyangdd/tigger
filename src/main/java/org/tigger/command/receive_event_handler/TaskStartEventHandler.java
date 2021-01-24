package org.tigger.command.receive_event_handler;

import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.datastruct.TaskExecuteStatus;
import org.tigger.common.datastruct.TaskStatus;
import org.tigger.common.util.TigerUtil;
import org.tigger.persistence.database.dao.entity.TigerTask;

import java.util.Map;

import static org.tigger.common.Constant.IP;

/**
 * 任务开始监听动作
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
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

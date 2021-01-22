package org.tigger.common.datastruct;

import org.tigger.database.dao.entity.TigerTask;

/**
 * 任务运行状态
 *
 * @author kangshaofei
 * @date 2020-01-22
 */
public class TaskExecuteStatus {

    private String ip;


    private TigerTask tigerTask;


    private TaskStatus taskStatus;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public TigerTask getTigerTask() {
        return tigerTask;
    }

    public void setTigerTask(TigerTask tigerTask) {
        this.tigerTask = tigerTask;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "TaskExecuteStatus{" +
                "ip='" + ip + '\'' +
                ", tigerTask=" + tigerTask +
                ", taskStatus=" + taskStatus +
                '}';
    }
}

package org.tigger.db.dao.entity;

import java.sql.Timestamp;

public class TigerTaskExecute {

    private long id;

    private long taskId;

    private Timestamp startTime;

    private Timestamp endTime;

    private String taskStatus;

    private String taskExecutorIp;

    private String taskParameter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskExecutorIp() {
        return taskExecutorIp;
    }

    public void setTaskExecutorIp(String taskExecutorIp) {
        this.taskExecutorIp = taskExecutorIp;
    }

    public String getTaskParameter() {
        return taskParameter;
    }

    public void setTaskParameter(String taskParameter) {
        this.taskParameter = taskParameter;
    }

    @Override
    public String toString() {
        return "TigerTaskExecute{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskExecutorIp='" + taskExecutorIp + '\'' +
                ", taskParameter='" + taskParameter + '\'' +
                '}';
    }
}

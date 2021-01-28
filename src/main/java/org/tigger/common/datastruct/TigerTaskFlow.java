package org.tigger.common.datastruct;

import org.tigger.persistence.file.Record;

/**
 * tiger作业流，对应作业表 tiger_task_flow
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class TigerTaskFlow implements Record {

    private long id;

    private String taskName;

    private long previousTaskId;

    private String previousTaskStatus;

    private long nextTaskId;

    private String taskParameter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskParameter() {
        return taskParameter;
    }

    public void setTaskParameter(String taskParameter) {
        this.taskParameter = taskParameter;
    }

    public long getPreviousTaskId() {
        return previousTaskId;
    }

    public void setPreviousTaskId(long previousTaskId) {
        this.previousTaskId = previousTaskId;
    }

    public String getPreviousTaskStatus() {
        return previousTaskStatus;
    }

    public void setPreviousTaskStatus(String previousTaskStatus) {
        this.previousTaskStatus = previousTaskStatus;
    }

    public long getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(long nextTaskId) {
        this.nextTaskId = nextTaskId;
    }
}

package org.tiger.common.datastruct;

/**
 * tiger作业流，对应作业表 tiger_task_flow
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class TigerTaskFlow extends AbstractRecord {

    private String taskName;

    /**
     * 多个previousTaskId，用逗号相割
     */
    private String previousTaskId;

    private String previousTaskStatus;

    /**
     * 多个nextTaskId，用逗号相割
     */
    private String nextTaskId;

    private String taskParameter;

    @Override
    public String getUnionKey() {
        return null;
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

    public String getPreviousTaskStatus() {
        return previousTaskStatus;
    }

    public void setPreviousTaskStatus(String previousTaskStatus) {
        this.previousTaskStatus = previousTaskStatus;
    }

    public String getPreviousTaskId() {
        return previousTaskId;
    }

    public void setPreviousTaskId(String previousTaskId) {
        this.previousTaskId = previousTaskId;
    }

    public String getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(String nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    @Override
    public int recordLength() {
        return 500;
    }
}

package org.tiger.common.datastruct;

import org.tiger.persistence.file.Field;

/**
 * tigger作业，对应作业表 tiger_task
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class TigerTask extends AbstractRecord {

    @Field(maxLength = 200)
    private String taskName;

    private String taskLayerId;

    private String taskProcessMode;

    private String taskParameter;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskLayerId() {
        return taskLayerId;
    }

    public void setTaskLayerId(String taskLayerId) {
        this.taskLayerId = taskLayerId;
    }

    public String getTaskParameter() {
        return taskParameter;
    }

    public void setTaskParameter(String taskParameter) {
        this.taskParameter = taskParameter;
    }

    public String getTaskProcessMode() {
        return taskProcessMode;
    }

    public void setTaskProcessMode(String taskProcessMode) {
        this.taskProcessMode = taskProcessMode;
    }

    @Override
    public String toString() {
        return "TigerTask{" +
                "id=" + this.getId() +
                ", taskName='" + taskName + '\'' +
                ", taskLayerId='" + taskLayerId + '\'' +
                ", taskParameter='" + taskParameter + '\'' +
                '}';
    }

    @Override
    public String getUnionKey() {
        return taskName;
    }


}

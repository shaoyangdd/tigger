package org.tigger.persistence.database.dao.entity;

import org.tigger.persistence.file.Field;

/**
 * tigger作业，对应作业表 tiger_task
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class TigerTask {

    private long id;

    @Field(maxLength = 200)
    private String taskName;

    private String taskLayerId;

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

    @Override
    public String toString() {
        return "TigerTask{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskLayerId='" + taskLayerId + '\'' +
                ", taskParameter='" + taskParameter + '\'' +
                '}';
    }
}

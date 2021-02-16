package org.tiger.common.datastruct;

/**
 * 任务调度
 *
 * @author 康绍飞
 * @date 2021/2/16 23:47
 */
public class TaskSchedule extends AbstractRecord {

    /**
     * 定时cron表达式
     */
    private String cron;

    /**
     * 起始任务ID
     */
    private Long startId;

    /**
     * 结束任务ID
     */
    private Long endId;

    /**
     * 参数
     */
    private String parameter;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getUnionKey() {
        return null;
    }
}

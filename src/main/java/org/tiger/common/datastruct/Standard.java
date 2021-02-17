package org.tiger.common.datastruct;

import java.math.BigDecimal;


/**
 * 性能、资源标准
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
public class Standard extends AbstractRecord {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务执行时间 秒
     */
    private Integer taskTime;

    /**
     * CPU使用率
     */
    private BigDecimal cpuUse;

    /**
     * 内存使用率
     */
    private BigDecimal memoryUse;

    /**
     * 磁盘使用率
     */
    private BigDecimal diskUse;

    /**
     * 磁盘IO使用率
     */
    private BigDecimal diskIoUse;

    /**
     * 网络IO使用率
     */
    private BigDecimal netIoUse;


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Integer taskTime) {
        this.taskTime = taskTime;
    }

    public BigDecimal getMemoryUse() {
        return memoryUse;
    }

    public void setMemoryUse(BigDecimal memoryUse) {
        this.memoryUse = memoryUse;
    }

    public BigDecimal getDiskUse() {
        return diskUse;
    }

    public void setDiskUse(BigDecimal diskUse) {
        this.diskUse = diskUse;
    }

    public BigDecimal getDiskIoUse() {
        return diskIoUse;
    }

    public void setDiskIoUse(BigDecimal diskIoUse) {
        this.diskIoUse = diskIoUse;
    }

    public BigDecimal getNetIoUse() {
        return netIoUse;
    }

    public void setNetIoUse(BigDecimal netIoUse) {
        this.netIoUse = netIoUse;
    }

    public BigDecimal getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(BigDecimal cpuUse) {
        this.cpuUse = cpuUse;
    }

    @Override
    public String getUnionKey() {
        return String.valueOf(taskId);
    }
}

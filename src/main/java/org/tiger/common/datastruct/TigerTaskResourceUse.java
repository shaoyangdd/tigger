package org.tiger.common.datastruct;

import java.math.BigDecimal;

/**
 * 作业执行资源消耗 对应tiger_task_resource_use
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class TigerTaskResourceUse extends AbstractRecord {

    private String taskExecuteId;

    private BigDecimal cpuUse;

    private BigDecimal memoryUse;

    private BigDecimal diskIoUse;

    private BigDecimal netUse;

    public String getTaskExecuteId() {
        return taskExecuteId;
    }

    public void setTaskExecuteId(String taskExecuteId) {
        this.taskExecuteId = taskExecuteId;
    }

    public BigDecimal getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(BigDecimal cpuUse) {
        this.cpuUse = cpuUse;
    }

    public BigDecimal getMemoryUse() {
        return memoryUse;
    }

    public void setMemoryUse(BigDecimal memoryUse) {
        this.memoryUse = memoryUse;
    }

    public BigDecimal getDiskIoUse() {
        return diskIoUse;
    }

    public void setDiskIoUse(BigDecimal diskIoUse) {
        this.diskIoUse = diskIoUse;
    }

    public BigDecimal getNetUse() {
        return netUse;
    }

    public void setNetUse(BigDecimal netUse) {
        this.netUse = netUse;
    }

    @Override
    public String getUnionKey() {
        return null;
    }
}

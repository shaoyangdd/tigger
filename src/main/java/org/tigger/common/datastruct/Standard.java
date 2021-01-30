package org.tigger.common.datastruct;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 性能、资源标准
 *
 * @author 康绍飞
 * @date 2021-01-31
 */
public class Standard {

    /**
     * 任务执行时间 秒
     */
    private Map<TigerTask, Integer> taskTime;

    /**
     * CPU使用率
     */
    private BigDecimal cpuUse;

    /**
     * TODO 其它更多，后面列举
     */

    public Map<TigerTask, Integer> getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Map<TigerTask, Integer> taskTime) {
        this.taskTime = taskTime;
    }

    public BigDecimal getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(BigDecimal cpuUse) {
        this.cpuUse = cpuUse;
    }
}

package org.tiger.common.datastruct;

import java.math.BigDecimal;

/**
 * cpu信息
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class CpuInfo {

    /**
     * cpu使用率 小数 0到1
     */
    private BigDecimal cpuUse;

    public BigDecimal getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(BigDecimal cpuUse) {
        this.cpuUse = cpuUse;
    }
}

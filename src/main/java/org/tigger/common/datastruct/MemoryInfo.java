package org.tigger.common.datastruct;

import java.math.BigDecimal;

/**
 * 内存信息
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class MemoryInfo {

    /**
     * 内存使用率
     */
    private BigDecimal usage;

    public BigDecimal getUsage() {
        return usage;
    }

    public void setUsage(BigDecimal usage) {
        this.usage = usage;
    }
}

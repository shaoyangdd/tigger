package org.tigger.common.datastruct;

import java.math.BigDecimal;

/**
 * 带宽使用信息
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class NetInfo {

    /**
     * 带宽使用率
     */
    private BigDecimal usage;

    public BigDecimal getUsage() {
        return usage;
    }

    public void setUsage(BigDecimal usage) {
        this.usage = usage;
    }
}

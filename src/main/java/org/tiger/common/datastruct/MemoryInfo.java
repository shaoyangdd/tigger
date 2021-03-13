package org.tiger.common.datastruct;

import java.math.BigDecimal;

/**
 * 内存信息
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class MemoryInfo extends AbstractRecord {

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

    @Override
    public String getUnionKey() {
        return null;
    }

    @Override
    public int recordLength() {
        return 500;
    }
}

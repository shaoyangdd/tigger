package org.tigger.common.datastruct;

import java.math.BigDecimal;

/**
 * 磁盘IO信息
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class DiskIoInfo {

    private BigDecimal ioUsage;

    public BigDecimal getIoUsage() {
        return ioUsage;
    }

    public void setIoUsage(BigDecimal ioUsage) {
        this.ioUsage = ioUsage;
    }
}

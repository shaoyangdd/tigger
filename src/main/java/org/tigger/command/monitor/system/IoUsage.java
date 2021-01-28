package org.tigger.command.monitor.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigger.common.datastruct.DiskIoInfo;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 采集磁盘IO使用率
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class IoUsage implements ResourceUsage {

    private static Logger log = LoggerFactory.getLogger(IoUsage.class.getSimpleName());

    private static IoUsage INSTANCE = new IoUsage();

    private IoUsage() {

    }

    public static IoUsage getInstance() {
        return INSTANCE;
    }

    /**
     * 采集磁盘IO使用率
     *
     * @return
     */
    public DiskIoInfo get() {
        log.info("开始收集磁盘IO使用率");
        BigDecimal ioUsage = BigDecimal.ZERO;
        Process pro;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -x";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count = 0;
            while ((line = in.readLine()) != null) {
                if (++count >= 4) {
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    if (temp.length > 1) {
                        BigDecimal util = new BigDecimal(temp[temp.length - 1]);
                        ioUsage = (ioUsage.compareTo(util) > 0) ? ioUsage : util;
                    }
                }
            }
            if (ioUsage.compareTo(BigDecimal.ZERO) > 0) {
                log.info("本节点磁盘IO使用率为: " + ioUsage);
                ioUsage = ioUsage.divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("IoUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        DiskIoInfo diskIoInfo = new DiskIoInfo();
        diskIoInfo.setIoUsage(ioUsage);
        return diskIoInfo;
    }
}
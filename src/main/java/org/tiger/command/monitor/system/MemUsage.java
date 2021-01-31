package org.tiger.command.monitor.system;

import org.tiger.common.datastruct.MemoryInfo;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

/**
 * 采集内存使用率
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class MemUsage implements ResourceUsage {

    private static Logger log = Logger.getLogger(MemUsage.class.getSimpleName());

    private static MemUsage INSTANCE = new MemUsage();

    private MemUsage() {

    }

    public static MemUsage getInstance() {
        return INSTANCE;
    }

    /**
     * 采集内存使用率
     *
     * @return MemUsage
     */
    public MemoryInfo get() {
        log.info("开始收集memory使用率");
        BigDecimal memUsage = BigDecimal.ZERO;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/meminfo";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count = 0;
            long totalMem = 0, freeMem = 0;
            while ((line = in.readLine()) != null) {
                log.info(line);
                String[] memInfo = line.split("\\s+");
                if (memInfo[0].startsWith("MemTotal")) {
                    totalMem = Long.parseLong(memInfo[1]);
                }
                if (memInfo[0].startsWith("MemFree")) {
                    freeMem = Long.parseLong(memInfo[1]);
                }
                memUsage = new BigDecimal("1").subtract(new BigDecimal(freeMem)
                        .divide(new BigDecimal(totalMem)).setScale(2, RoundingMode.HALF_UP));
                log.info("本节点内存使用率为: " + memUsage);
                if (++count == 2) {
                    break;
                }
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.info("MemUsage发生InstantiationException. " + e.getMessage());
            log.info(sw.toString());
        }
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setUsage(memUsage);
        return memoryInfo;
    }
}
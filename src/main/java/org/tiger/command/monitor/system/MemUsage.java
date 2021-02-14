package org.tiger.command.monitor.system;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.datastruct.MemoryInfo;
import org.tiger.common.util.SystemUtil;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 采集内存使用率
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class MemUsage implements ResourceUsage {

    private static Logger log = LoggerFactory.getLogger(MemUsage.class.getSimpleName());

    private static MemUsage INSTANCE = new MemUsage();

    private MemUsage() {

    }

    public static MemUsage getInstance() {
        return INSTANCE;
    }

    public static MemoryInfo getByWindows() {
        log.info("getByWindows...");
        int kb = 1024 * 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 操作系统
        String osName = System.getProperty("os.name");
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        // 已使用的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb
                .getFreePhysicalMemorySize()) / kb;
        log.info("osName:{},totalMemorySize:{}Mb,freePhysicalMemorySize:{}Mb,usedMemory:{}Mb", osName, totalMemorySize, freePhysicalMemorySize, usedMemory);
        BigDecimal memUsage = new BigDecimal(usedMemory).divide(new BigDecimal(totalMemorySize), 2, BigDecimal.ROUND_HALF_UP);
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setUsage(memUsage);
        return memoryInfo;
    }


    /**
     * 采集内存使用率
     *
     * @return MemUsage
     */
    public MemoryInfo get() {
        log.info("开始收集memory使用率");
        if (SystemUtil.isWindows()) {
            return getByWindows();
        } else if (SystemUtil.isLinux()) {
            return getByLinux();
        } else if (SystemUtil.isMacOs()) {
            //TODO 后面支持
            return null;
        } else {
            throw new RuntimeException("不支持的操作系统");
        }
    }

    public MemoryInfo getByLinux() {
        log.info("开始收集memory使用率,linux");
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
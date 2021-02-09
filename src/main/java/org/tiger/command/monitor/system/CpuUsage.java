package org.tiger.command.monitor.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.datastruct.CpuInfo;
import org.tiger.common.util.SystemUtil;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 采集CPU使用率
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class CpuUsage {

    private static Logger log = LoggerFactory.getLogger(CpuUsage.class.getSimpleName());

    private static CpuUsage INSTANCE = new CpuUsage();

    private CpuUsage() {
    }

    public static CpuUsage getInstance() {
        return INSTANCE;
    }

    /**
     * 采集CPU信息
     *
     * @return cpuInfo
     */
    public CpuInfo get() {
        BigDecimal cpuUsage = null;
        if (SystemUtil.isLinux()) {
            cpuUsage = getLinuxCpuUsage();
        } else if (SystemUtil.isWindows()) {
            cpuUsage = WindowsCpuUtil.getCpuUsage();
        } else if (SystemUtil.isMacOs()) {
            //TODO 后面支持
        }
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setCpuUse(cpuUsage);
        return cpuInfo;
    }

    private BigDecimal getLinuxCpuUsage() {
        log.info("开始收集cpu使用率");
        BigDecimal cpuUsage = null;
        Process pro1, pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/stat";
            //第一次采集CPU时间
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line;
            long idleCpuTime1 = 0;
            long totalCpuTime1 = 0;    //分别为系统启动后空闲的CPU时间和总的CPU时间
            while ((line = in1.readLine()) != null) {
                if (line.startsWith("cpu")) {
                    line = line.trim();
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime1 = Long.parseLong(temp[4]);
                    for (String s : temp) {
                        if (!s.equals("cpu")) {
                            totalCpuTime1 += Long.parseLong(s);
                        }
                    }
                    log.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            //第二次采集CPU时间
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long idleCpuTime2 = 0, totalCpuTime2 = 0;    //分别为系统启动后空闲的CPU时间和总的CPU时间
            while ((line = in2.readLine()) != null) {
                if (line.startsWith("cpu")) {
                    line = line.trim();
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime2 = Long.parseLong(temp[4]);
                    for (String s : temp) {
                        if (!s.equals("cpu")) {
                            totalCpuTime2 += Long.parseLong(s);
                        }
                    }
                    log.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
                    break;
                }
            }
            if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
                BigDecimal idle = new BigDecimal(idleCpuTime2 - idleCpuTime1)
                        .divide(new BigDecimal(totalCpuTime2 - totalCpuTime1)).setScale(2, RoundingMode.HALF_UP);
                cpuUsage = new BigDecimal("1").subtract(idle);
                log.info("本节点CPU使用率为: " + cpuUsage);
            }
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("CpuUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return cpuUsage;
    }
}
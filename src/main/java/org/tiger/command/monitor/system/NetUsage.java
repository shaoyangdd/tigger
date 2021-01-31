package org.tiger.command.monitor.system;

import org.tiger.common.datastruct.NetInfo;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

/**
 * 采集网络带宽使用率
 *
 * @author 康绍飞
 * @date 2021-01-28
 */
public class NetUsage implements ResourceUsage {

    private static Logger log = Logger.getLogger(NetUsage.class.getSimpleName());

    private static NetUsage INSTANCE = new NetUsage();

    private final static float TotalBandwidth = 1000;    //网口带宽,Mbps

    private NetUsage() {

    }

    public static NetUsage getInstance() {
        return INSTANCE;
    }

    /**
     * @Purpose:
     * @param args
     * @return float, 网络带宽使用率, 小于1
     */

    /**
     * 采集网络带宽使用率
     *
     * @return 网络使用信息
     */
    public NetInfo get() {
        log.info("开始收集网络带宽使用率");
        BigDecimal netUsage = BigDecimal.ZERO;
        Process pro1, pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;
            while ((line = in1.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("eth0")) {
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize1 = Long.parseLong(temp[0].substring(5));    //Receive bytes,单位为Byte
                    outSize1 = Long.parseLong(temp[8]);                //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.info("NetUsage休眠时发生InterruptedException. " + e.getMessage());
                log.info(sw.toString());
            }
            //第二次采集流量数据
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0, outSize2 = 0;
            while ((line = in2.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("eth0")) {
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize2 = Long.parseLong(temp[0].substring(5));
                    outSize2 = Long.parseLong(temp[8]);
                    break;
                }
            }
            if (inSize1 != 0 && outSize1 != 0 && inSize2 != 0 && outSize2 != 0) {
                BigDecimal interval = new BigDecimal(endTime - startTime).divide(new BigDecimal("1000")).setScale(2, RoundingMode.HALF_UP);
                //网口传输速度,单位为bps
                BigDecimal interval100w = new BigDecimal("1000000").multiply(interval);
                BigDecimal curRate = new BigDecimal(inSize2 - inSize1 + outSize2 - outSize1).multiply(new BigDecimal("8")).divide(interval100w).setScale(2, RoundingMode.HALF_UP);
                netUsage = curRate.divide(new BigDecimal(TotalBandwidth));
                log.info("本节点网口速度为: " + curRate + "Mbps");
                log.info("本节点网络带宽使用率为: " + netUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.info("NetUsage发生InstantiationException. " + e.getMessage());
            log.info(sw.toString());
        }
        NetInfo netInfo = new NetInfo();
        netInfo.setUsage(netUsage);
        return netInfo;
    }
}
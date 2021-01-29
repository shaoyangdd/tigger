package org.tigger.command.monitor;

import com.alibaba.fastjson.JSON;
import org.tigger.command.Event;
import org.tigger.common.datastruct.JvmInfo;
import org.tigger.common.datastruct.TigerTaskExecute;
import org.tigger.common.util.ThreadUtil;
import org.tigger.common.util.TigerUtil;
import org.tigger.persistence.DataPersistence;

import java.lang.management.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.tigger.common.Constant.MB;

/**
 * JVM监视器
 * @author kangshaofei
 * @date 2020-01-16
 */
public class JvmMonitor implements Monitor {

    private static Logger logger = Logger.getLogger(JvmMonitor.class.getSimpleName());

    private DataPersistence<JvmInfo> dataPersistence;

    private Map<String, Event> map = new ConcurrentHashMap<>();

    /**
     * 资源监控 启动一个线程，定时去获取JVM信息，如果任务结束，也就不再获取 //TODO 线程数可能不可控制，先这样，后面再优化
     *
     * @param event     事件
     * @param parameter 参数
     */
    @Override
    public void monitor(Event event, Map<String, ?> parameter) {
        TigerTaskExecute tigerTaskExecute = (TigerTaskExecute) parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_EXECUTE_KEY);
        if (tigerTaskExecute != null) {
            String executeId = String.valueOf(tigerTaskExecute.getId());
            if (event == Event.TASK_START) {
                map.put(executeId, event);
                new Thread(() -> {
                    while (map.get(executeId) == Event.TASK_START) {
                        ThreadUtil.sleep(1000);
                        //获取JVM信息
                        dataPersistence.insert(get());
                    }
                }).start();
            } else {
                map.put(executeId, Event.TASK_COMPLETE);
            }
        }
    }

    /**
     * 获取JVM信息
     *
     * @return JvmInfo
     */
    public JvmInfo get() {

        JvmInfo jvmInfo = new JvmInfo();

        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memorymbean.getHeapMemoryUsage();
        MemoryUsage noHeapUsage = memorymbean.getNonHeapMemoryUsage();
        jvmInfo.setInitHeapSize((usage.getInit() / MB));
        jvmInfo.setMaxHeapSize(usage.getMax() / MB);
        jvmInfo.setUsedHeapSize(usage.getUsed() / MB);
        jvmInfo.setInitNonHeapSize(noHeapUsage.getInit() / MB);
        jvmInfo.setMaxNonHeapSize(noHeapUsage.getMax() / MB);
        jvmInfo.setUsedNonHeapSize(noHeapUsage.getUsed() / MB);

        logger.info("初始化堆大小: " + usage.getInit() / MB + "MB");
        logger.info("最大堆大小: " + usage.getMax() / MB + "MB");
        logger.info("使用堆大小: " + usage.getUsed() / MB + "MB");
        logger.info("初始化非堆大小: " + noHeapUsage.getInit() / MB + "MB");
        logger.info("最大非堆大小: " + noHeapUsage.getMax() / MB + "MB");
        logger.info("使用非堆大小: " + noHeapUsage.getUsed() / MB + "MB");

        logger.info("全信息展示:");
        logger.info("Heap Memory Usage: " + memorymbean.getHeapMemoryUsage());
        logger.info("非堆内存使用: " + memorymbean.getNonHeapMemoryUsage());

        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        logger.info("===================java options=============== ");
        logger.info(JSON.toJSONString(inputArguments));

        logger.info("=======================通过java来获取相关系统状态============================ ");
        long total = Runtime.getRuntime().totalMemory() / MB;//Java 虚拟机中的内存总量,以字节为单位
        long free = Runtime.getRuntime().freeMemory() / MB;//Java 虚拟机中的空闲内存量
        long max = Runtime.getRuntime().maxMemory() / MB;

        logger.info("空闲内存量:" + free + "MB");
        logger.info("总的内存量:" + total + "MB");
        logger.info("最大内存量" + max + "MB");
        jvmInfo.setJvmFreeSize(free);
        jvmInfo.setJvmCurrentSize(total);
        jvmInfo.setJvmMaxSize(max);


        //获取整个虚拟机内存使用情况
        logger.info("=======================MemoryMXBean============================ ");
        MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
        logger.info("堆使用 " + mm.getHeapMemoryUsage());
        logger.info("非堆使用 " + mm.getNonHeapMemoryUsage());
        //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况
        logger.info("=======================ThreadMXBean============================ ");
        ThreadMXBean tm = ManagementFactory.getThreadMXBean();
        logger.info("线程总数： " + tm.getThreadCount());
        logger.info("高峰线程数: " + tm.getPeakThreadCount());
        logger.info("当前线程CPU使用时间： " + tm.getCurrentThreadCpuTime());
        logger.info("守护线程总数: " + tm.getDaemonThreadCount());
        logger.info("当前线程用户时间 " + tm.getCurrentThreadUserTime());
        jvmInfo.setThreadTotalCount(tm.getThreadCount());
        jvmInfo.setPeakThreadCount(tm.getPeakThreadCount());
        jvmInfo.setThreadCpuTime(tm.getCurrentThreadCpuTime());
        jvmInfo.setDemonThreadCount(tm.getDaemonThreadCount());
        jvmInfo.setThreadUserTime(tm.getCurrentThreadUserTime());


        //获取GC的次数以及花费时间之类的信息
        logger.info("=======================MemoryPoolMXBean============================ ");
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcm : gcmList) {
            logger.info("getName " + gcm.getName());
            logger.info("getMemoryPoolNames " + gcm.getMemoryPoolNames());
            logger.info("GC次数:" + gcm.getCollectionCount());
        }

        return jvmInfo;
    }
}

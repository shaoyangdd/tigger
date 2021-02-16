package org.tiger.command.monitor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.command.Event;
import org.tiger.command.monitor.system.CpuUsage;
import org.tiger.command.monitor.system.MemUsage;
import org.tiger.common.datastruct.CpuInfo;
import org.tiger.common.datastruct.MemoryInfo;
import org.tiger.common.datastruct.TigerTaskExecute;
import org.tiger.common.datastruct.TigerTaskResourceUse;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.thread.ThreadPool;
import org.tiger.common.util.ThreadUtil;
import org.tiger.common.util.TigerUtil;
import org.tiger.persistence.file.FileDataPersistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统监视器
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
@SingletonBean
public class SystemMonitor implements Monitor {

    private static Logger logger = LoggerFactory.getLogger(SystemMonitor.class.getSimpleName());

    @InjectCustomBean
    private FileDataPersistence<TigerTaskResourceUse> systemMonitorFileDataPersistence;

    private Map<String, Event> map = new ConcurrentHashMap<>();

    @InjectParameter
    private String jvmMonitorInterval;

    /**
     * 资源监控 启动一个线程，定时去获取资源使用率，如果任务结束，也就不再获取 //TODO 线程数可能不可控制，先这样，后面再优化
     *
     * @param event     事件
     * @param parameter 参数
     */
    @Override
    public void monitor(Event event, Map<String, ?> parameter) {
        if (parameter != null) {
            TigerTaskExecute tigerTaskExecute = (TigerTaskExecute) parameter.get(TigerUtil.TIGER_TASK_PARAM_MAP_EXECUTE_KEY);
            if (tigerTaskExecute != null) {
                String executeId = String.valueOf(tigerTaskExecute.getId());
                Event existEvent = map.get(executeId);
                if (existEvent == null) {
                    //不存在此任务的开始事件
                    if (event == Event.TASK_START) {
                        map.put(executeId, event);
                        ThreadPool.getThreadPoolExecutor().execute(() -> {
                            logger.info("开始监控系统资源,executeId:{}", executeId);
                            //间隔采集CPU信息
                            while (map.get(executeId) == Event.TASK_START) {
                                //休眠一段时间再观察
                                ThreadUtil.sleep(Integer.parseInt(jvmMonitorInterval));
                                //获取CPU使用率
                                CpuInfo cpuInfo = CpuUsage.getInstance().get();
                                logger.info("CPU使用率:{}", cpuInfo.getCpuUse());
                                //获取IO使用率 TODO 先不支持
                                //DiskIoInfo diskIoInfo = IoUsage.getInstance().get();
                                //logger.info("磁盘使用率:{}", diskIoInfo.getIoUsage());
                                //获取内存使用率
                                MemoryInfo memoryInfo = MemUsage.getInstance().get();
                                logger.info("内存使用率:{}", memoryInfo.getUsage());
                                //获取网络使用率 TODO 先不支持
                                //NetInfo netInfo = NetUsage.getInstance().get();
                                TigerTaskResourceUse resourceUse = new TigerTaskResourceUse();
                                resourceUse.setTaskExecuteId(executeId);
                                resourceUse.setCpuUse(cpuInfo.getCpuUse());
                                //resourceUse.setDiskIoUse(diskIoInfo.getIoUsage());
                                //resourceUse.setNetUse(netInfo.getUsage());
                                resourceUse.setMemoryUse(memoryInfo.getUsage());
                                systemMonitorFileDataPersistence.insert(resourceUse);
                            }
                        });
                    }
                } else {
                    //不存在此任务的开始事件,并且本次为任务结束事件
                    if (existEvent == Event.TASK_START && event == Event.TASK_COMPLETE) {
                        map.remove(executeId);
                    }
                }
            }
        }
    }
}

package org.tiger.command.monitor;


import org.tiger.command.Event;
import org.tiger.command.monitor.system.CpuUsage;
import org.tiger.command.monitor.system.IoUsage;
import org.tiger.command.monitor.system.MemUsage;
import org.tiger.command.monitor.system.NetUsage;
import org.tiger.common.datastruct.*;
import org.tiger.common.ioc.InjectCustomBean;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.common.threadpool.ThreadPool;
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
                    if (event == Event.TASK_START) {
                        map.put(executeId, event);
                        ThreadPool.getThreadPoolExecutor().execute(() -> {
                            while (map.get(executeId) == Event.TASK_START) {
                                //休眠一段时间再观察
                                ThreadUtil.sleep(Integer.parseInt(jvmMonitorInterval));
                                //获取CPU使用率
                                CpuInfo cpuInfo = CpuUsage.getInstance().get();
                                //获取IO使用率
                                DiskIoInfo diskIoInfo = IoUsage.getInstance().get();
                                //获取内存使用率
                                MemoryInfo memoryInfo = MemUsage.getInstance().get();
                                //获取网络使用率
                                NetInfo netInfo = NetUsage.getInstance().get();
                                TigerTaskResourceUse resourceUse = new TigerTaskResourceUse();
                                resourceUse.setTaskExecuteId(executeId);
                                resourceUse.setCpuUse(cpuInfo.getCpuUse());
                                resourceUse.setDiskIoUse(diskIoInfo.getIoUsage());
                                resourceUse.setNetUse(netInfo.getUsage());
                                resourceUse.setMemoryUse(memoryInfo.getUsage());
                                systemMonitorFileDataPersistence.insert(resourceUse);
                            }
                        });
                    }
                } else {
                    map.put(executeId, Event.TASK_COMPLETE);
                }
            }
        }
    }
}

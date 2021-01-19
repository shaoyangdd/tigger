package org.tigger.common;

import org.tigger.command.TaskExecutor;
import org.tigger.command.TaskFlowScheduler;
import org.tigger.command.monitor.*;
import org.tigger.common.config.TigerConfiguration;
import org.tigger.communication.client.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象工厂
 * @author kangshaofei
 * @date 2020-01-16
 */
public class ObjectFactory {

    private TaskFlowScheduler taskFlowScheduler;

    private TaskExecutor taskExecutor;

    private static ObjectFactory objectFactory;

    private TigerConfiguration tigerConfiguration;

    private final Object lock = new Object();

    public static ObjectFactory instance() {
        //DCL模式
        if (objectFactory == null) {
            synchronized (ObjectFactory.class) {
                if (objectFactory == null) {
                    objectFactory = new ObjectFactory();
                }
            }
        }
        return objectFactory;
    }

    public Client getClient() {
        return new Client();
    }

    public EventListener getEventListener() {
        List<Monitor> monitorList = new ArrayList<>();
        monitorList.add(new AppMonitor());
        monitorList.add(new JvmMonitor());
        monitorList.add(new SystemMonitor());
        return new EventListener(monitorList);
    }

    public TaskFlowScheduler getTaskFlowScheduler() {
        //DCL模式
        if (taskFlowScheduler == null) {
            synchronized (lock) {
                if (taskFlowScheduler == null) {
                    taskFlowScheduler = new TaskFlowScheduler();
                }
            }
        }
        return taskFlowScheduler;
    }

    public TigerConfiguration getTigerConfiguration() {
        //DCL模式
        if (tigerConfiguration == null) {
            synchronized (lock) {
                if (tigerConfiguration == null) {
                    tigerConfiguration = new TigerConfiguration();
                }
            }
        }
        return tigerConfiguration;
    }

    public TaskExecutor setTaskExecutor(TaskExecutor taskExecutor) {
        //DCL模式
        if (this.taskExecutor == null) {
            synchronized (lock) {
                if (this.taskExecutor == null) {
                    this.taskExecutor = taskExecutor;
                }
            }
        }
        return taskExecutor;
    }

    public TaskExecutor getTaskExecutor() {
        if (this.taskExecutor == null) {
            throw new RuntimeException("请参考手册配置TaskExecutor!");
        }
        return taskExecutor;
    }
}

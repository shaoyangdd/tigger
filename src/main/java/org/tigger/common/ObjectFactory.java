package org.tigger.common;

import org.tigger.command.Event;
import org.tigger.command.TaskExecutor;
import org.tigger.command.TaskFlowScheduler;
import org.tigger.command.TigerTaskExecutor;
import org.tigger.command.monitor.*;
import org.tigger.command.receive_event_handler.EventHandlerRegistry;
import org.tigger.command.receive_event_handler.HeartbeatEventHandler;
import org.tigger.command.receive_event_handler.TaskCompleteEventHandler;
import org.tigger.command.receive_event_handler.TaskStartEventHandler;
import org.tigger.command.send_event_handler.HeartbeatSendEventHandler;
import org.tigger.common.config.TigerConfiguration;
import org.tigger.communication.client.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象工厂,为了代码轻量，不使用任何IOC框架
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class ObjectFactory {

    private TaskFlowScheduler taskFlowScheduler;

    private TaskExecutor taskExecutor;

    private static ObjectFactory objectFactory;

    private TigerConfiguration tigerConfiguration;

    private TigerTaskExecutor tigerTaskExecutor;

    private EventListener eventListener;

    private EventHandlerRegistry eventHandlerRegistry;

    private final Object lock = new Object();

    public static ObjectFactory instance() {
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
        if (eventListener == null) {
            synchronized (lock) {
                if (eventListener == null) {
                    List<Monitor> monitorList = new ArrayList<>();
                    monitorList.add(new AppMonitor());
                    monitorList.add(new JvmMonitor());
                    monitorList.add(new SystemMonitor());
                    monitorList.add(new WarnMonitor());
                    new EventListener(monitorList);
                }
            }
        }
        return eventListener;
    }

    public TaskFlowScheduler getTaskFlowScheduler() {
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
        if (tigerConfiguration == null) {
            synchronized (lock) {
                if (tigerConfiguration == null) {
                    tigerConfiguration = new TigerConfiguration();
                }
            }
        }
        return tigerConfiguration;
    }

    public TigerTaskExecutor getTigerExecutor() {
        if (tigerTaskExecutor == null) {
            synchronized (lock) {
                if (tigerTaskExecutor == null) {
                    tigerTaskExecutor = new TigerTaskExecutor();
                }
            }
        }
        return tigerTaskExecutor;
    }

    public TaskExecutor setTaskExecutor(TaskExecutor taskExecutor) {
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

    public EventHandlerRegistry getEventHandlerRegistry() {
        if (eventHandlerRegistry == null) {
            synchronized (lock) {
                if (eventHandlerRegistry == null) {
                    EventHandlerRegistry eventHandlerRegistry = new EventHandlerRegistry();
                    eventHandlerRegistry.registry(Event.TASK_START, new TaskStartEventHandler());
                    eventHandlerRegistry.registry(Event.TASK_COMPLETE, new TaskCompleteEventHandler());

                    eventHandlerRegistry.registry(Event.HEART_BEAT, new HeartbeatEventHandler());
                    eventHandlerRegistry.registry(Event.HEART_BEAT, new HeartbeatSendEventHandler());
                    return eventHandlerRegistry;
                }
            }
        }
        return eventHandlerRegistry;
    }
}

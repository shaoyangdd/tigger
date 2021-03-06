package org.tiger.common;

import org.tiger.command.Event;
import org.tiger.command.TaskExecutor;
import org.tiger.command.receive_event_handler.EventHandlerRegistry;
import org.tiger.command.receive_event_handler.HeartbeatEventHandler;
import org.tiger.command.receive_event_handler.TaskCompleteEventHandler;
import org.tiger.command.receive_event_handler.TaskStartEventHandler;
import org.tiger.command.send_event_handler.HeartbeatSendEventHandler;
import org.tiger.common.config.TigerConfiguration;
import org.tiger.communication.client.Client;

/**
 * 对象工厂
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
public class ObjectFactory {

    private TaskExecutor taskExecutor;

    private static ObjectFactory objectFactory;

    private TigerConfiguration tigerConfiguration;

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

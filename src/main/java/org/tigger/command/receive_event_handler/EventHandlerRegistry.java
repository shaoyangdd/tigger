package org.tigger.command.receive_event_handler;

import org.tigger.command.monitor.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络事件监视器注册容器
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
public class EventHandlerRegistry {

    private Map<Event, EventHandler> eventEventHandlerMap = new HashMap<>();

    /**
     * 网络事件监视器注册
     *
     * @param event        网络事件
     * @param eventHandler 事件监视处理器
     */
    public void registry(Event event, EventHandler eventHandler) {
        eventEventHandlerMap.put(event, eventHandler);
    }

    /**
     * 根据event获取 网络事件监视器
     *
     * @param event 网络事件
     * @return 事件监视处理器
     */
    public EventHandler getEventHandler(Event event) {
        return eventEventHandlerMap.get(event);
    }
}

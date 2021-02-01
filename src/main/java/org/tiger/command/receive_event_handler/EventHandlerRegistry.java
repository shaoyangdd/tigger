package org.tiger.command.receive_event_handler;

import org.tiger.command.Event;
import org.tiger.command.send_event_handler.SendEventHandler;
import org.tiger.common.ioc.SingletonBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络事件监视器注册容器
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
@SingletonBean
public class EventHandlerRegistry {

    private Map<Event, EventHandler> eventEventHandlerMap = new HashMap<>();

    private Map<Event, SendEventHandler> sendEventHandlerMap = new HashMap<>();

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

    /**
     * 网络事件监视器注册
     *
     * @param event        网络事件
     * @param eventHandler 事件监视处理器
     */
    public void registry(Event event, SendEventHandler eventHandler) {
        sendEventHandlerMap.put(event, eventHandler);
    }

    /**
     * 根据event获取 网络事件监视器
     *
     * @param event 网络事件
     * @return 事件监视处理器
     */
    public SendEventHandler getSendEventHandler(Event event) {
        return sendEventHandlerMap.get(event);
    }
}

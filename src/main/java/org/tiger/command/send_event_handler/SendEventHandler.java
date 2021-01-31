package org.tiger.command.send_event_handler;

import java.util.Map;

/**
 * 发送事件处理器
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
public interface SendEventHandler {

    /**
     * 发送处理
     *
     * @param parameter parameter
     */
    void sendHandle(Map<String, ?> parameter);

}

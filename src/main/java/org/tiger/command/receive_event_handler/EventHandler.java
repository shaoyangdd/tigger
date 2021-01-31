package org.tiger.command.receive_event_handler;

import java.util.Map;

/**
 * 网络事件监视器接口，主要监听其它IP的动作
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
public interface EventHandler {

    /**
     * 网络事件处理
     *
     * @param parameter 参数
     */
    void handle(Map<String, ?> parameter);
}

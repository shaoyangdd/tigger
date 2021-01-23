package org.tigger.command.monitor;

import org.tigger.command.Event;

import java.util.Map;

/**
 * 监视器接口
 * @author kangshaofei
 * @date 2020-01-19
 */
public interface Monitor {

    /**
     * 监视
     *
     * @param event     事件
     * @param parameter 参数
     */
    void monitor(Event event, Map<String, ? extends Object> parameter);

}

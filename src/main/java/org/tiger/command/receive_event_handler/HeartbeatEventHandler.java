package org.tiger.command.receive_event_handler;

import org.tiger.common.cache.MemoryShareDataRegion;

import java.util.Map;

import static org.tiger.common.Constant.IP;

/**
 * 心跳事件接收处理器
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
public class HeartbeatEventHandler implements EventHandler {

    @Override
    public void handle(Map<String, ?> parameter) {
        String ip = (String) parameter.get(IP);
        //第一次心跳覆盖，第N次更新成当前时间
        MemoryShareDataRegion.heartBeatTime.put(ip, System.currentTimeMillis());
    }
}

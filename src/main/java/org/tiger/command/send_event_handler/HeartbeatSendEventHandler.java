package org.tiger.command.send_event_handler;

import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.common.ioc.SingletonBean;
import org.tiger.communication.message.encoder.TigerMessageEncoder;

import java.util.Map;

import static org.tiger.communication.server.MessageType.HEARTBEAT;

/**
 * 心跳发送事件处理器
 *
 * @author kangshaofei
 * @date 2020-01-23
 */
@SingletonBean
public class HeartbeatSendEventHandler implements SendEventHandler {

    /**
     * 发送心跳
     *
     * @param parameter parameter
     */
    @Override
    public void sendHandle(Map<String, ?> parameter) {
        //发出去就好，不同步等待结果。
        //对方通过守护线程定期判断是否存活，判断算法为当前间隔*2<上次接收到的时间
        MemoryShareDataRegion.tigerRunningIpChannel.forEach((k, v) -> {
            v.writeAndFlush(TigerMessageEncoder.encode(HEARTBEAT.getMsgType(), "心跳"));
        });
    }
}

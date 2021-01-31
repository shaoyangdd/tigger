package org.tiger.communication.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProtobufClientHandler extends SimpleChannelInboundHandler<MessageProtobuf.Msg> {

    private Logger logger = Logger.getLogger(ProtobufClientHandler.class.getName());

    public static Map<String, Message> messageMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageProtobuf.Msg message) throws Exception {
        Message returnMessage = new Message(message);
        returnMessage.setTimestamp(System.currentTimeMillis());
        messageMap.put(message.getHead().getMsgId(), returnMessage);
        logger.info("客户端接到服务端的消息:" + JSON.toJSONString(new Message(message)));
    }
}
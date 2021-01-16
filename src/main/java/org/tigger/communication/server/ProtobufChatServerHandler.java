package org.tigger.communication.server;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.tigger.communication.client.MessageProtobuf;

/**
 * 聊天处理
 *
 * @author kangshaofei
 * @date 20200407
 */
@ChannelHandler.Sharable
public class ProtobufChatServerHandler extends SimpleChannelInboundHandler<MessageProtobuf.Msg> { // (1)

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        MessageService.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        MessageService.handlerRemoved(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) throws Exception { // (4)
        MessageService.processMessage(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        MessageService.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        MessageService.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        MessageService.exceptionCaught(ctx, cause);
    }


}
package org.tiger.communication.server;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.tiger.communication.client.MessageProtobuf;

/**
 * 服务处理
 *
 * @author kangshaofei
 * @date 20200407
 */
@ChannelHandler.Sharable
public class ProtobufServerHandler extends SimpleChannelInboundHandler<MessageProtobuf.Msg> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        MessageService.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        MessageService.handlerRemoved(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        MessageService.processMessage(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageService.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MessageService.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        MessageService.exceptionCaught(ctx, cause);
    }
}
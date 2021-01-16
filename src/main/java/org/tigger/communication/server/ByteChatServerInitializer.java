package org.tigger.communication.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.tigger.communication.client.MessageProtobuf;

public class ByteChatServerInitializer extends
        ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // protobufDecoder仅仅负责编码，并不支持读半包，所以在之前，一定要有读半包的处理器。
        // 有三种方式可以选择：
        // 使用netty提供ProtobufVarint32FrameDecoder
        // 继承netty提供的通用半包处理器 LengthFieldBasedFrameDecoder
        // 继承ByteToMessageDecoder类，自己处理半包
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufServerHandler());

        System.out.println("ByteChatServerInitializer:" + ch.remoteAddress() + "连接上");
    }
}
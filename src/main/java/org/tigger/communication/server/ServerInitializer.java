package org.tigger.communication.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class ServerInitializer extends
        ChannelInitializer<SocketChannel> {

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast("handler", new ServerHandler());
 
		System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
    }
}
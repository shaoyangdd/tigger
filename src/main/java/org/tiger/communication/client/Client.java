package org.tiger.communication.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private Logger logger = LoggerFactory.getLogger(Client.class.getName());

    private Channel channel;

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ProtobufClientInitializer());
            logger.info("连接IP:{},连接端口:{}", host, port);
            channel = bootstrap.connect(host, port).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("连接" + host + ":" + port + "失败");
        } finally {
            logger.info("IP{}没有tiger运行", host);
            group.shutdownGracefully();
        }
    }

    public void sendMessage(MessageProtobuf.Msg message) {
        channel.writeAndFlush(message);
    }

    public Channel getChannel() {
        return channel;
    }
}
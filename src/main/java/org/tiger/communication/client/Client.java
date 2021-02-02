package org.tiger.communication.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.tiger.common.ioc.SingletonBean;

import java.util.logging.Logger;

@SingletonBean
public class Client {

    private Logger logger = Logger.getLogger(Client.class.getName());

    private Channel channel;

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ProtobufClientInitializer());
            System.out.println("启动IM线程,连接IP:{},连接端口:{}" + host + ":" + port);
            channel = bootstrap.connect(host, port).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            logger.info("连接" + host + ":" + port + "失败");
        } finally {
            logger.info("停止IM线程失败");
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
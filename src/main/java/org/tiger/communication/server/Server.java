package org.tiger.communication.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiger.common.ioc.InjectParameter;
import org.tiger.common.ioc.SingletonBean;

/**
 * 服务器启动类
 *
 * @author kangshaofei
 * @date 2020-01-16
 */
@SingletonBean
public class Server {

    private Logger logger = LoggerFactory.getLogger(Server.class);

    @InjectParameter
    private String port;

    public Server() {
    }

    public void run() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ByteChatServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            logger.info("tiger服务器启动成功端口号:" + port);

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(Integer.parseInt(port)).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
            logger.info("server =====");
        } catch (Exception e) {
            logger.info("tiger服务器启动失败!");
          throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("tiger服务器关闭成功");
        }
    }
}
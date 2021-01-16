package org.tigger.communication.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.tigger.communication.client.Client;

import java.util.logging.Logger;

/**
 * 服务器启动类
 * 参考：https://www.cnblogs.com/damowang/p/6226167.html
 */
public class SimpleChatServer {

    private Logger logger = Logger.getLogger(Client.class.getName());
    
    private int port;

    public SimpleChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ByteChatServerInitializer())  //(4)
                    .option(ChannelOption.SO_BACKLOG, 1024)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true); // (6)

//            //设置TCP参数
//            //1.链接缓冲池的大小（ServerSocketChannel的设置）
//            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
//            //维持链接的活跃，清除死链接(SocketChannel的设置)
//            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
//            //关闭延迟发送
//            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            logger.info("实时聊天服务器启动成功端口号:" + port);

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("实时聊天服务器关闭成功");
        }
    }

    public static void main(String[] args) throws Exception {

        //启动TCP服务器
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8096;
        }
        new SimpleChatServer(port).run();

    }
}
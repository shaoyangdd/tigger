package org.tigger.communication.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> { // (1)

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


	//gz
//	private static int id = 0;
//	private static Map<String,Channel> channelMap = new HashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
		Channel incoming = ctx.channel();

		// Broadcast a message to multiple Channels
		channels.writeAndFlush("[客户端] - " + incoming.read().localAddress() + " | " +incoming.remoteAddress() + " 加入" + System.lineSeparator());
		
		channels.add(incoming);
		System.out.println(incoming.id().asShortText());
		System.out.println(incoming.id().asLongText());
		System.out.println();

//		//gz
//		ctx.channel().writeAndFlush(id+"");
//		channelMap.put(id+"",ctx.channel());
//		System.out.println(id + "jia ru,masize:" + channelMap.size() + "channelId:"  + ctx.channel().id().asLongText());
//		id = id+1;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
		Channel incoming = ctx.channel();
		
		// Broadcast a message to multiple Channels
		channels.writeAndFlush("[客户端] - " + incoming.remoteAddress() + " 离开");

		// A closed Channel is automatically removed from ChannelGroup,
		// so there is no need to do "channels.remove(ctx.channel());"
    }

    @Override
	protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception { // (4)
		Channel incoming = ctx.channel();
		System.out.println("[收到客户端" + incoming.id().asShortText() + "的消息:]" + s);
    	String channelId = s.split(",")[0];

		System.out.println("发送目标客户端channelId:" + channelId);


		for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
//            } else {
//            	channel.writeAndFlush("[you]" + s + "\n");
//            }
			System.out.println("遍历所有channelId:" + channel.id().asLongText() + " | " + channel.id().asShortText());
			if (channelId.equals(channel.id().asLongText())) {
				System.out.println("发消息到:" + channel.id().asLongText() + " | " + channel.id().asShortText());
				//TODO 注意末尾一定要加换行，不然发不出去！！！！！！
				channel.writeAndFlush("message: " + s + System.lineSeparator());
			}
        }


//		Channel toChannel = channelMap.get(s.split(",")[0]);
//		System.out.println(s + "toChannel:" + toChannel.id().asLongText());
//		channelMap.get(s.split(",")[0]).writeAndFlush(s);
	}
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
    	Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
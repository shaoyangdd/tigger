package org.tigger.communication.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tigger.command.Event;
import org.tigger.common.ObjectFactory;
import org.tigger.common.cache.MemoryShareDataRegion;
import org.tigger.common.util.TigerUtil;
import org.tigger.communication.client.Client;
import org.tigger.communication.client.Message;
import org.tigger.communication.client.MessageProtobuf;
import org.tigger.database.dao.entity.TigerTask;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.tigger.common.Constant.IP;
import static org.tigger.common.Constant.PORT;

/**
 * 消息服务
 */
public class MessageService {

    private static Logger logger = Logger.getLogger(Client.class.getName());

    /**
     * 所有信道
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 处理消息
     *
     * @param ctx ChannelHandlerContext
     * @param msg MessageProtobuf.Msg
     */
    public static void processMessage(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        MessageType messageType = MessageType.getMsgType(msg.getHead().getMsgType());
        String body = msg.getContent().getContent();
        String ip = ctx.channel().remoteAddress().toString();
        switch (messageType) {
            case ONLINE_NOTICE:
                processOnlineNotice(ctx, msg);
                break;
            case HEARTBEAT:
                Map<String, Object> param0 = new HashMap<>();
                param0.put(IP, ip);
                ObjectFactory.instance().getEventHandlerRegistry().getEventHandler(Event.HEART_BEAT).handle(param0);
                break;
            case TASK_START:
                TigerTask tigerTask = JSON.parseObject(body, TigerTask.class);
                Map<String, Object> param1 = new HashMap<>();
                param1.put(TigerUtil.TIGER_TASK_PARAM_MAP_KEY, tigerTask);
                param1.put(IP, ip);
                ObjectFactory.instance().getEventHandlerRegistry().getEventHandler(Event.TASK_START).handle(param1);
                break;
            case TASK_COMPLETE:
                TigerTask tigerTaskComplete = JSON.parseObject(body, TigerTask.class);
                Map<String, Object> param2 = new HashMap<>();
                param2.put(IP, ip);
                param2.put(TigerUtil.TIGER_TASK_PARAM_MAP_KEY, tigerTaskComplete);
                ObjectFactory.instance().getEventHandlerRegistry().getEventHandler(Event.TASK_COMPLETE).handle(param2);
                break;
            default:
                doNothing();
        }
    }

    private static void doNothing() {
    }

    /**
     * 其它实例上线，本机要处理的事
     * @param ctx ChannelHandlerContext
     * @param msg MessageProtobuf.Msg
     */
    private static void processOnlineNotice(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        Channel incoming = ctx.channel();
        logger.info("其它实例上线，处理开始...");

        // 缓存映射
        String ip = incoming.remoteAddress().toString();
        MemoryShareDataRegion.tigerRunningIpChannelS2C.put(ip,incoming);
        logger.info("缓存映射,IP:" + ip + "channel:" + incoming.id().asShortText());

        // 主动连它
        Channel channel = ObjectFactory.instance().getClient().connect(ip, PORT);
        if (channel != null) {
            MemoryShareDataRegion.tigerRunningIpChannel.put(ip,channel);
            logger.info("连接成功,IP:" + ip);
        } else {
            logger.info("连接失败,IP:" + ip);
        }

        logger.info("其它实例上线，处理结束");
    }

    private static void singleChat(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
//        Message message = new Message(msg);
//        long seq = GlobaleSeq.getSeq();
//        message.setSeq(seq);
//        Channel incoming = ctx.channel();
//        logger.info("收到客户端" + incoming.id().asShortText() + "发送消息:" + JSON.toJSONString(message));
//        String toUserId = msg.getHead().getTarget();
//        List<String> channelList = ChatRelationShip.getChannelIdByUserId(toUserId);
//        if (channelList == null || channelList.size() == 0) {
//            //发送离线消息
//            logger.info("用户{}不在线，缓存离线信息", toUserId);
//            OfflineService.recieveOfflineMessage(toUserId, MessageUtil.getMsg(message));
//        } else {
//            //发在线消息
//            for (Channel channel : channels) {
//                if (channelList.contains(channel.id().asLongText())) {
//                    logger.info("要发送的channel:" + channel.id().asLongText());
//                    channel.writeAndFlush(MessageUtil.getMsg(message));
//                }
//            }
//        }
//
//        String conversationId = getConversationId(message.getFrom(), message.getTarget());
//
//        //异步操作提升性能
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //保存会话id
//                if (!ChatRelationShip.getAllConversationId().contains(conversationId)) {
//                    logger.info("首次聊天，建立会话:{}", conversationId);
//                    new ChatSessionService().saveConversation(message, conversationId);
//                    ChatRelationShip.getAllConversationId().add(conversationId);
//                }
//
//                //消息持久化到数据库
//                logger.info("消息持久化到数据库：{}", conversationId);
//                new ChatRecordService().saveMessage(message, conversationId);
//            }
//        }).start();
//
//        //发送回执给消息发送方
//        Message successMsg = new Message(msg);
//        successMsg.setSeq(seq);
//        successMsg.setMsgType(MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
//        successMsg.setContent("消息发送成功！");
//        successMsg.setStatus(MessageStatus.Sent.value());
//        logger.info("消息状态回执：" + JSON.toJSONString(successMsg));
//        incoming.writeAndFlush(getMsg(successMsg));
    }

    private static void heartBeat(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        Channel incoming = ctx.channel();
        logger.info("收到客户端" + incoming.id().asShortText() + "的心跳消息:" + JSON.toJSONString(new Message(msg)));
        //心跳，原报文返回
        incoming.writeAndFlush(msg);
    }

    private static void queryOfflineMessage(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        //查询所有离线消息
//        logger.info("查询所有离线消息");
//        Message offlineMessage = new Message(msg);
//        String offMsg = OfflineService.getAllOfflineMessage();
//        offlineMessage.setContent(offMsg);
//        logger.info("查询所有离线消息:{}", offMsg);
//        ctx.channel().writeAndFlush(getMsg(offlineMessage));
    }

    private static void deleteOfflineMessage(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
//        logger.info("删除所有离线消息");
//        Message offlineMessage = new Message(msg);
//        OfflineService.deleteAll();
//        offlineMessage.setContent("删除所有离线消息成功");
//        logger.info("删除所有离线消息成功");
//        ctx.channel().writeAndFlush(getMsg(offlineMessage));
    }

    private static void queryOnlineUsers(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
        //查询所有在用户
//        logger.info("查询所有在用户");
//        Message onlineUser = new Message(msg);
//        String user = JSON.toJSONString(ChatRelationShip.getOnlineUsers());
//        onlineUser.setContent(user);
//        logger.info("所有在线用户:{}", user);
//        ctx.channel().writeAndFlush(getMsg(onlineUser));
    }

    private static void deleteOnlineUsers(ChannelHandlerContext ctx, MessageProtobuf.Msg msg) {
//        logger.info("删除所有在用户");
//        ChatRelationShip.deleteAllUser();
//        channels.clear();
//        Message offlineMessage = new Message(msg);
//        offlineMessage.setContent("删除所有在用户成功");
//        ctx.channel().writeAndFlush(getMsg(offlineMessage));
//        logger.info("删除所有在用户成功");
    }

    public static void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        // Broadcast a message to multiple Channels
        channels.writeAndFlush("客户端 - " + incoming.read().localAddress() + " | " + incoming.remoteAddress() + " 加入" + System.lineSeparator());
        // 加入缓存（注意最大值，一台机器支持的长连接有限）
        channels.add(incoming);
    }

    public static void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        channels.writeAndFlush("客户端 - " + incoming.remoteAddress() + " 离开");
        removeChannel(incoming);
    }

    /**
     * 从容器中移除channel
     *
     * @param channel
     */
    private static void removeChannel(Channel channel) {
//        logger.info("从容器中移除channel,channelId{}", channel.id().asLongText());
//        ChatRelationShip.removeChannel(channel.id().asLongText());
    }

    public static void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        logger.info(incoming.remoteAddress() + "上线, channelId:" + incoming.id().asLongText());
    }

    public static void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
//        Channel incoming = ctx.channel();
//        String incommingChannelId = incoming.id().asLongText();
//        logger.info(incoming.remoteAddress() + "掉线, channelId:" + incoming.id().asLongText());
//        String userId = ChatRelationShip.getUserIdByChannelId(incommingChannelId);
//        logger.info("掉线通道的UserId:{}", userId);
//        //广播给其它在线用户此用户已下线，TODO 现在先是发给除自己的所有人，后面考虑好友、群等等
//        for (Channel channel : channels) {
//            String channelId2 = channel.id().asLongText();
//            String userId2 = ChatRelationShip.getUserIdByChannelId(channelId2);
//            if (!userId.equals(userId2)) {
//                logger.info("广播给otherUserId{},channelId{},{}已下线", userId2, channelId2, userId);
//                channel.writeAndFlush(getBroadcastOfflineMsg(userId));
//            }
//        }
//        removeChannel(incoming);
    }

    public static void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
//        Channel incoming = ctx.channel();
//        logger.error(incoming.remoteAddress() + "发生异常", cause);
//        removeChannel(incoming);
//        ctx.close();
    }
}

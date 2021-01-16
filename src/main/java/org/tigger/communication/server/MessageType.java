package org.tigger.communication.server;

/**
 * 消息类型
 */
public enum MessageType {

    /*
     * 上线通知
     */
    ONLINE_NOTICE(1001),

    /*
     * 心跳消息
     */
    HEARTBEAT(1002),

    /*
     * 客户端提交的消息接收状态报告
     */
    CLIENT_MSG_RECEIVED_STATUS_REPORT(1009),

    /*
     * 服务端返回的消息发送状态报告
     */
    SERVER_MSG_SENT_STATUS_REPORT(1010),

    /**
     * 单聊消息
     */
    SINGLE_CHAT(2001),

    /**
     * 群聊消息
     */
    GROUP_CHAT(3001),

    /**
     * 广播上线消息
     */
    ONLINE_BROADCAST(3002),

    /**
     * 广播下线消息
     */
    OFFLINE_BROADCAST(3003),

    /**
     * 查询离线消息
     */
    QUERY_OFFLINE_MESSAGE(3004),

    /**
     * 删除离线消息
     */
    DELETE_OFFLINE_MESSAGE(3005),

    /**
     * 查询在线用户
     */
    QUERY_ONLINE_USER(3006),

    /**
     * 删除在线用户
     */
    DELETE_ONLINE_USER(3007),

    /**
     * 资金变动消息
     */
    MONEY_CHANGE(9001),

    /**
     * 评论消息
     */
    COMMENT(9002),

    /**
     * 点赞消息
     */
    LIKE(9003);

    private int msgType;

    MessageType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public static MessageType getMsgType(int msgType) {
        for (MessageType value : MessageType.values()) {
            if (value.getMsgType() == msgType) {
                return value;
            }
        }
        throw new RuntimeException("无此msgType:" + msgType);
    }

    public enum MessageContentType {

        /**
         * 文本消息
         */
        TEXT(101),

        /**
         * 图片消息
         */
        IMAGE(102),

        /**
         * 语音消息
         */
        VOICE(103);

        private int msgContentType;

        MessageContentType(int msgContentType) {
            this.msgContentType = msgContentType;
        }

        public int getMsgContentType() {
            return this.msgContentType;
        }
    }
}

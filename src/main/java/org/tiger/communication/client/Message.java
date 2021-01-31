package org.tiger.communication.client;

public class Message {

    /** ===header==== **/

    /**
     * 客户端id 由客户端生成，全局唯一，为了解决重复消息问题
     */
    private String msgId;

    /**
     * 服务端id 由服务端生成，为了解决离线消息排序问题
     */
    private long seq;

    /**
     * 发起ID 私聊时是用户ID
     */
    private String from;

    /**
     * 目标ID  单聊时是用户ID，群聊时是sessionId
     */
    private String target;

    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 扩展字段
     */
    private String extend;

    /**
     * 消息状态
     */
    private int status;

    /**
     * 时间戳 由服务端生成
     */
    private long timestamp;

    /** ====body==== **/

    /**
     * 消息类型
     */
    private int type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 链接 消息内容为图片、音频、视频、文件时使用链接
     */
    private String url;

    /**
     * 扩展字段
     */
    private String extra;

    public Message(Message message) {
        this.msgId = message.msgId;
        this.seq = message.seq;
        this.from = message.from;
        this.target = message.target;
        this.msgType = message.msgType;
        this.extend = message.extend;
        this.status = message.status;
        this.timestamp = message.timestamp;
        this.type = message.type;
        this.content = message.content;
        this.url = message.url;
        this.extra = message.extra;
    }

    public Message() {
    }

    public Message(MessageProtobuf.Msg msg) {
        /* ====header==== **/
        this.setMsgId(msg.getHead().getMsgId());
        this.setSeq(msg.getHead().getSeq());
        this.setMsgType(msg.getHead().getMsgType());
        this.setFrom(msg.getHead().getFrom());
        this.setTarget(msg.getHead().getTarget());
        this.setStatus(msg.getHead().getStatus());
        this.setExtend(msg.getHead().getExtend());
        this.setTimestamp(msg.getHead().getTimestamp());
        /* ====body==== **/
        this.setType(msg.getContent().getType());
        this.setContent(msg.getContent().getContent());
        this.setUrl(msg.getContent().getUrl());
        this.setExtra(msg.getContent().getExtra());
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

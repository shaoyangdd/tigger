package org.tiger.communication.message.encoder;

import org.tiger.common.cache.MemoryShareDataRegion;
import org.tiger.communication.client.MessageProtobuf;

import java.util.UUID;

import static org.tiger.common.Constant.EMPTY_STRING;


public class TigerMessageEncoder {

    /**
     * 消息编码
     *
     * @param messageType 消息类型
     * @param message     消息内容
     * @return protobuf消息对象
     */
    public static MessageProtobuf.Msg encode(int messageType, String message) {
        MessageProtobuf.Body.Builder bodyBuilder = MessageProtobuf.Body.newBuilder();
        MessageProtobuf.Head.Builder headOrBuilder = MessageProtobuf.Head.newBuilder();
        headOrBuilder.setSeq(0);
        headOrBuilder.setTarget(EMPTY_STRING);
        headOrBuilder.setTimestamp(System.currentTimeMillis());
        headOrBuilder.setMsgId(UUID.randomUUID().toString());
        headOrBuilder.setFrom(MemoryShareDataRegion.localIp);
        headOrBuilder.setMsgType(messageType);
        headOrBuilder.setExtend(EMPTY_STRING);
        headOrBuilder.setStatus(0);

        MessageProtobuf.Msg.Builder msgBuilder = MessageProtobuf.Msg.newBuilder();
        msgBuilder.setHead(headOrBuilder);
        bodyBuilder.setType(0);
        bodyBuilder.setUrl(EMPTY_STRING);
        bodyBuilder.setExtra(EMPTY_STRING);
        bodyBuilder.setContent(message);
        msgBuilder.setBody(bodyBuilder);
        return msgBuilder.build();
    }
}

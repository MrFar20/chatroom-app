package pers.mrwangx.tools.chatroom.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.alibaba.fastjson.JSON;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;

import pers.mrwangx.tools.chatroom.framework.server.session.Session;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:18
 **/
public class SimpleSession extends Session<Message> {

    public SimpleSession(SocketChannel cChannel, SelectionKey sKey, int sessionId) {
        super(cChannel, sKey, sessionId);
    }

    @Override
    public byte[] parseToByteData(Message message) {
        return JSON.toJSONString(message).getBytes();
    }

    @Override
    public Message registeBackMsg() {
        return Message.newBuilder()
                .fromId(FROM_SERVER)
                .toId(getSessionId())
                .type(Message.ALLOCATE_ID)
                .time(System.currentTimeMillis())
                .content(getSessionId() + "")
                .build();
    }
}

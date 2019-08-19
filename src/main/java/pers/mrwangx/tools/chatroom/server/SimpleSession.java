package pers.mrwangx.tools.chatroom.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Base64;

import com.alibaba.fastjson.JSON;

import pers.mrwangx.tools.chatroom.framework.protocol.Message;
import pers.mrwangx.tools.chatroom.framework.server.session.Session;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:18
 **/
public class SimpleSession extends Session {

	public SimpleSession(SocketChannel cChannel, SelectionKey sKey, int sessionId) {
		super(cChannel, sKey, sessionId);
	}

	@Override
	public byte[] parseToByteData(Message message) {
		return JSON.toJSONString(message).getBytes();
	}
}

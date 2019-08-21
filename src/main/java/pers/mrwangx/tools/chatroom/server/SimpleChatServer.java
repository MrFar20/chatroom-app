package pers.mrwangx.tools.chatroom.server;

import com.alibaba.fastjson.JSON;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;
import pers.mrwangx.tools.chatroom.framework.server.ChatServer;
import pers.mrwangx.tools.chatroom.framework.server.handler.Handler;
import pers.mrwangx.tools.chatroom.framework.server.session.Session;
import pers.mrwangx.tools.chatroom.framework.server.session.SessionManager;
import pers.mrwangx.tools.chatroom.framework.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/***
 * @author 王昊鑫
 * @description
 * @date 2019年08月07日 13:44
 ***/

public class SimpleChatServer extends ChatServer<Session<Message>, Message> {

	public SimpleChatServer(String host, int port, int timeout, int initSessionId, long heartBeatInterval, long heartBeatCheckInterval, SessionManager sessionManager, Handler handler, int MSG_SIZE) {
		super(host, port, timeout, initSessionId, heartBeatInterval, heartBeatCheckInterval, sessionManager, handler, MSG_SIZE);
	}

	public SimpleChatServer(String host, int port, int timeout, int initSessionId, long heartBeatInterval, long heartBeatCheckInterval, SessionManager sessionManager, Handler handler, int MSG_SIZE, ExecutorService executorService) {
		super(host, port, timeout, initSessionId, heartBeatInterval, heartBeatCheckInterval, sessionManager, handler, MSG_SIZE, executorService);
	}

	@Override
	public Message parseToMessage(Session session, byte[] data) {
		String strMsg = null;
		if (data != null) {
			try {
				strMsg = new String(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		};
		return strMsg == null ? null: JSON.parseObject(strMsg).toJavaObject(Message.class);
	}

	@Override
	public void sessionCreated(Session<Message> session) {
		broadCast(Message.
						newBuilder()
						.type(Message.MESSAGE)
						.time(System.currentTimeMillis())
						.toId(Message.TO_BROADCAST)
						.fromId(Message.FROM_SERVER)
						.content(StringUtil.str("[%s:%s]加入了聊天室", session.getSessionId(), session.getName()))
						.build()
		);
	}

	@Override
	protected Session newSession(SocketChannel channel) {
		return new SimpleSession(channel, sKey, currentSessionId.incrementAndGet());
	}

}

package pers.mrwangx.tools.chatroom.server;

import java.util.logging.Logger;

import pers.mrwangx.tools.chatroom.framework.protocol.Message;
import pers.mrwangx.tools.chatroom.framework.server.handler.Handler;
import pers.mrwangx.tools.chatroom.framework.server.session.Session;
import pers.mrwangx.tools.chatroom.framework.server.session.SessionManager;

import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;
import static pers.mrwangx.tools.chatroom.framework.util.StringUtil.str;

/***
 * @author 王昊鑫
 * @description
 * @date 2019年08月08日 14:25
 ***/
public class ServerHanler implements Handler<Session<Message>, Message> {

	public static final Logger log = Logger.getLogger("ChatServer");
	private SessionManager<Session> sessionManager;

	@Override
	public void handle(Session session, Message message) {
		int msgType = message.getType();
		log.info(str("处理请求:%s", message));
		switch (msgType) {
		case ALLOCATE_ID:
			allocateId(session, message);
			break;
		case UPDATE_NAME:
			updateName(session, message);
			break;
		case MESSAGE:
			message(session, message);
			break;
		case CMD:
			cmd(session, message);
			break;
		}
	}

	public void allocateId(Session session, Message message) {

	}

	public void updateName(Session session, Message message) {
		session.setName(message.getContent());
	}

	public void message(Session session, Message message) {
		if (message.getToId() == 0) { //广播消息
			message.setFromId(session.getSessionId());
			message.setName(session.getName());
			sessionManager.getSessions().forEach(s -> {
				if (s.getSessionId() != session.getSessionId()) {
					s.write(message);
				}
			});
		} else if (message.getToId() > 0) { //发送私信
			Session sessionTo = sessionManager.get(message.getToId());
			message.setFromId(session.getSessionId());
			message.setName(session.getName());
			sessionTo.write(message);
		}
	}

	public void cmd(Session session, Message message) {
		StringBuffer buffer = new StringBuffer();
		switch (message.getContent()) {
		case "list":
			sessionManager.getSessions().forEach(s -> {
				buffer.append(str("[ID:%d, 名字:%s]\n", s.getSessionId(), s.getName()));
			});
			Message msg1 = Message.newBuilder()
							.type(MESSAGE)
							.fromId(-1)
							.name("server")
							.toId(session.getSessionId())
							.time(System.currentTimeMillis())
							.content(buffer.toString())
							.build();
			session.write(msg1);
			break;
		}
	}

	public SessionManager<Session> getSessionManager() {
		return sessionManager;
	}

	public ServerHanler setSessionManager(SessionManager<Session> sessionManager) {
		this.sessionManager = sessionManager;
		return this;
	}
}



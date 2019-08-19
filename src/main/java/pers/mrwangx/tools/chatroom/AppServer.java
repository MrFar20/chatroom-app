package pers.mrwangx.tools.chatroom;

import pers.mrwangx.tools.chatroom.framework.server.session.Session;
import pers.mrwangx.tools.chatroom.framework.server.session.SessionManager;
import pers.mrwangx.tools.chatroom.framework.server.session.SimpleSessionManager;
import pers.mrwangx.tools.chatroom.server.ServerHanler;
import pers.mrwangx.tools.chatroom.server.SimpleChatServer;

/***
 * @author 王昊鑫
 * @description
 * @date 2019年08月08日 15:20
 ***/
public class AppServer {

	public static void main(String[] args) {
		run("localhost", 8066);
	}


	public static void run(String host, int port) {

		SessionManager<Session> sessionManager = new SimpleSessionManager();
		ServerHanler serverHanler = new ServerHanler();
		serverHanler.setSessionManager(sessionManager);
		SimpleChatServer chatServer = new SimpleChatServer(host, port, 1000, 0, sessionManager, serverHanler, 2048);
		chatServer.start();
	}

}

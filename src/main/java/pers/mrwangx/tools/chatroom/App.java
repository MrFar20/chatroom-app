package pers.mrwangx.tools.chatroom;

import pers.mrwangx.tools.chatroom.client.AppClient;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;
import pers.mrwangx.tools.chatroom.fx.APPWithGUI;
import pers.mrwangx.tools.chatroom.server.AppServer;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:22
 **/
public class App {

	public static final long heartBeatInterval = 10000;
	public static final long heartBeatCheckInterval = 60000;

	public static void main(String[] args) {
		String msg = "[options]" + System.lineSeparator()
						+ "cli						-启动客户端" + System.lineSeparator()
						+ "sver [host] [port]		-启动服务器" + System.lineSeparator()
						+ "guicli					-带GUI的客户端" + System.lineSeparator();
		if (args == null || args.length < 1) {
			System.out.println(msg);
		} else if (args[0].equals("cli")) {
			new AppClient().run();
		} else if (args[0].equals("sver") && args.length == 3) {
			AppServer.run(args[1], Integer.parseInt(args[2]));
		} else if (args[0].equals("guicli")) {
			APPWithGUI.main(args);
		} else {
			System.out.println(msg);
		}
	}

	public static Message newMessage(int type, String name, String content, int fromId, int toId, long time) {
		return Message.newBuilder()
						.type(type)
						.name(name)
						.content(content)
						.fromId(fromId)
						.toId(toId)
						.time(time)
						.build();
	}

}

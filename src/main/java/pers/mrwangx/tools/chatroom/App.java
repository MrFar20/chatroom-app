package pers.mrwangx.tools.chatroom;

import pers.mrwangx.tools.chatroom.framework.protocol.Message;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:22
 **/
public class App {

	public static final long heartBeatInterval = 10000;
	public static final long heartBeatCheckInterval = 60000;

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			System.out.println("请输入正确格式: server/client host port");
		} else if (args[0].equals("server")) {
			AppServer.run(args[1], Integer.parseInt(args[2]));
		} else if (args[0].equals("client")) {
			new AppClient().run(args[1], Integer.parseInt(args[2]));
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

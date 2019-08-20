package pers.mrwangx.tools.chatroom;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:22
 **/
public class App {

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			System.out.println("请输入正确格式: server/client host port");
		} else if (args[0].equals("server")) {
			AppServer.run(args[1], Integer.parseInt(args[2]));
		} else if (args[0].equals("client")) {
			AppClient.run(args[1], Integer.parseInt(args[2]));
		}

	}

}

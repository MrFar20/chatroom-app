package pers.mrwangx.tools.chatroom;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月19 9:22
 **/
public class App {

	public static void main(String[] args) {
		// System.out.println(ansi().eraseScreen().fg(RED).a("HELLO").fg(YELLOW).a(",WORLD").reset());
		if (args == null || args.length < 3) {
			System.out.println("请输入正确格式: server/client host port");
		} else if (args[0].equals("server")) {
			AppServer.run(args[1], Integer.parseInt(args[2]));
		} else if (args[0].equals("client")) {
			AppClient.run(args[1], Integer.parseInt(args[2]));
		}

	}

}

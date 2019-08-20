package pers.mrwangx.tools.chatroom;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pers.mrwangx.tools.chatroom.client.SimpleChatClient;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static pers.mrwangx.tools.chatroom.App.newMessage;
import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月15 9:56
 **/
public class AppClient {

	public static void main(String[] args) throws IOException {
		new AppClient().run("localhost", 8066);
	}

	public static final String SEND_MESSAGE = "sd";
	public static final String COMMAND = "cmd";
	public static final String UP_NAME = "upname";
	public static String reg;

	static {
		reg = "^(sd [0-9]+::.+)|(cmd .+)|(upname .+)$";
	}

	private Scanner input;
	private List<String> history;
	private SimpleChatClient client;

	public AppClient() {
		input = new Scanner(System.in);
		history = new ArrayList<>();
		client = new SimpleChatClient(App.heartBeatInterval);

	}

	public void run(String host, int port) {
		if (client.connect(host, port)) {
			String name = input("请输入您的名字:", false);
			client.sendMessage(newMessage(UPDATE_NAME, null, name, -1, -1, System.currentTimeMillis()));
			while (true) {
				String line = input();
				if (!line.matches(reg)) {
					println("操作命令不正确");
				} else {
					Message message = parseMessageFromInput(line);
					if (message != null) {
						client.sendMessage(message);
					}
				}
			}
		}
	}

	public Message parseMessageFromInput(String line) {
		if (line.startsWith("sd")) {
			String[] strs = line.split("::");
			return newMessage(MESSAGE, null, strs[1], -1, Integer.parseInt(strs[0].split(" ")[1]), System.currentTimeMillis());
		} else if (line.startsWith("cmd")) {
			String[] strs = line.split(" ");
			return newMessage(CMD, null, strs[1], -1, -1, System.currentTimeMillis());
		} else if (line.startsWith("upname")) {
			String[] strs = line.split(" ");
			return newMessage(UPDATE_NAME, null, strs[1], -1, -1, System.currentTimeMillis());
		} else if (line.startsWith("help")) {
			System.out.println("help .....");
		}
		return null;
	}

	public String inputln(String msg) {
		return input(msg + System.lineSeparator(), true);
	}

	public String input(String msg, boolean tip) {
		System.out.print(msg);
		if (tip) {
			System.out.print("chatroom>");
		}
		return input.nextLine();
	}

	public String input() {
		return input("", true);
	}

	public void print(String msg) {
		System.out.print(msg);
	}

	public void println(String msg) {
		System.out.println(msg);
	}

}

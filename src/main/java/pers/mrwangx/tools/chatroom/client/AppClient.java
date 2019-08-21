package pers.mrwangx.tools.chatroom.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import pers.mrwangx.tools.chatroom.App;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static pers.mrwangx.tools.chatroom.App.newMessage;
import static pers.mrwangx.tools.chatroom.client.ClientOpt.*;
import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月15 9:56
 **/
public class AppClient {

	public static void main(String[] args) throws IOException {
		new AppClient().run();
	}

	private Scanner input;
	private List<String> history;
	private SimpleChatClient client;

	public AppClient() {
		input = new Scanner(System.in);
		history = new ArrayList<>();
		client = new SimpleChatClient(App.heartBeatInterval);
	}

	public boolean connect(String host, int port) {
		return client.connect(host, port);
	}

	public void run() {
		while (true) {
			String line = input();
			if (!client.checkInput(line)) {
				println("操作命令不正确");
			} else {
				ClientOpt clientOpt = client.getClientOpt(line);
				if (clientOpt != null) {
					switch (clientOpt.getType()) {
					case CLIENT_OPT_CONNECT:
						if (client.isStoped()) {
							println(clientOpt.getDescription());
							String[] v = clientOpt.getValues();
							client.connect(v[0], Integer.parseInt(v[1]));
						} else {
							println("服务器已经连接");
						}
						break;
					case CLIENT_OPT_RECONNECT:
						if (client.isStoped()) {
							println(clientOpt.getDescription());
							client.reConnect();
						} else {
							println("服务器已经连接");
						}
						break;
					case CLIENT_OPT_STOP:
						if (client.isStoped()) {
							println("您还未连接服务器");
						} else {
							println(clientOpt.getDescription());
							client.stop();
						}
						break;
					default:
						if (client.isStoped()) {
							println("请先连接服务器");
						} else {
							println(clientOpt.getDescription());
							if (clientOpt.getMessage() != null) {
								client.sendMessage(clientOpt.getMessage());
							}

						}
						break;
					}
				}
			}
		}
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

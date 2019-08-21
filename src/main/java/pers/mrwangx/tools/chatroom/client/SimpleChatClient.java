package pers.mrwangx.tools.chatroom.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import pers.mrwangx.tools.chatroom.framework.client.ChatClient;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static pers.mrwangx.tools.chatroom.App.newMessage;
import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;
import static pers.mrwangx.tools.chatroom.framework.util.StringUtil.*;
import static pers.mrwangx.tools.chatroom.client.ClientOpt.*;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月15 9:29
 **/
public class SimpleChatClient extends ChatClient<Message> {

	public static String reg;

	static {
		reg = String.format("^(%s [0-9]+::.+)|(%s .+)|(%s .+)|(%s .+ [0-9]+)|(%s)|(%s)$", SEND_MESSAGE, COMMAND, UP_NAME, CONNECT, STOP, RECONNECT);
	}

	private Integer id;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SimpleChatClient(long heartBeatInterval) {
		super(heartBeatInterval);
	}

	public SimpleChatClient(long heartBeatInterval, int MSG_SIZE) {
		super(heartBeatInterval, MSG_SIZE);
	}

	@Override
	public Message parseToMessage(byte[] data) {
		Message msg = null;
		try {
			String strMsg = new String(data, "UTF-8");
			msg = JSON.parseObject(strMsg).toJavaObject(Message.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public byte[] parseToByteData(Message msg) {
		return JSON.toJSONString(msg).getBytes();
	}

	@Override
	public void onReceiveMessage(Message msg) {
		if (msg.getType() == Message.ALLOCATE_ID) {
			this.id = Integer.parseInt(msg.getContent());
		} else {
			System.out.println(msgUI(msg));
			System.out.print("chatroom>");
		}
	}

	@Override
	public Message heartBeatMsg() {
		return Message.newBuilder().type(Message.HEART_BEAT_PAC).build();
	}

	public boolean checkInput(String input) {
		return input.matches(reg);
	}

	public ClientOpt getClientOpt(String input) {
		if (!checkInput(input)) {
			return null;
		} else {
			ClientOpt clientOpt = new ClientOpt();
			if (input.startsWith(CONNECT)) { //连接服务器
				String[] strs = input.split(" +");
				String[] values = new String[2];
				values[0] = strs[1];
				values[1] = strs[2];
				clientOpt.setOptKeyword(CONNECT)
								.setType(CLIENT_OPT_CONNECT)
								.setValues(values)
								.setDescription(str("连接服务器[%s:%s]", values[0], values[1]));
			} else if (input.startsWith(STOP)) { //断开连接
				clientOpt.setOptKeyword(STOP)
								.setType(CLIENT_OPT_STOP)
								.setDescription(str("断开与服务器的连接"));
			} else if (input.startsWith(RECONNECT)) { //重新连接
				clientOpt.setOptKeyword(RECONNECT)
								.setType(CLIENT_OPT_RECONNECT)
								.setDescription(str("重新连接服务器[%s:%s]", address.getHostName(), address.getPort()));
			} else if (input.startsWith(SEND_MESSAGE)) { //发送消息
				String[] strs = input.split("::");
				String[] values = new String[2];
				values[0] = strs[0].split(" +")[1];
				values[1] = strs[1];
				Message message = newMessage(MESSAGE, null, values[1], -1, Integer.parseInt(values[0]), System.currentTimeMillis());
				boolean isBroadCast = message.getToId() == TO_BROADCAST;
				clientOpt.setOptKeyword(SEND_MESSAGE)
								.setType(CLIENT_OPT_SEND_MESSAGE)
								.setValues(values)
								.setMessage(message)
								.setDescription(str("发送%s[%s]给[ID:%s]", isBroadCast ? "广播信息" : "私信", message.getContent(), isBroadCast ? "所有用户" : message.getToId()));
			} else if (input.startsWith(COMMAND)) {  //向服务器发送命令
				String[] strs = input.split(" +");
				String[] values = new String[1];
				values[0] = strs[1];
				Message message = newMessage(CMD, null, values[0], -1, -1, System.currentTimeMillis());
				clientOpt.setOptKeyword(COMMAND)
								.setType(CLIENT_OPT_SEND_COMMAND)
								.setValues(values)
								.setMessage(message)
								.setDescription(str("发送命令操作[%s]", message.getContent()));
			} else if (input.startsWith(UP_NAME)) { //修改名字
				String[] strs = input.split(" +");
				String[] values = new String[1];
				values[0] = strs[1];
				Message message = newMessage(UPDATE_NAME, null, values[0], -1, -1, System.currentTimeMillis());
				clientOpt.setOptKeyword(UP_NAME)
								.setType(CLIENT_OPT_UPNAME)
								.setValues(values)
								.setMessage(message)
								.setDescription(str("修改名字为[%s]", message.getContent()));
			} else if (input.startsWith("help")) {
				System.out.println("help .....");
			}
			return clientOpt;
		}
	}

	public String msgUI(Message msg) {
		String ui = System.lineSeparator() +
						"%s@%s[%s]\t%s" + System.lineSeparator()
						+ "%s" + System.lineSeparator();
		String fromId = Integer.toString(msg.getFromId());
		String msgSendType = "私信";
		String name = msg.getName();
		String date = simpleDateFormat.format(new Date(msg.getTime()));
		String content = msg.getContent();
		if (msg.getFromId() == FROM_SERVER) {
			fromId = "服务器";
		}
		if (msg.getToId() == TO_BROADCAST) {
			msgSendType = "广播";
		}
		if (msg.getType() == ALLOCATE_ID) {
			content = "您的ID为:" + content;
		}
		return String.format(ui, fromId, name, msgSendType, date, content);
	}

	public Integer getId() {
		return id;
	}
}

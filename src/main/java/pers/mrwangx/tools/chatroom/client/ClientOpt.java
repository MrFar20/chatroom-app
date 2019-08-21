package pers.mrwangx.tools.chatroom.client;

import pers.mrwangx.tools.chatroom.framework.protocol.Message;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月21 14:44
 **/
public class ClientOpt {

	public static final String SEND_MESSAGE = "send";
	public static final String COMMAND = "cmd";
	public static final String UP_NAME = "upname";
	public static final String CONNECT = "connect";
	public static final String RECONNECT = "reconnect";
	public static final String STOP = "stop";


	public static final int CLIENT_OPT_SEND_MESSAGE = 0;
	public static final int CLIENT_OPT_SEND_COMMAND = 1;
	public static final int CLIENT_OPT_UPNAME = 2;
	public static final int CLIENT_OPT_CONNECT = 3;
	public static final int CLIENT_OPT_STOP  = 4;
	public static final int CLIENT_OPT_RECONNECT  = 5;

	private int type;
	private String description;
	private String[] values;
	private String optKeyword;
	private Message message;

	public ClientOpt() {
	}

	public ClientOpt(int type, String description, String[] values, String optKeyword, Message message) {
		this.type = type;
		this.description = description;
		this.values = values;
		this.optKeyword = optKeyword;
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public ClientOpt setType(int type) {
		this.type = type;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ClientOpt setDescription(String description) {
		this.description = description;
		return this;
	}

	public String[] getValues() {
		return values;
	}

	public ClientOpt setValues(String[] values) {
		this.values = values;
		return this;
	}

	public String getOptKeyword() {
		return optKeyword;
	}

	public ClientOpt setOptKeyword(String optKeyword) {
		this.optKeyword = optKeyword;
		return this;
	}

	public Message getMessage() {
		return message;
	}

	public ClientOpt setMessage(Message message) {
		this.message = message;
		return this;
	}
}

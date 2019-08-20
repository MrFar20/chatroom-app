package pers.mrwangx.tools.chatroom.client;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import pers.mrwangx.tools.chatroom.framework.client.ChatClient;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.fusesource.jansi.Ansi.ansi;
import static pers.mrwangx.tools.chatroom.App.newMessage;
import static pers.mrwangx.tools.chatroom.framework.protocol.Message.*;
import static pers.mrwangx.tools.chatroom.framework.util.StringUtil.str;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月15 9:29
 **/
public class SimpleChatClient extends ChatClient<Message> {

	public static final String SEND_MESSAGE = "send";
	public static final String COMMAND = "cmd";
	public static final String UP_NAME = "upname";
	public static String reg;

	static {
		reg = String.format("^(%s [0-9]+::.+)|(%s .+)|(%s .+)$", SEND_MESSAGE, COMMAND, UP_NAME);
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

    public Message parseMessageFromInput(String input) {
        if (input.startsWith(SEND_MESSAGE)) {
            String[] strs = input.split("::");
            return newMessage(MESSAGE, null, strs[1], -1, Integer.parseInt(strs[0].split(" ")[1]), System.currentTimeMillis());
        } else if (input.startsWith(COMMAND)) {
            String[] strs = input.split(" ");
            return newMessage(CMD, null, strs[1], -1, -1, System.currentTimeMillis());
        } else if (input.startsWith(UP_NAME)) {
            String[] strs = input.split(" ");
            return newMessage(UPDATE_NAME, null, strs[1], -1, -1, System.currentTimeMillis());
        } else if (input.startsWith("help")) {
            System.out.println("help .....");
        }
        return null;
    }



	public String msgUI(Message msg) {
		String ui = System.lineSeparator() +
				"%s@%s[%s]\t%s" + System.lineSeparator()
				+ "%s" + System.lineSeparator();
		Integer id = msg.getFromId();
		String name = msg.getName();
		String date = simpleDateFormat.format(new Date(msg.getTime()));
		String content = msg.getContent();
		return String.format(ui, msg.getFromId() == Message.FROM_SERVER ? "服务器" : id, name, msg.getToId() == Message.TO_BROADCAST ? "广播" : "私信", date, content);
	}

    public Integer getId() {
        return id;
    }
}

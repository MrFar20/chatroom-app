package pers.mrwangx.tools.chatroom.fx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import pers.mrwangx.tools.chatroom.App;
import pers.mrwangx.tools.chatroom.client.ClientOpt;
import pers.mrwangx.tools.chatroom.client.SimpleChatClient;
import pers.mrwangx.tools.chatroom.framework.client.ChatClientLogger;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

import static pers.mrwangx.tools.chatroom.client.ClientOpt.*;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月20 16:28
 **/
public class MainController implements Initializable {

	@FXML
	private TextField input;
	@FXML
	private TextArea feedbackBoard;
	@FXML
	private TextArea messageBoard;
	@FXML
	private TextArea logBoard;

	private String fip = "> ";
	private FxChatClient client;
	private String lastword;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClientLogger.LOGGER.addHandler(new ConsoleHandler() {

			@Override
			public void publish(LogRecord record) {
				String msg = getFormatter().format(record);
				println(logBoard, msg);
			}
		});
		client = new FxChatClient(App.heartBeatInterval);
		input.setOnKeyPressed(event -> {
			handleInput(event);
		});
	}

	public void handleInput(KeyEvent event) {
		switch (event.getCode()) {
		case ENTER:
			String line = input.getText();
			lastword = line;
			input.setText("");
			println(feedbackBoard, fip + line);
			if (!client.checkInput(line)) {
				println(feedbackBoard, "命令输入不正确");
			} else {
				ClientOpt clientOpt = client.getClientOpt(line);
				if (clientOpt != null) {
					switch (clientOpt.getType()) {
					case CLIENT_OPT_CONNECT:
						if (client.isStoped()) {
							println(feedbackBoard, clientOpt.getDescription());
							String[] v = clientOpt.getValues();
							Task<Boolean> task = new Task<Boolean>() {

								@Override
								protected Boolean call() throws Exception {
									return client.connect(v[0], Integer.parseInt(v[1]));
								}
							};
							new Thread(task).start();
						} else {
							println(feedbackBoard, "服务器已经连接");
						}
						break;
					case CLIENT_OPT_RECONNECT:
						if (client.isStoped()) {
							println(feedbackBoard, clientOpt.getDescription());
							Task<Boolean> task = new Task<Boolean>() {

								@Override
								protected Boolean call() throws Exception {
									return client.reConnect();
								}
							};
							new Thread(task).start();
						} else {
							println(feedbackBoard, "服务器已经连接");
						}
						break;
					case CLIENT_OPT_STOP:
						if (client.isStoped()) {
							println(feedbackBoard, "您还未连接服务器");
						} else {
							println(feedbackBoard, clientOpt.getDescription());
							client.stop();
						}
						break;
					default:
						if (client.isStoped()) {
							println(feedbackBoard, "请先连接服务器");
						} else {
							println(feedbackBoard, clientOpt.getDescription());
							Platform.runLater(() -> {
								if (clientOpt.getMessage() != null) {
									Task task = new Task() {

										@Override
										protected Object call() throws Exception {
											client.sendMessage(clientOpt.getMessage());
											return null;
										}
									};
									new Thread(task).start();
								}
							});
						}
						break;
					}
				}
			}
		}
	}

	public void println(TextArea textArea, String str) {
		basePrint(textArea, str, true);
	}

	public void print(TextArea textArea, String str) {
		basePrint(textArea, str, false);
	}

	public void basePrint(TextArea textArea, String str, boolean nextLine) {
		str = nextLine ? str + System.lineSeparator() : str;
		textArea.appendText(str);
	}

	private class FxChatClient extends SimpleChatClient {

		public FxChatClient(long heartBeatInterval) {
			super(heartBeatInterval);
		}

		@Override
		public void onReceiveMessage(Message msg) {
			Platform.runLater(() -> {
				super.onReceiveMessage(msg);
				print(messageBoard, msgUI(msg));
			});
		}
	}
}

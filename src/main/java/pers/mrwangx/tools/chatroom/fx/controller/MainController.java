package pers.mrwangx.tools.chatroom.fx.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import pers.mrwangx.tools.chatroom.App;
import pers.mrwangx.tools.chatroom.client.SimpleChatClient;
import pers.mrwangx.tools.chatroom.framework.protocol.Message;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月20 16:28
 **/
public class MainController implements Initializable {

	@FXML
	private TextField input;
	@FXML
	private TextArea show;
	@FXML
	private TextArea messageBoard;

	private String fip = ">";
	private FxChatClient client;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		show.setEditable(false);
		client = new FxChatClient(App.heartBeatInterval);
		client.connect("localhost", 8066);
		input.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				String line = input.getText();
				input.setText("");
				show.appendText(fip + line + System.lineSeparator());
				if (!client.checkInput(line)) {
					show.appendText("命令输入不正确" + System.lineSeparator());
				} else {
					Message msg = client.parseMessageFromInput(line);
					if (line != null) {
						Platform.runLater(() -> {
							client.sendMessage(msg);
						});
					}
				}
			}
		});
	}

	private class FxChatClient extends SimpleChatClient {

		public FxChatClient(long heartBeatInterval) {
			super(heartBeatInterval);
		}


		@Override
		public void onReceiveMessage(Message msg) {
			Platform.runLater(() -> {
				super.onReceiveMessage(msg);
				messageBoard.appendText(msgUI(msg));
			});
		}
	}



}

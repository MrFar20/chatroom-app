package pers.mrwangx.tools.chatroom.fx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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

	private String text = "chatroom>";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		show.setEditable(false);
		input.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				show.appendText(input.getText());
				input.setText("");
			}
		});
	}

}

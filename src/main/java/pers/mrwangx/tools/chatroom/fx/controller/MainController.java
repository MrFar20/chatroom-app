package pers.mrwangx.tools.chatroom.fx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月20 16:28
 **/
public class MainController implements Initializable {

	@FXML
	private TextArea input;
	@FXML
	private TextArea show;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		input.setOnKeyPressed(event -> {

		});
	}





}

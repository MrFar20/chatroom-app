package pers.mrwangx.tools.chatroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pers.mrwangx.tools.chatroom.fx.controller.MainController;

/**
 * @description:
 * @author: 王昊鑫
 * @create: 2019年08月20 16:15
 **/
public class APPWithGUI extends Application {

	private MainController controller;
	private AnchorPane root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		controller = new MainController();

		FXMLLoader mainLoader = new FXMLLoader();
		mainLoader.setLocation(this.getClass().getResource("/fxml/main.fxml"));
		mainLoader.setBuilderFactory(new JavaFXBuilderFactory());
		mainLoader.setController(controller);
		mainLoader.load();

		Parent parent = mainLoader.getRoot();

		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}

}

package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		ApplicationController appController = ApplicationController.getInstance();
		
		BorderPane root = new BorderPane();
		
		root.setLeft(appController.getNavigationBar());
		root.setCenter(appController.getContentContainer());
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("‚§µ√æËÕ‚§µ√·¡Ë‚ª√·°√¡‰Õ —  ‡µÁ¡ 15 ‰¥È 50 ");
		//primaryStage.setMaximized(true);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}

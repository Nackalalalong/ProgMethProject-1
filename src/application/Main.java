package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		ApplicationController appController = ApplicationController.getInstance();
		
		BorderPane root = new BorderPane();
		
		root.setLeft(appController.getNavigationBar());
		root.setCenter(appController.getContentContainer());
		
		Scene scene = new Scene(root);
		
		//Font.loadFont(getClass().getResource("ekkamai-standard-Light.ttf").toExternalForm(), 10);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		
		primaryStage.setScene(scene);
		primaryStage.setTitle("‚§µ√æËÕ‚§µ√·¡Ë‚ª√·°√¡‰Õ —  ‡µÁ¡ 15 ‰¥È 50 ");
		//primaryStage.setMaximized(true);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}

package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		ApplicationController appController = ApplicationController.getInstance();
		
		BorderPane root = new BorderPane();
		
		root.setLeft(appController.getNavigationBar());
		root.setCenter(appController.getContentContainer());
		
		Scene scene = new Scene(root);
		
		//Font.loadFont(getClass().getResource("ekkamai-standard-Light.ttf").toExternalForm(), 10);
		Font.loadFont(ClassLoader.getSystemResource("fonts/supermarket.ttf").toExternalForm(), 10);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		
		primaryStage.setScene(scene);
		primaryStage.setTitle("โปรแกรมคลังสินค้า");
		//primaryStage.setMaximized(true);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		       @Override
		       public void handle(WindowEvent e) {
		          Platform.exit();
		          System.exit(0);
		       }
		    });
		primaryStage.show();
	}


	public static void main(String[] args) {
		//BillPDF a = new BillPDF();
		//a.readPDF();
		//BillPDF.readPDF("C:/Users/Nack/Desktop/test");
		launch(args);
		
	}
}

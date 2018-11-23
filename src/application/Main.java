package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		VBox root = new VBox();
		//CustomButton cs = new CustomButton();
		//cs.setText("test ");
		//cs.setImage("icon/warehouse.png");
		ImageView imageView = new ImageView(new Image(ClassLoader.getSystemResource("icon/warehouse.png").toString()));
		Button cs = new Button("test", imageView);
		cs.setContentDisplay(ContentDisplay.BOTTOM);
		((ImageView)cs.getGraphic()).setFitHeight(70);
		((ImageView)cs.getGraphic()).setFitWidth(70);
		Font font = new Font(20);
		cs.setFont(font);
		cs.setPadding(new Insets(10));
		cs.setPrefWidth(120);
		cs.setPrefHeight(120);
		root.getChildren().add(cs);
		root.setPrefHeight(600);
		
		cs.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Button Clicked");
			}
			
		});
		
		root.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Container Clicked");
			}
			
		});
		
		root.setPadding(new Insets(10));
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}

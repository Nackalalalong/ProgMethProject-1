package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		HBox root = new HBox();
		
		NavigationButton warehouseBtn = new NavigationButton("Warehouse", "icons/warehouse.png");
		NavigationButton itemInBtn = new NavigationButton("In", "icons/item_in.png");
		NavigationButton itemOutBtn = new NavigationButton("Out", "icons/item_out.png");
		NavigationButton statBtn = new NavigationButton("Statistic", "icons/graph.png");
		NavigationButton customerBtn = new NavigationButton("Customer", "icons/feedback.png");
		
		ArrayList<NavigationButton> btns = new ArrayList<NavigationButton>() {{
			add(warehouseBtn);
			add(itemInBtn);
			add(itemOutBtn);
			add(statBtn);
			add(customerBtn);
		}};
		
		NavigationBar navBar = new NavigationBar(btns);
		ContentContainer contentCont = new ContentContainer();
		
		root.getChildren().addAll(navBar, contentCont);
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		//primaryStage.setMaximized(true);
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}

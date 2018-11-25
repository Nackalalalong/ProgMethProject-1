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
		BorderPane root = new BorderPane();
		
		NavigationButton warehouseBtn = new NavigationButton("Warehouse", "icons/warehouse.png");
		NavigationButton itemInBtn = new NavigationButton("In", "icons/item_in.png");
		NavigationButton itemOutBtn = new NavigationButton("Out", "icons/item_out.png");
		NavigationButton statBtn = new NavigationButton("Statistic", "icons/graph.png");
		NavigationButton customerBtn = new NavigationButton("Customer", "icons/feedback.png");
		NavigationButton listBtn = new NavigationButton("Customer", "icons/list.png");
		NavigationButton helpBtn = new NavigationButton("Customer", "icons/question.png");
		
		NavigationButtonPane warehouseBtnPane = new NavigationButtonPane(warehouseBtn);
		NavigationButtonPane itemInBtnPane = new NavigationButtonPane(itemInBtn);
		NavigationButtonPane itemOutBtnPane = new NavigationButtonPane(itemOutBtn);
		NavigationButtonPane statBtnPane = new NavigationButtonPane(statBtn);
		NavigationButtonPane customerBtnPane = new NavigationButtonPane(customerBtn);
		NavigationButtonPane listBtnPane = new NavigationButtonPane(listBtn);
		NavigationButtonPane helpBtnPane = new NavigationButtonPane(helpBtn);
		
		ArrayList<NavigationButtonPane> navBtnPanes = new ArrayList<NavigationButtonPane>() {{
			add(warehouseBtnPane);
			add(itemInBtnPane);
			add(itemOutBtnPane);
			add(statBtnPane);
			add(customerBtnPane);
			add(listBtnPane);
			add(helpBtnPane);
		}};
		
		NavigationBar navBar = new NavigationBar(navBtnPanes);
		ContentContainer contentCont = new ContentContainer();
		
		root.setLeft(navBar);
		root.setCenter(contentCont);
		
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

package application;

import java.io.IOException;
import java.util.ArrayList;

import FXMLController.ItemOutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class ApplicationController {
	
	private static ApplicationController instance;

	private NavigationBar navBar;
	private ContentContainer contentCont;
	
	private NavigationButtonPane warehouseBtnPane;
	private NavigationButtonPane itemInBtnPane;
	private NavigationButtonPane itemOutBtnPane;
	private NavigationButtonPane statBtnPane;
	private NavigationButtonPane customerBtnPane;
	private NavigationButtonPane listBtnPane;
	private NavigationButtonPane helpBtnPane;
	
	private Pane warehousePane;
	private Pane itemInPane;
	private Pane itemOutPane;
		
	private ApplicationController() {
		NavigationButton warehouseBtn = new NavigationButton("คลัง", "icons/warehouse.png");
		NavigationButton itemInBtn = new NavigationButton("เข้า", "icons/item_in.png");
		NavigationButton itemOutBtn = new NavigationButton("ออก", "icons/item_out.png");
		NavigationButton statBtn = new NavigationButton("สถิติ", "icons/graph.png");
		NavigationButton customerBtn = new NavigationButton("ลูกค้า", "icons/feedback.png");
		NavigationButton listBtn = new NavigationButton("รายการบิล", "icons/list.png");
		NavigationButton helpBtn = new NavigationButton("ช่วยเหลือ", "icons/question.png");
		
		warehouseBtnPane = new NavigationButtonPane(warehouseBtn);
		itemInBtnPane = new NavigationButtonPane(itemInBtn);
		itemOutBtnPane = new NavigationButtonPane(itemOutBtn);
		statBtnPane = new NavigationButtonPane(statBtn);
		customerBtnPane = new NavigationButtonPane(customerBtn);
		listBtnPane = new NavigationButtonPane(listBtn);
		helpBtnPane = new NavigationButtonPane(helpBtn);
		
		ArrayList<NavigationButtonPane> navBtnPanes = new ArrayList<NavigationButtonPane>() {{
			add(warehouseBtnPane);
			add(itemInBtnPane);
			add(itemOutBtnPane);
			add(statBtnPane);
			add(customerBtnPane);
			add(listBtnPane);
			add(helpBtnPane);
		}};
		
		navBar = new NavigationBar(navBtnPanes);
		
		ArrayList<Pane> contentPanes = new ArrayList<Pane>();
		try {
			warehousePane = FXMLLoader.load(ClassLoader.getSystemResource("warehouse.fxml"));
			itemInPane = FXMLLoader.load(ClassLoader.getSystemResource("itemIn.fxml"));
			itemOutPane = FXMLLoader.load(ClassLoader.getSystemResource("itemOut.fxml"));
						
			contentPanes.add(warehousePane);
			contentPanes.add(itemInPane);
			contentPanes.add(itemOutPane);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		contentCont = new ContentContainer(contentPanes);

		
		InitializeNavigationButton();
		warehouseBtn.fire();
	}
	
	public void InitializeNavigationButton() {
		warehouseBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("warehouse");
				warehousePane.toFront();
			}
		});
		
		itemInBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler(){ 
			
			@Override
			public void showContentPane() {
				System.out.println("item in");
				itemInPane.toFront();
			}
		});
		
		itemOutBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler(){
			
			@Override
			public void showContentPane() {
				System.out.println("item out");
				itemOutPane.toFront();
			}
		});
		
		statBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("statistics");
			}
		});
		
		customerBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("customers");
			}
		});
		
		listBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("bills");
			}
		});
		
		helpBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("help");
			}
		});
	}

	public static ApplicationController getInstance() {
		if ( instance == null ) {
			instance = new ApplicationController();
		}
		return instance;
	}

	public NavigationBar getNavigationBar() {
		return navBar;
	}

	public ContentContainer getContentContainer() {
		return contentCont;
	}
	
	
}

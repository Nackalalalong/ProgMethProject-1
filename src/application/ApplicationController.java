package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;

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
	
	private ApplicationController() {
		NavigationButton warehouseBtn = new NavigationButton("Warehouse", "icons/warehouse.png");
		NavigationButton itemInBtn = new NavigationButton("In", "icons/item_in.png");
		NavigationButton itemOutBtn = new NavigationButton("Out", "icons/item_out.png");
		NavigationButton statBtn = new NavigationButton("Statistic", "icons/graph.png");
		NavigationButton customerBtn = new NavigationButton("Customer", "icons/feedback.png");
		NavigationButton listBtn = new NavigationButton("bills", "icons/list.png");
		NavigationButton helpBtn = new NavigationButton("help", "icons/question.png");
		
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
		contentCont = new ContentContainer(null);
		
		InitializeNavigationButton();
		warehouseBtn.fire();
	}
	
	public void InitializeNavigationButton() {
		warehouseBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler() {
			
			@Override
			public void showContentPane() {
				System.out.println("warehouse");
			}
		});
		
		itemInBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler(){ 
			
			@Override
			public void showContentPane() {
				System.out.println("item in");
			}
		});
		
		itemOutBtnPane.getNavigationButton().setOnAction(new NavigationButtonEventHandler(){
			
			@Override
			public void showContentPane() {
				System.out.println("item out");
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

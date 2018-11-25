package application;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NavigationBar extends VBox {
	
	public static final String NAVIGATION_BAR_BACKGROUND_COLOR = "cbdadb";
	public static final int NAVIGATION_BAR_PADDING_TOP = 5;
	public static final int NAVIGATION_BAR_PADDING_BOTTOM = 5;
	public static final int NAVIGATION_BAR_PADDING_RIGHT = 0;
	public static final int NAVIGATION_BAR_PADDING_LEFT = 0;
	public static final Background DEFAULT_NAVIGATION_BACKGROUND = new  Background(new BackgroundFill(Color.web("#" + NAVIGATION_BAR_BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY));

	
	private ArrayList<NavigationButtonPane> navBtnPanes;

	public NavigationBar(ArrayList<NavigationButtonPane> navBtnPanes) {
		super();
		
		//this.setPrefWidth( 2 * NAVIGATION_BAR_PADDING + NavigationButton.NAVIGATION_BUTTON_WIDTH);
		//this.setPrefHeight(720);
		this.setPadding(new Insets(NAVIGATION_BAR_PADDING_TOP, NAVIGATION_BAR_PADDING_RIGHT, NAVIGATION_BAR_PADDING_BOTTOM, NAVIGATION_BAR_PADDING_LEFT));
		//this.setSpacing(NAVIGATION_BAR_SPACING);
		this.setBackground(DEFAULT_NAVIGATION_BACKGROUND);
		
		for( NavigationButtonPane navBtnPane : navBtnPanes) {
			this.getChildren().add(navBtnPane);
			navBtnPane.getNavigationButton().setNavigationBar(this);
		}
		
		this.navBtnPanes = navBtnPanes;
	
	}

	public ArrayList<NavigationButtonPane> getNavigationButtonPanes() {
		return navBtnPanes;
	}
	
	
	
}

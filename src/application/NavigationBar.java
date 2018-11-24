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
	public static final int NAVIGATION_BAR_PADDING = 10;
	public static final int NAVIGATION_BAR_SPACING = 10;
	
	private ArrayList<NavigationButton> buttons;

	public NavigationBar(ArrayList<NavigationButton> buttons) {
		super();
		
		this.setPrefWidth( 2 * NAVIGATION_BAR_PADDING + NavigationButton.NAVIGATION_BUTTON_WIDTH);
		this.setPrefHeight(720);
		this.setPadding(new Insets(NAVIGATION_BAR_PADDING));
		this.setSpacing(NAVIGATION_BAR_SPACING);
		this.setBackground(new Background(new BackgroundFill(Color.web("#" + NAVIGATION_BAR_BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
		
		for( NavigationButton btn : buttons) {
			this.getChildren().add(btn);
		}
		
		this.buttons = buttons;
	
	}
	
	public void setBackgroundAtButtonIndex(int index) {
		//set background effect to button at index index.
	}
	
}

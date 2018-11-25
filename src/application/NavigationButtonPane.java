package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class NavigationButtonPane extends StackPane {
	
	public static final int NAVIGATION_BUTTON_PANE_PADDING_TOP = 5;
	public static final int NAVIGATION_BUTTON_PANE_PADDING_RIGHT = 10;
	public static final int NAVIGATION_BUTTON_PANE_PADDING_BOTTOM = 5;
	public static final int NAVIGATION_BUTTON_PANE_PADDING_LEFT = 10;
	public static final Background NAVIGATION_BUTTON_PANE_HIGHLIGHT_BACKGROUND = new  Background(new BackgroundFill(Color.web("#" + ContentContainer.APPLICATION_BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY));

	
	private NavigationButton navBtn;

	public NavigationButtonPane(NavigationButton button) {
		super();
		
		navBtn = button;
		navBtn.setNavigationButtonPane(this);
		
		this.setPadding(new Insets(NAVIGATION_BUTTON_PANE_PADDING_TOP, NAVIGATION_BUTTON_PANE_PADDING_RIGHT, NAVIGATION_BUTTON_PANE_PADDING_BOTTOM, NAVIGATION_BUTTON_PANE_PADDING_LEFT));
		
		this.getChildren().add(navBtn);
		
		unHighlightBackground();
	}
	
	public void highlightBackground() {
		this.setBackground(NAVIGATION_BUTTON_PANE_HIGHLIGHT_BACKGROUND);
	}
	
	public void unHighlightBackground() {
		this.setBackground(NavigationBar.DEFAULT_NAVIGATION_BACKGROUND);
	}
	
	public NavigationButton getNavigationButton() {
		return this.navBtn;
	}
}

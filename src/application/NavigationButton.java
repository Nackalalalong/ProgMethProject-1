package application;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavigationButton extends ToggleButton implements Discolorable{
	
	//public static final Background HIGHLIGHT_NAVIGATION_BUTTON_BACKGROUND = new  Background(new BackgroundFill(Color.web("#fdae84"), CornerRadii.EMPTY, Insets.EMPTY));
	//public static final Background UNHIGHLIGHT_NAVIGATION_BUTTON_BACKGROUND = new  Background(new BackgroundFill(Color.web("#dae9e4"), CornerRadii.EMPTY, Insets.EMPTY));

	
	public static final int NAVIGATION_BUTTON_WIDTH = 90;
	public static final int NAVIGATION_BUTTON_HEIGHT = 90;
	public static final int NAVIIGATION_BUTTON_IMAGE_WIDTH = 50;
	public static final int NAVIIGATION_BUTTON_IMAGE_HEIGHT = 50;
	public static final int NAVIGATION_BUTTON_PADDING = 10;
	
	private ImageView image;
	private NavigationButtonPane navigationButtonPane;
	private NavigationBar navigationBar;
	
	public NavigationButton(String text, String imagePath) {
		super(text);
		
		String path = ClassLoader.getSystemResource(imagePath).toString();
		image = new ImageView(new Image(path));
		image.setPreserveRatio(true);
		image.setSmooth(true);
		this.setGraphic(image);
		setImageWidthHeight(NAVIIGATION_BUTTON_IMAGE_WIDTH, NAVIIGATION_BUTTON_IMAGE_HEIGHT);
		
		this.setContentDisplay(ContentDisplay.TOP);
		
		this.setPrefWidth(NAVIGATION_BUTTON_WIDTH);
		this.setPrefHeight(NAVIGATION_BUTTON_HEIGHT);
		
		this.setPadding(new Insets(NAVIGATION_BUTTON_PADDING));
		
		setCssStyle("navigation-button-beautiful");
		
	}
	
	/*public void highlightBackground() {
		this.setBackground(HIGHLIGHT_NAVIGATION_BUTTON_BACKGROUND);
	}
	
	public void unHighlightBackground() {
		this.setBackground(UNHIGHLIGHT_NAVIGATION_BUTTON_BACKGROUND);
	}*/
	
	public NavigationButtonPane getNavigationButtonPane() {
		return navigationButtonPane;
	}



	public void setNavigationButtonPane(NavigationButtonPane holder) {
		this.navigationButtonPane = holder;
	}

	

	public NavigationBar getNavigationBar() {
		return navigationBar;
	}



	public void setNavigationBar(NavigationBar navigationBar) {
		this.navigationBar = navigationBar;
	}



	public void setImageWidthHeight(int width, int height) {
		this.image.setFitWidth(width);
		this.image.setFitHeight(height);
	}

	@Override
	public void setCssStyle(String cssClass) {
		// TODO Auto-generated method stub
		getStyleClass().add(cssClass);
	}

}

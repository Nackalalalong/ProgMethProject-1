package application;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavigationButton extends Button implements Discolorable{
	
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
		
	}
	
	
	
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
		
	}

}

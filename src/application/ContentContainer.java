package application;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ContentContainer extends Pane {
	
	public static final String APPLICATION_BACKGROUND_COLOR = "f4e7e7";
	
	public ContentContainer() {
		super();
		
		this.setPrefWidth(1200);
		//this.setPrefHeight(720);
		
		this.setBackground(new Background(new BackgroundFill(Color.web("#" + ContentContainer.APPLICATION_BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
}

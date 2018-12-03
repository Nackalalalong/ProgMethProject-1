package application;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ContentContainer extends StackPane {
	
	public static final String APPLICATION_BACKGROUND_COLOR = "f4e7e7";
	public static final int CONTENT_CONTAINER_PADDING = 10;
	public static final Background CONTENT_BACKGROUND = new Background(new BackgroundFill(Color.web("#" + ContentContainer.APPLICATION_BACKGROUND_COLOR), CornerRadii.EMPTY, Insets.EMPTY));
	
	public ContentContainer(ArrayList<Pane> panes) {
		super();
		
		this.setPrefWidth(1200);
		//this.setPrefHeight(720);
		
		this.setPadding(new Insets(CONTENT_CONTAINER_PADDING));
		this.setBackground(CONTENT_BACKGROUND);
		
		for( Pane pane : panes ) {
			this.getChildren().add(pane);
		}
	}
	
}

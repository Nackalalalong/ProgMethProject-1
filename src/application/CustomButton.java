package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CustomButton extends Button {
	
	private ImageView buttonIcon;
	private Label buttonLabel;
	private VBox vbox;
	
	public CustomButton() {
		super();
		setDefaultSize();
		createDefaultSkin();
	}
	
	public CustomButton(String str) {
		super(str);
		setDefaultSize();
	}
	
	private void setDefaultSize() {
		this.setPrefHeight(CustomButtonSkin.NAVIGATION_BUTTON_HEIGHT);
		this.setPrefWidth(CustomButtonSkin.NAVIGATION_BUTTON_WIDTH);
			
	}
	
	public void setImage(String path) {
		String imagePath = ClassLoader.getSystemResource(path).toString();
		buttonIcon.setImage(new Image(imagePath));
		buttonIcon.setFitHeight(200);
	}

	@Override
	protected Skin<?> createDefaultSkin(){
		Skin<?> result = new CustomButtonSkin(this);
		
		buttonIcon = ((CustomButtonSkin)result).getImageView();
		buttonLabel = ((CustomButtonSkin)result).getLabel();
		vbox = ((CustomButtonSkin)result).getVbox();
		
		return result;
	}
}

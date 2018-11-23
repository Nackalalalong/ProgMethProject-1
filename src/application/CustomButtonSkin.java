package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CustomButtonSkin extends ButtonSkin {
	
	public static final int NAVIGATION_BUTTON_IMAGE_WIDTH = 100;
	public static final int NAVIGATION_BUTTON_IMAGE_HEIGHT = 100;
	public static final int NAVIGATION_BUTTON_WIDTH = 200;
	public static final int NAVIGATION_BUTTON_HEIGHT = 200;
	
	private VBox vbox;
	private ImageView image;
	private Label label;

	public CustomButtonSkin(Button button) {
		super(button);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void updateChildren() {
		super.updateChildren();
		
		if ( vbox == null ) {
			vbox = new VBox();
			
			image = new ImageView();
			label = new Label();
			
			vbox.getChildren().addAll(label, image);
		}
		getChildren().add(vbox);
	}
	
	public ImageView getImageView() {
		return image;
	}

	public VBox getVbox() {
		return vbox;
	}

	public Label getLabel() {
		return label;
	}
	
	

}

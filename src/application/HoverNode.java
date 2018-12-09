package application;



import javax.swing.text.DefaultEditorKit.DefaultKeyTypedAction;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HoverNode extends StackPane {  	//ชี้จุดที่กราฟแล้วแสดงค่า

	private Label dataLabel;
	private double defaultX;
	private double defaultY;
	
	public HoverNode (String value) {
		dataLabel = new Label(value);
		setDafaultSize();
		initializeListener();
	}
	
	private void setDafaultSize() {
		setWidth(12);
		setHeight(12);
	}
	
	private void setBiggerSize() {
		setWidth(80);
		setHeight(40);
	}

	private void initializeListener() {
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				getChildren().setAll(dataLabel);
				//System.out.println("hover");
				setCursor(Cursor.NONE);
				setBiggerSize();
				defaultX = getLayoutX();
				defaultY = getLayoutY();
				setLayoutX(getLayoutX()-30);
				setLayoutY(getLayoutY()-30);
				toFront();
			}
			
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				setDafaultSize();
				setLayoutX(defaultX);
				setLayoutY(defaultY);
				getChildren().clear();
			}
			
		});
	}
	
	
	
}

package application;



import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HoverNode extends StackPane {  	//ชี้จุดที่กราฟแล้วแสดงค่า

	private Label dataLabel;
	
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
		setHeight(20);
	}

	private void initializeListener() {
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				getChildren().setAll(dataLabel);
				setCursor(Cursor.NONE);
				setBiggerSize();
				toFront();
			}
			
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				setDafaultSize();
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
			
		});
	}
	
	
	
}

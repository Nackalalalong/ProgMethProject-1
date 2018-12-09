package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MaxSellException extends Exception {
	
	public MaxSellException() {
		showWaringDialog();
	}
	
	public MaxSellException(String message) {
		super(message);
		showWaringDialog();
	}
	
	public MaxSellException(Throwable e) {
		super(e);
		showWaringDialog();
	}

	private void showWaringDialog() {
		Alert warning = new Alert(Alert.AlertType.WARNING, "รายการขายของคุณถึงขีดจำกัดการทำรายการแล้ว กรุณาทำรายการขายปัจจุบันให้เสร็จสิ้นก่อนทำรายการใหม่", ButtonType.OK);
		warning.setTitle("ระวัง");
		warning.setHeaderText("รายการขายถึงขีดจำกัด");
		warning.show();
	}
	
}

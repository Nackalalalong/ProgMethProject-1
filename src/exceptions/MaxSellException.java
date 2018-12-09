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
		Alert warning = new Alert(Alert.AlertType.WARNING, "��¡�â�¢ͧ�س�֧�մ�ӡѴ��÷���¡������ ��سҷ���¡�â�»Ѩ�غѹ���������鹡�͹����¡������", ButtonType.OK);
		warning.setTitle("���ѧ");
		warning.setHeaderText("��¡�â�¶֧�մ�ӡѴ");
		warning.show();
	}
	
}

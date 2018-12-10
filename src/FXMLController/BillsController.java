package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import application.BillPDF;
import dataModel.BillModel;
import dataModel.CustomerTableModel;
import dataModel.DataSet;
import factory.ApplicationFactory;
import factory.DatabaseCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class BillsController implements Initializable{

	@FXML
	private TextField billIdTf, billDateTf, customerNameTf;
	
	@FXML
	private Button searchBtn, clearBtn;
	
	@FXML
	private TableView table;
	
	@FXML
	private TableColumn billIdTc, billDateTc, customerNameTc, noteTc;
	
	private Statement billStatement;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initializeTable();
		billStatement = DatabaseCenter.getBillStatement();
	}
	
	public void clearInput() {
		billIdTf.setText("");
		billDateTf.setText("");
		customerNameTf.setText("");
	}

	public void searchData() {
		
		String billId = billIdTf.getText().trim();
		String billDate = billDateTf.getText().trim();
		String customerName = customerNameTf.getText().trim();
		
		String cmd = "SELECT * FROM " + ApplicationFactory.BILL_DATABASE_NAME + " WHERE " +
				ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " LIKE '%" + billId + "%' AND " +
				ApplicationFactory.BILL_DATABASE_BILL_DATE_COLUMN_NAME + " LIKE '%" + billDate + "%' AND " +
				ApplicationFactory.BILL_DATABASE_CUSTOMER_NAME_COLUMN_NAME + " LIKE '%" + customerName + "%'";
		
		try {
			ResultSet res = billStatement.executeQuery(cmd);
			loadDataToTable(res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void loadDataToTable(ResultSet res) throws SQLException {
		ObservableList<BillModel> bills = FXCollections.observableArrayList();
		
		while( res.next() ) {
			String billId = res.getString(ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME);
			String billDate = res.getString(ApplicationFactory.BILL_DATABASE_BILL_DATE_COLUMN_NAME);
			String customerName = res.getString(ApplicationFactory.BILL_DATABASE_CUSTOMER_NAME_COLUMN_NAME);
			String note = res.getString(ApplicationFactory.BILL_DATABASE_NOTE_COLUMN_NAME);
			
			bills.add(new BillModel(billId, billDate, customerName, note));
			
		}
		
		table.setItems(bills);
		
	}
	
	private void initializeTable() {
		
		billIdTc.setCellValueFactory(new PropertyValueFactory<>("billId"));
		billDateTc.setCellValueFactory(new PropertyValueFactory<>("billDate"));
		customerNameTc.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		noteTc.setCellValueFactory(new PropertyValueFactory<>("note"));

		MenuItem menuItem = new MenuItem("แก้ไขหมายเหตุ");
		menuItem.setOnAction((ActionEvent event) -> {
		    BillModel model = (BillModel) table.getSelectionModel().getSelectedItem();
		    showEditNoteDialog(model);
		});

		MenuItem viewBill =  new MenuItem("ดูใบเสร็จ");
		viewBill.setOnAction((ActionEvent event) -> {
			BillModel model = (BillModel) table.getSelectionModel().getSelectedItem();
			String billId = model.getBillId();
			BillPDF.readPDF("./bills/" + billId);
		});
		
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(menuItem);
		menu.getItems().addAll(viewBill);
		table.setContextMenu(menu);
		
	}
	
	private void showEditNoteDialog(BillModel model) {
		// TODO Auto-generated method stub
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("หมายเหตุ");
		dialog.setHeaderText("แก้ไขหมายเหตุ " );
		
		String originalNote = model.getNote();
		
		for( Node node : dialog.getDialogPane().getChildren() ) {
			if ( node instanceof GridPane ) {
				for( Node n : ((GridPane)node).getChildren() ) {
					if ( n instanceof TextField ) {
						((TextField) n).setPrefHeight(CustomerController.NOTE_DIALOG_HEIGHT);
						((TextField) n).setPrefWidth(CustomerController.NOTE_DIALOG_WIDTH);
						((TextField) n).setText(originalNote);
					}
				}
			}
		}
		
		Optional<String> result = dialog.showAndWait();
		if ( result.isPresent() ) {
			String note = result.get();
			String billId = model.getBillId();
			
			try {
				String cmd = "UPDATE " + ApplicationFactory.BILL_DATABASE_NAME + " SET " +
						ApplicationFactory.BILL_DATABASE_NOTE_COLUMN_NAME + " = '" +
						note + "' WHERE " + 
						ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " = '" + billId + "'";
				
				billStatement.executeUpdate(cmd);
				model.setNote(note);
				table.refresh();
				showInfomationDialog("หมายเหตุ", "แก้ไขหมายเหตุสำเร็จ", "");
			}
			catch(SQLException e) {
				showErrorDialog("มีบางอย่างผิดพลาดขณะกำลังแก้ไขฐานข้อมูล", "กรุณาลองใหม่ภายหลัง");
				e.printStackTrace();
			}
		}
	}
	
	public void showErrorDialog(String header, String message) {
		Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		error.setTitle("ข้อผิดพลาด");
		error.setHeaderText(header);
		error.show();
	}
	
	private void showInfomationDialog(String title, String header, String message) {
		Alert info = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
		info.setTitle(title);
		info.setHeaderText(header);
		info.show();
	}
	
}

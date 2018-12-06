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

import application.CustomerTableModel;
import application.DateThai;
import factory.ApplicationFactory;
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

public class CustomerController implements Initializable {
	
	private static final int CUSTOMER_CELL_SIZE = 40;
	private static final int NOTE_DIALOG_WIDTH = 300;
	private static final int NOTE_DIALOG_HEIGHT = 150;
	
	@FXML
	private TextField customerNameTf;
	
	@FXML
	private Button searchBtn;
	
	@FXML
	private TableView table;
	
	@FXML 
	private TableColumn customerNameTc, buyAmountTc, lastestBuyDateTc, totalBuyTc, totalProfitTc, noteTc;
	
	private Statement statement;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			initializeDatabaseConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorDialog("การเชื่อมต่อกับฐานข้อมูลผิดพลาด", "กรุณาลองใหม่ภายหลัง");
		}
		initializeTable();
	}
	
	public void showErrorDialog(String header, String message) {
		Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		error.setTitle("ข้อผิดพลาด");
		error.setHeaderText(header);
		error.show();
	}
	
	private void initializeTable() {		
		table.setFixedCellSize(CUSTOMER_CELL_SIZE);
		
		customerNameTc.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		buyAmountTc.setCellValueFactory(new PropertyValueFactory<>("buyAmount"));
		lastestBuyDateTc.setCellValueFactory(new PropertyValueFactory<>("lastestBuyDate"));
		totalBuyTc.setCellValueFactory(new PropertyValueFactory<>("totalBuy"));
		totalProfitTc.setCellValueFactory(new PropertyValueFactory<>("totalProfit"));
		noteTc.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		MenuItem menuItem = new MenuItem("แก้ไขหมายเหตุ");
		menuItem.setOnAction((ActionEvent event) -> {
		    CustomerTableModel model = (CustomerTableModel) table.getSelectionModel().getSelectedItem();
		    showEditNoteDialog(model);
		});

		ContextMenu menu = new ContextMenu();
		menu.getItems().add(menuItem);
		table.setContextMenu(menu);
		
	}
	
	private void showEditNoteDialog(CustomerTableModel model) {
		// TODO Auto-generated method stub
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("หมายเหตุ");
		dialog.setHeaderText("แก้ไขหมายเหตุ " );
		
		String originalNote = model.getNote();
		
		for( Node node : dialog.getDialogPane().getChildren() ) {
			if ( node instanceof GridPane ) {
				for( Node n : ((GridPane)node).getChildren() ) {
					if ( n instanceof TextField ) {
						((TextField) n).setPrefHeight(NOTE_DIALOG_HEIGHT);
						((TextField) n).setPrefWidth(NOTE_DIALOG_WIDTH);
						((TextField) n).setText(originalNote);
					}
				}
			}
		}
		
		Optional<String> result = dialog.showAndWait();
		if ( result.isPresent() ) {
			String note = result.get();
			String customerName = model.getCustomerName();
			
			try {
				String cmd = "UPDATE " + ApplicationFactory.CUSTOMER_DATABASE_NAME + " SET " +
						ApplicationFactory.CUSTOMER_DATABASE_NOTE_COLUMN_NAME + " = '" +
						note + "' WHERE " + 
						ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " = '" + customerName + "'";
				
				statement.executeUpdate(cmd);
				model.setNote(note);
				table.refresh();
			}
			catch(SQLException e) {
				showErrorDialog("มีบางอย่างผิดพลาดขณะกำลังแก้ไขฐานข้อมูล", "กรุณาลองใหม่ภายหลัง");
				e.printStackTrace();
			}
		}
	}

	private void initializeDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CUSTOMER_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		statement = connection.createStatement();
	}

	public void searchCustomer() throws SQLException {
		String customerName = customerNameTf.getText().trim();
		
		String cmd = "SELECT * FROM " + ApplicationFactory.CUSTOMER_DATABASE_NAME + " WHERE " +
				ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " LIKE '%" + customerName + "%'";
		
		ResultSet res = statement.executeQuery(cmd);
		loadDataToTable(res);
	}
	
	private void loadDataToTable(ResultSet res) throws SQLException {
		
		ObservableList<CustomerTableModel> customerDatas = FXCollections.observableArrayList();
		
		while( res.next() ) {
			String customerName = res.getString(ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME);
			String buyAmount = res.getString(ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME);
			String lastestBuyDate = res.getString(ApplicationFactory.CUSTOMER_DATABASE_LASTEST_BUY_DATE_COLUMN_NAME);
			String totalBuy = res.getString(ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME);
			String totalProfit = res.getString(ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME);
			String note = res.getString(ApplicationFactory.CUSTOMER_DATABASE_NOTE_COLUMN_NAME);
			lastestBuyDate = DateThai.numberDateToThaiDate(lastestBuyDate);
			
			CustomerTableModel model = new CustomerTableModel(customerName, buyAmount, lastestBuyDate, totalBuy, totalProfit, note);
			customerDatas.add(model);
			
		}
		
		table.setItems(customerDatas);
		
	}
}

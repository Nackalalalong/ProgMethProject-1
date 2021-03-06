package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import dataModel.DataSet;
import dataModel.ItemOutDataSet;
import factory.ApplicationFactory;
import factory.BillPDF;
import factory.BillsIdGenerator;
import factory.DatabaseCenter;
import factory.DateThai;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class ItemOutController implements Initializable {
	
	@FXML
	private TextField customerNameTf, billIdTf, billDateTf, noteTf, saveBillDestinationTf, priceTf, discountByBahtTf , discountByPercentTf, discountByPercentBahtTf,
		vatTf, vatBahtTf, netPriceTf, profitTf;
	
	@FXML
	private Button viewBillBtn, selectBillDestinationBtn, okBtn, cancelBtn;
	
	@FXML
	private RadioButton discountByBahtRb, discountByPercentRb;
	
	@FXML
	private TableView table;
	
	@FXML
	private TableColumn imageTc, itemIdTc, snTc, itemNameTc, buyPriceTc, sellPriceTc, sellAmountTc, itemTotalPriceTc;
	
	private ObservableList<ItemOutDataSet> itemOutDataSets;
	private File destinationDirectory;
	
	private Statement customersStatement;
	private Statement statisticsStatement;
	private Statement mainStatement;
	private Statement billStatement;
	
	private DecimalFormat decimalFormatter;
	
	private WarehouseController warehouseController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		itemOutDataSets = FXCollections.observableArrayList();
		destinationDirectory = null;
		
		decimalFormatter = new DecimalFormat("#0.00");
		
		initializeNumericTextField();
		initializeTable();
		initializeFinancialTextFieldBinding();
		
		billDateTf.setText(DateThai.getCurrentThaiDate());
		billIdTf.setText(BillsIdGenerator.getBillId());
		discountByBahtRb.fire();
		
		customersStatement = DatabaseCenter.getCustomerStatement();
		statisticsStatement = DatabaseCenter.getStatisticsStatement();
		mainStatement = DatabaseCenter.getMainStatement();
		billStatement = DatabaseCenter.getBillStatement();
		
	}
	
	public void setWarehouseController(WarehouseController wc) {
		warehouseController = wc;
	}
	
	
	public ObservableList<ItemOutDataSet> getItemOutDataSets() {
		return itemOutDataSets;
	}
	
	private void showErrorDialog(String title, String header, String message) {
		Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		error.setTitle(title);
		error.setHeaderText(header);
		error.show();
	}
	
	private void updateBillsDatabase(String billId, String billDate, String customerName, String note) throws SQLException {
		
		String cmd = "INSERT INTO " + ApplicationFactory.BILL_DATABASE_NAME + " (" +
				ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + ", " +
				ApplicationFactory.BILL_DATABASE_BILL_DATE_COLUMN_NAME + ", " +
				ApplicationFactory.BILL_DATABASE_CUSTOMER_NAME_COLUMN_NAME + ", " +
				ApplicationFactory.BILL_DATABASE_NOTE_COLUMN_NAME + ") VALUES ('" +
				billId + "', '" +
				billDate + "', '" +
				customerName + "', '" +
				note + "')";
		
		billStatement.executeUpdate(cmd);
	}
	
	private void updateStatisticsDatabase(String itemName, String itemId, String itemSn, String sellAmount, String totalSell, String totalProfit, int month, int year) throws SQLException {
		
		String cmd = "INSERT INTO " +ApplicationFactory.STATISTICS_DATABASE_NAME + "(" +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_ID_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_SERIAL_NUMBER_COLUMN_NAME + ", " + 
				ApplicationFactory.STATISTICS_DATABASE_SELL_AMOUNT_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_TOTAL_SELL_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_TOTAL_PROFIT_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + ") VALUES ('" +
				itemName + "', " +
				itemId + ", '" +
				itemSn + "', " +
				sellAmount + ", " +
				totalSell + ", " +
				totalProfit + ", " +
				month + ", " +
				year + ")" ;
		
		statisticsStatement.execute(cmd);
	}
	
	private void updateCustomerDatabase(String customerName, String netPrice, String profit, String lastestBuyDate) throws SQLException {
		
		String cmd = "";
		
		try { // ������������ǡ��Ѿഷ �ѧ����ա� insert 㹺��͡ catch
			cmd = "SELECT * FROM " + ApplicationFactory.CUSTOMER_DATABASE_NAME + " WHERE " + 
					ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " = '" + customerName + "'";
			ResultSet res = customersStatement.executeQuery(cmd);
		
			String count = res.getString(ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME);
			String totalBuy = res.getString(ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME);
			String totalProfit = res.getString(ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME);
			
			cmd = "UPDATE " + ApplicationFactory.CUSTOMER_DATABASE_NAME + " SET " +
					ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME + " = " + ( Integer.parseInt(count) + 1 ) + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME + " = " + ( Double.parseDouble(totalBuy) + Double.parseDouble(netPrice) ) + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME + " = " + ( Double.parseDouble(totalProfit) + Double.parseDouble(profit) ) + " WHERE " +
					ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " = '" + customerName + "'";
			
			customersStatement.executeUpdate(cmd);
		}
		catch(SQLException e) {
			e.printStackTrace();
			cmd = "INSERT INTO " + ApplicationFactory.CUSTOMER_DATABASE_NAME + "(" + 
					ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME + ", " + 
					ApplicationFactory.CUSTOMER_DATABASE_LASTEST_BUY_DATE_COLUMN_NAME + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME  + ")" + " VALUES ('" +
					customerName + "', " +
					"1, '" +
					lastestBuyDate + "', " + 
					netPrice + ", " +
					profit + ")";
			
			customersStatement.executeUpdate(cmd);
		}
		
	}
	
	private void updateStockDatabase(String itemSn, int sellAmount) throws SQLException {
		
		String cmd = "SELECT " + ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " FROM " +
				ApplicationFactory.MAIN_DATEBASE_NAME + " WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " = '" + itemSn + "'";
		
		ResultSet res = mainStatement.executeQuery(cmd);
		int amount  = res.getInt(ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT);
		
		cmd = "UPDATE " + ApplicationFactory.MAIN_DATEBASE_NAME + " SET " + 
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " = " + ( amount - sellAmount ) + " WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " = '" + itemSn + "'";
		mainStatement.executeUpdate(cmd);
	}
	
	public void pickDirectory() {
		FileChooser saver = new FileChooser();
		saver.setTitle("���͡��������·ҧ");
		saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf"));
		destinationDirectory = saver.showSaveDialog(null);

		selectBillDestinationBtn.setDisable(true);
		
		if(destinationDirectory == null){
		     //No Directory selected
		}else{
		     String path = destinationDirectory.getAbsolutePath();
		     saveBillDestinationTf.setText(path);		     
		     
		}
		
		selectBillDestinationBtn.setDisable(false);
	}
	
	private void initializeTable() {
		table.setFixedCellSize(WarehouseController.TABLE_ROW_SIZE);
		
		imageTc.setCellValueFactory(new PropertyValueFactory<>("image"));
		itemIdTc.setCellValueFactory(new PropertyValueFactory<>("itemId"));
		snTc.setCellValueFactory(new PropertyValueFactory<>("itemSn"));
		itemNameTc.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		buyPriceTc.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
		sellPriceTc.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
		sellAmountTc.setCellValueFactory(new PropertyValueFactory<>("sellAmount"));
		itemTotalPriceTc.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		
		table.setItems(itemOutDataSets);
		
		MenuItem delItem = new MenuItem("����͡");
		delItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
				confirm.setTitle("�׹�ѹ");
				confirm.setHeaderText(null);
				confirm.setContentText("����Թ��ҹ���͡�ҡ��¡�â��");
				
				Optional<ButtonType> result = confirm.showAndWait();
				if ( result.get() == ButtonType.OK ) {
					int index = table.getSelectionModel().getSelectedIndex();
					table.getItems().remove(index);
					updateFinancialTextField();
				}
				
			}
			
		});
		
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(delItem);
		table.setContextMenu(menu);
		
	}
	
	private void initializeFinancialTextFieldBinding() {
		
		ChangeListener<String> cl = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
		    	updateFinancialTextField();
			}
			
		};
		
		discountByBahtTf.textProperty().addListener(cl);
		discountByPercentTf.textProperty().addListener(cl);
		vatTf.textProperty().addListener(cl);
	}
	
	private void initializeNumericTextField() {			//do not use right now
		UnaryOperator<Change> filter = change -> {
		    String text = change.getText();

		    if (text.matches("[.0-9]*")) {
		        return change;
		    }

		    return null;
		};
		
		TextFormatter<String> dbbTextFormatter = new TextFormatter<>(filter);	
		discountByBahtTf.setTextFormatter(dbbTextFormatter);
		
		TextFormatter<String> dbpTextFormatter = new TextFormatter<>(filter);	
		discountByPercentTf.setTextFormatter(dbpTextFormatter);
		
		TextFormatter<String> vatTextFormatter = new TextFormatter<>(filter);
		vatTf.setTextFormatter(vatTextFormatter);
	}
	
	public void updateFinancialTextField() {
		
		if ( itemOutDataSets.size() <= 0 ) {
			priceTf.setText("");
			discountByPercentBahtTf.setText("");
			vatBahtTf.setText("");
			netPriceTf.setText("");
			profitTf.setText("");
			return ;
		}
		
		double price = 0.00;
		double allBuyPrice = 0.00;
		double discount = 0.00;
		double netPrice = 0.00;
		double profit = 0.00;
		
		for( ItemOutDataSet data : itemOutDataSets ) {
			price += Double.parseDouble(data.getTotalPrice());
			allBuyPrice += Double.parseDouble(data.getSellAmount()) * Double.parseDouble(data.getBuyPrice());
		}
		
		try {
			if ( discountByBahtRb.isSelected() ) {
				if ( !discountByBahtTf.getText().equals("") ) {
					discount = Double.parseDouble(discountByBahtTf.getText());
				}
			}
			else {
				if ( !discountByPercentTf.getText().equals("") ) {
					double percent = Double.parseDouble(discountByPercentTf.getText());
					discount = percent * price / 100.0;
					discountByPercentBahtTf.setText(decimalFormatter.format(discount));
				}
				else {
					discountByPercentBahtTf.setText("0.00");
				}
			}
		}catch(NumberFormatException e) {
			System.out.println(e);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Alert");
			alert.setContentText("Incorrect Format");
			alert.show();
		}
		
		double taxPercent = 0.00;
		if ( !vatTf.getText().equals("") ) {
			taxPercent = Double.parseDouble(vatTf.getText());
		}
		netPrice = price - discount;
		double taxBaht = netPrice * taxPercent / 100.0;
		netPrice += taxBaht;
		profit = netPrice - allBuyPrice;
		
		vatBahtTf.setText(decimalFormatter.format(taxBaht));
		priceTf.setText(decimalFormatter.format(price));
		netPriceTf.setText(decimalFormatter.format(netPrice));
		profitTf.setText(decimalFormatter.format(profit));
	}

	public void validateInput() {
		String customerName = customerNameTf.getText().trim();
		String billId = BillsIdGenerator.getBillId();
		String billDate = billDateTf.getText().trim();
		String note = noteTf.getText().trim();
		String discount;
		String netPrice = netPriceTf.getText().trim();
		String price = priceTf.getText().trim();
		
		String profit = profitTf.getText().trim();
		String lastestBuyDate = DateThai.getNumberDateFormat();
		String taxBaht = vatBahtTf.getText().trim();
		if ( taxBaht.equals("") ) {
			taxBaht = "0.00";
		}
		
		if ( itemOutDataSets.size() <= 0 ) {
			showInputWarningDialog("������Թ��ҷ��Т��㹢�й��");
			return ;
		}
		if ( discountByBahtRb.isSelected() ) {
				discount = discountByBahtTf.getText();
		}
		else {
				discount = discountByPercentBahtTf.getText();
		}
		if ( customerName.equals("") ) {
			showInputWarningDialog("��س��������١������ͺ���ѷ");
		}
		else if ( billId.equals("") ) {
			showInputWarningDialog("��س���������Ţ�͡���");
		}
		else if ( !isNumeric(billId) ) {
			showInputWarningDialog("��س���������Ţ�͡����繵���Ţ");
		}
		else if ( destinationDirectory == null ) {
			showInputWarningDialog("��س����͡��������·ҧ����Ѻ�ѹ�֡�͡���");
		}
		else if ( Double.parseDouble(netPrice) <= 0 ) {
			showInputWarningDialog("��س��������ŷҧ����Թ���١��ͧ");
		}
		else {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("����觫���	");
			alert.setHeaderText("�׹�ѹ����觫���?");
			alert.setContentText("�׹�ѹ����觫��� " + itemOutDataSets.size() + "��¡��");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					updateCustomerDatabase(customerName, netPrice, profit, lastestBuyDate);
					updateBillsDatabase(billId, billDate, customerName, note);
					updateData();
					File billsDir = new File("./bills/");
					if ( !billsDir.exists() ) {
						billsDir.mkdirs();
					}
					BillPDF.printPDF(itemOutDataSets, customerName, DateThai.getCurrentThaiDate(),  billId+"", "./bills/"+billId+".pdf", discount, taxBaht, price, netPrice);
					BillPDF.printPDF(itemOutDataSets, customerName, DateThai.getCurrentThaiDate(),  billId+"", destinationDirectory.getAbsolutePath(), discount, taxBaht, price, netPrice);
					showInfomationDialog("����������", "����觢���������", "");
					warehouseController.searchData();
				}
					catch(SQLException e) {
						showErrorDialog("��ͼԴ��Ҵ", "�պҧ���ҧ�Դ���Ԣ�С��ѧ�Ѿഷ�ҹ������", "��س��ͧ���������ѧ");
						e.printStackTrace();
					}
			} else {
			    // ... user chose CANCEL or closed the dialog
			}
		
		}
	}
	
	private void showInfomationDialog(String title, String header, String message) {
		Alert info = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
		info.setTitle(title);
		info.setHeaderText(header);
		info.show();
	}
	
	private void updateData() throws SQLException {
		for( ItemOutDataSet itds : itemOutDataSets ) {
			String itemName = itds.getItemName();
			String itemId = itds.getItemId();
			String itemSn = itds.getItemSn();
			String sellAmount = itds.getSellAmount();
			String totalSell = itds.getTotalPrice();
			String itemProfit = ( Double.parseDouble(totalSell) - (Double.parseDouble(itds.getBuyPrice()) * Double.parseDouble(sellAmount))) + "";
			int month = DateThai.getCurrentMonthNumber();
			int year = DateThai.getCurrentYear();
			
			updateStockDatabase(itemSn, Integer.parseInt(sellAmount));
			updateStatisticsDatabase(itemName, itemId, itemSn, sellAmount, totalSell, itemProfit, month, year);
		}
		
	}
	
	private boolean isNumeric(String text) {
		try {
			Double.parseDouble(text);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	private void showInputWarningDialog(String message) {
		showAlertDialog("��ͼԴ��Ҵ", "��س������������١��ͧ", message);
	}
	
	private void showAlertDialog(String title, String header, String message) {
		Alert infomation = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
		infomation.setTitle(title);
		infomation.setHeaderText(header);
		infomation.show();
	}
	
	public void clearInput() {
		itemOutDataSets.clear();
		customerNameTf.setText("");
		billIdTf.setText(BillsIdGenerator.getBillId());
		billDateTf.setText(DateThai.getCurrentThaiDate());
		noteTf.setText("");
		saveBillDestinationTf.setText("");
		destinationDirectory = null;
		priceTf.setText("");
		discountByBahtTf.setText("0.0");
		discountByPercentTf.setText("0");
		discountByPercentBahtTf.setText("0.0");
		vatTf.setText("0");
		vatBahtTf.setText("");
		netPriceTf.setText("");
		profitTf.setText("");
		
		
	}
	
	public void setDiscountByBath() {
		discountByBahtTf.setDisable(false);
		
		discountByPercentTf.setDisable(true);
		discountByPercentBahtTf.setDisable(true);
		updateFinancialTextField();
	}
	
	public void setDiscountByPercent() {
		discountByPercentTf.setDisable(false);
		discountByPercentBahtTf.setDisable(false);
		
		discountByBahtTf.setDisable(true);
		updateFinancialTextField();

	}
}

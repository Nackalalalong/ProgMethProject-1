package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import application.DateThai;
import application.ItemOutDataSet;
import factory.ApplicationFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

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
	private Statement stockStatement;
	
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
		discountByBahtRb.fire();
		
		try {
			initializeCustomerDatabaseConnection();
			initializeStatisticsDatabaseConnection();
			initializeStockDatabaseConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			showErrorDialog("มีบางอย่างผิดพลาด", "การเชื่อมต่อกับฐานข้อมูลล้มเหลว", "กรุณาลองใหม่ภายหลัง");
			e.printStackTrace();
		}
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
	
	public void initializeStockDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.MAIN_DATABASE_FILE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		stockStatement = connection.createStatement();
	}
	
	public void initializeCustomerDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CUSTOMER_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		customersStatement = connection.createStatement();
	}
	
	private void initializeStatisticsDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.STATISTICS_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		statisticsStatement = connection.createStatement();
	}
	
	private void updateStatisticsDatabase(String itemName, String itemId, String itemSn, String sellAmount, String totalSell, String totalProfit, int month, int year) throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.STATISTICS_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME + " TEXT, " +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_ID_COLUMN_NAME + " INTEGER, " +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_SERIAL_NUMBER_COLUMN_NAME + " INTEGER, " + 
				ApplicationFactory.STATISTICS_DATABASE_SELL_AMOUNT_COLUMN_NAME + " INTEGER, " +
				ApplicationFactory.STATISTICS_DATABASE_TOTAL_SELL_COLUMN_NAME + " REAL, " +
				ApplicationFactory.STATISTICS_DATABASE_TOTAL_PROFIT_COLUMN_NAME + " REAL, " +
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + " INTEGER, " +
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + " YEAR)";
		
		statisticsStatement.execute(cmd);
		
		cmd = "INSERT INTO " +ApplicationFactory.STATISTICS_DATABASE_NAME + "(" +
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
	
	private void updateCustomerDatabase(String customerName, String netPrice, String profit, String lastestBuyDate, String note) throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.CUSTOMER_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " TEXT, " +
				ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME + " INTEGER, " + 
				ApplicationFactory.CUSTOMER_DATABASE_LASTEST_BUY_DATE_COLUMN_NAME + " TEXT, " +
				ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME + " REAL, " +
				ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME + " REAL, " +
				ApplicationFactory.CUSTOMER_DATABASE_NOTE_COLUMN_NAME + " TEXT)";
		
		customersStatement.execute(cmd);
		
		try {
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
					ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME + ", " +
					ApplicationFactory.CUSTOMER_DATABASE_NOTE_COLUMN_NAME + ")" + " VALUES ('" +
					customerName + "', " +
					"1, '" +
					lastestBuyDate + "', " + 
					netPrice + ", " +
					profit + ", '" +
					note + "')";
			
			customersStatement.executeUpdate(cmd);
		}
		
	}
	
	private void updateStockDatabase(String itemSn, int sellAmount) throws SQLException {
		
		String cmd = "SELECT " + ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " FROM " +
				ApplicationFactory.MAIN_DATEBASE_NAME + " WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " = '" + itemSn + "'";
		
		ResultSet res = stockStatement.executeQuery(cmd);
		int amount  = res.getInt(ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT);
		
		cmd = "UPDATE " + ApplicationFactory.MAIN_DATEBASE_NAME + " SET " + 
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " = " + ( amount - sellAmount ) + " WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " = '" + itemSn + "'";
		stockStatement.executeUpdate(cmd);
	}
	
	public void pickDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("เลือกโฟลเดอร์ปลายทาง");
		destinationDirectory = directoryChooser.showDialog(null);

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
			return ;
		}
		
		double price = 0.0;
		double allBuyPrice = 0.0;
		double discount = 0.0;
		double netPrice = 0.0;
		double profit = 0.0;
		
		for( ItemOutDataSet data : itemOutDataSets ) {
			price += Double.parseDouble(data.getTotalPrice());
			allBuyPrice += Double.parseDouble(data.getSellAmount()) * Double.parseDouble(data.getBuyPrice());
		}
		
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
		}
		
		double taxPercent = 0.0;
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
		String billId = billIdTf.getText().trim();
		String billDate = billDateTf.getText().trim();
		String note = noteTf.getText().trim();
		String discount;
		String netPrice = netPriceTf.getText();
		
		String profit = profitTf.getText().trim();
		String lastestBuyDate = DateThai.getNumberDateFormat();
		
		if ( itemOutDataSets.size() <= 0 ) {
			showInputWarningDialog("ไม่มีสินค้าที่จะขายในขณะนี้");
			return ;
		}
		if ( discountByBahtRb.isSelected() ) {
				discount = discountByBahtTf.getText();
		}
		else {
				discount = discountByPercentBahtTf.getText();
		}
		if ( customerName.equals("") ) {
			showInputWarningDialog("กรุณาใส่ชื่อลูกค้าหรือบริษัท");
		}
		else if ( billId.equals("") ) {
			showInputWarningDialog("กรุณาใส่หมายเลขเอกสาร");
		}
		else if ( !isNumeric(billId) ) {
			showInputWarningDialog("กรุณาใส่หมายเลขเอกสารเป็นตัวเลข");
		}
		else if ( destinationDirectory == null ) {
			showInputWarningDialog("กรุณาเลือกโฟลเดอร์ปลายทางสำหรับบันทึกเอกสาร");
		}
		else if ( Double.parseDouble(netPrice) <= 0 ) {
			showInputWarningDialog("กรุณาใส่ข้อมูลทางการเงินให้ถูกต้อง");
		}
		else {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("คำสั่งซื้อ	");
			alert.setHeaderText("ยืนยันคำสั่งซื้อ?");
			alert.setContentText("ยืนยันคำสั่งซื้อ " + itemOutDataSets.size() + "รายการ");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					updateData();
					updateCustomerDatabase(customerName, netPrice, profit, lastestBuyDate, note);
					showInfomationDialog("คำสั่งสำเร็จ", "คำสั่งขายเสร็จสิ้น", "");
					warehouseController.searchData();
				}
					catch(SQLException e) {
						showErrorDialog("ข้อผิดพลาด", "มีบางอย่างผิดปกติขณะกำลังอัพเดทฐานข้อมูล", "กรุณาลองใหม่ภายหลัง");
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
		showAlertDialog("ข้อผิดพลาด", "กรุณาใส่ข้อมูลให้ถูกต้อง", message);
	}
	
	private void showAlertDialog(String title, String header, String message) {
		Alert infomation = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
		infomation.setTitle(title);
		infomation.setHeaderText(header);
		infomation.show();
	}
	
	public void clearInput() {
		customerNameTf.setText("");
		billIdTf.setText("");
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
		
		itemOutDataSets.clear();
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

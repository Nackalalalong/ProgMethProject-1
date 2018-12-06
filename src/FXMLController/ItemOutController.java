package FXMLController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import application.DateThai;
import application.ItemOutDataSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
		vatTf, vatbathTf, netPriceTf, profitTf;
	
	@FXML
	private Button viewBillBtn, selectBillDestinationBtn, okBtn, cancelBtn;
	
	@FXML
	private RadioButton discountByBahtRb, discountByPercentRb;
	
	@FXML
	private TableView table;
	
	@FXML
	private TableColumn imageTc, itemIdTc, itemNameTc, buyPriceTc, sellPriceTc, sellAmountTc, itemTotalPriceTc;
	
	private ObservableList<ItemOutDataSet> itemOutDataSets;
	private File destinationDirectory;
	
	private static final String DEFAULT_BILL_DIRECTORY = "./bills/";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		itemOutDataSets = FXCollections.observableArrayList();
		destinationDirectory = null;
		
		initializeNumericTextField();
		initializeTable();
		initializeFinancialTextFieldBinding();
		
		billDateTf.setText(DateThai.getCurrentThaiDate());
		discountByBahtRb.fire();
	}
	
	public ObservableList<ItemOutDataSet> getItemOutDataSets() {
		return itemOutDataSets;
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
		String style = "-fx-alignment: CENTER";
		imageTc.setStyle(style);
		itemIdTc.setStyle(style);
		itemNameTc.setStyle(style);
		buyPriceTc.setStyle(style);
		sellPriceTc.setStyle(style);
		sellAmountTc.setStyle(style);
		itemTotalPriceTc.setStyle(style);
		
		imageTc.setCellValueFactory(new PropertyValueFactory<>("image"));
		itemIdTc.setCellValueFactory(new PropertyValueFactory<>("itemId"));
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
				discountByPercentBahtTf.setText(discount + "");
			}
		}
		
		netPrice = price - discount;
		profit = netPrice - allBuyPrice;
		
		priceTf.setText(price + "");
		netPriceTf.setText(netPrice + "");
		profitTf.setText(profit + "");
	}

	public void validateInput() {
		String customerName = customerNameTf.getText().trim();
		String billId = billIdTf.getText().trim();
		String billDate = billDateTf.getText().trim();
		String note = noteTf.getText().trim();
		String discount;
		String netPrice = netPriceTf.getText();
		
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
		vatbathTf.setText("");
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

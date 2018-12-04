package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import factory.ApplicationFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemInController implements Initializable {
	
	@FXML
	private TextField itemNameTf, itemIdTf, snTf, unitTf, buyPriceTf, sellPriceTf, amountTf, categoryTf, subCategoryTf;
	
	@FXML
	private ImageView showImageIv;
	
	@FXML
	private Button addImageBtn, acceptBtn, deniedBtn;
	
	@FXML
	private TextArea noteTa;
	
	private Image pickedImage;
	
	private Statement statement;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		pickedImage = null;
		showImageIv.setImage(new Image(ClassLoader.getSystemResource("icons/box.png").toString()));
		
	}
	
	public void pickImage() {
		
	}
	
	public void initilizeDatabaseConnection() {
		String path = "jdbc:sqlite:" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + ApplicationFactory.MAIN_DATABASE_FILE_NAME + ".db";
		
		try {
			String dbPath = "./database/";
			File dir = new File(dbPath);
			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			Connection connection = DriverManager.getConnection(path);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createMainDatabase() {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.MAIN_DATEBASE_NAME +"(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NAME + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_ID + " INTEGER, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " INTEGER, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_UNIT + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_BUYPRICE + " REAL, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SELLPRICE + " REAL, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " INTEGER, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NOTE + " TEXT)";
	
		try {
			statement.execute(cmd);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			showInfomationDialog("มีบางอย่างผิดพลาด", "กรุณาลองใหม่ภายหลัง");
			e.printStackTrace();
		}
	}
	
	private void insertDataToMainDatabase(String itemName, int itemId, int sn, String unit, double buyPrice
			, double sellPrice, int amount, String category, String subCategory, String note) {
		String cmd = "INSERT INTO " + ApplicationFactory.MAIN_DATEBASE_NAME + "(" +
				ApplicationFactory.MAIN_DATABASE_ITEM_NAME + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_ID + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_UNIT + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_BUYPRICE + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SELLPRICE + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NOTE + ") VALUES('" +
				itemName + "', " +
				itemId + ", " +
				sn + ", '" +
				unit + "', " +
				buyPrice + ", " +
				sellPrice + ", " +
				amount + ", '" +
				category + "', '" +
				subCategory + "', '" +
				note + "')";
		
			try {
				statement.executeUpdate(cmd);
				saveImage();
				showInfomationDialog("ฐานข้อมูลสินค้า", "เพิ่มสินค้าในคลังสินค้าสำเร็จ");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				showInfomationDialog("มีบางอย่างผิดพลาด", "กรุณาลองใหม่ภายหลัง");
				e.printStackTrace();
			}
	}
	
	private void saveImage() {
		// save image with unique name <serial number>.jpg box.png if image that name not found
	}

	public void validateUserInput() {
		String itemName = itemNameTf.getText().trim();
		String itemId = itemIdTf.getText().trim();
		String sn = snTf.getText().trim();
		String unit = unitTf.getText().trim();
		String buyPrice = buyPriceTf.getText().trim();
		String sellPrice = sellPriceTf.getText().trim();
		String amount = amountTf.getText().trim();
		String category = categoryTf.getText().trim();
		String subCategory = subCategoryTf.getText().trim();
		String note = noteTa.getText().trim();
		
		if ( itemName.equals("") ) {
			showInvalidInputDialog("กรุณาใส่ชื่อสินค้า");
		}
		else if ( itemId.equals("") ) {
			showInvalidInputDialog("กรุณาใส่รหัสสินค้า");
		}
		else if ( !isNumeric(itemId) ) {
			showInvalidInputDialog("กรุณาใส่รหัสสินค้าเป็นตัวเลข");
		}
		else if ( sn.equals("") ) {
			showInvalidInputDialog("กรุณาใส่ serial number");
		}
		else if ( !isNumeric(sn) ) {
			showInvalidInputDialog("กรุณาใส่ serial number เป็นตัวเลข");
		}
		else if ( unit.equals("") ) {
			showInvalidInputDialog("กรุณาใส่หน่วยสินค้า");
		}
		else if ( buyPrice.equals("") ) {
			showInvalidInputDialog("กรุณาใส่ราคาซื้อสินค้า");
		}
		else if ( !isNumeric(buyPrice) ) {
			showInvalidInputDialog("กรุณาใส่ราคาซื้อสินค้าเป็นตัวเลข");
		}
		else if ( sellPrice.equals("") ) {
			showInvalidInputDialog("กรุณาใส่ราคาขาย");
		}
		else if ( !isNumeric(sellPrice) ) {
			showInvalidInputDialog("กรุณาใส่ราคาขายสินค้าเป็นตัวเลข");
		}
		else if ( amount.equals("") ) {
			showInvalidInputDialog("กรุณาใส่จำนวนสินค้า");
		}
		else if ( !isNumeric(amount) ) {
			showInvalidInputDialog("กรุณาใส่จำนวนสินค้าเป้นตัวเลข");
		}
		else if ( category.equals("") ) {
			showInvalidInputDialog("กรุณาใส่หมวดหมู่สินค้า");
		}
		else {
			try {
			initilizeDatabaseConnection();
			createMainDatabase();
			insertDataToMainDatabase(itemName, Integer.parseInt(itemId), Integer.parseInt(sn), unit, Double.parseDouble(buyPrice)
					, Double.parseDouble(sellPrice), Integer.parseInt(amount), category, subCategory, note);
			}
			catch(Exception e) {
				//e.printStackTrace();
			}
		}
	}
	
	private boolean isNumeric(String text) {
		try {
		double test = Double.parseDouble(text);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void clearInput() {
		pickedImage = null;
		showImageIv.setImage(new Image(ClassLoader.getSystemResource("icons/box.png").toString()));
		
		itemIdTf.setText("");
		snTf.setText("");
		unitTf.setText("");
		buyPriceTf.setText("");
		sellPriceTf.setText("");
		amountTf.setText("");
		categoryTf.setText("");
		subCategoryTf.setText("");
		
		noteTa.setText("");
	}
	
	private void showInvalidInputDialog(String message) {
		showInfomationDialog("กรุณาใส่ข้อมูลให้ถูกต้อง" ,message);
	}
	
	private  void showInfomationDialog(String title, String message) {
		Alert infomation = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
		infomation.setTitle(title);
		infomation.setHeaderText(title);
		infomation.show();
	}

}

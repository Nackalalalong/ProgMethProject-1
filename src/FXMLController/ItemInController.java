package FXMLController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import factory.ApplicationFactory;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ItemInController implements Initializable {
	
	public static final int ITEM_IN_IMAGE_FIT_WIDTH = 250;
	
	@FXML
	private TextField itemNameTf, itemIdTf, snTf, unitTf, buyPriceTf, sellPriceTf, amountTf, categoryTf, subCategoryTf;
	
	@FXML
	private ImageView showImageIv;
	
	@FXML
	private Button addImageBtn, acceptBtn, deniedBtn;
	
	@FXML
	private TextArea noteTa;
	
	private BufferedImage pickedImage;
	
	private Statement statement;
	
	private WarehouseController warehouseController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		pickedImage = null;
		showImageIv.setImage(new Image(ClassLoader.getSystemResource("icons/box.png").toString()));
		showImageIv.setFitWidth(ITEM_IN_IMAGE_FIT_WIDTH);
		
	}
	
	public void setWarehouseController(WarehouseController wc) {
		warehouseController = wc;
	}
	
	public WarehouseController getWarehouseController() {
		return warehouseController;
	}
	
	public void pickImage() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("เลือกรูปภาพ");
		chooser.getExtensionFilters().addAll(
		        new FileChooser.ExtensionFilter("Image Files",
		            "*.png", "*.jpg", "*.jpeg"));
		
		addImageBtn.setDisable(true);
		File file = chooser.showOpenDialog(null);
		
		if ( file != null ) {
			try {
				pickedImage = ImageIO.read(file);
				showImageIv.setImage(SwingFXUtils.toFXImage(pickedImage, null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		addImageBtn.setDisable(false);
	}
	
	public void initilizeDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.MAIN_DATABASE_FILE_NAME + ".sqlite";
		
			String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
			File dir = new File(dbPath);
			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			Connection connection = DriverManager.getConnection(path);
			statement = connection.createStatement();
		
	}
	
	private void manipulateCategoryDatabase(String category) throws SQLException {
		String catePath = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CATEGORY_DATABASE_NAME + ".sqlite";
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		
		Connection connection = DriverManager.getConnection(catePath);
		Statement stm = connection.createStatement();
		
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.CATEGORY_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + " TEXT)";
		
		stm.execute(cmd);
		
		cmd = "SELECT * FROM " + ApplicationFactory.CATEGORY_DATABASE_NAME + " WHERE " + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + " = '" + category + "'";
		
		ResultSet res = stm.executeQuery(cmd);
		
		if ( !res.next() ) {
			cmd  = "INSERT INTO " + ApplicationFactory.CATEGORY_DATABASE_NAME + " (" + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + ") VALUES ('" + category +"')";
			stm.execute(cmd);
			warehouseController.loadCategory(); //reload category in warehouse page
		}
	}
	
	private void manipulateSubCategoryDatabase(String category, String subCategory) throws SQLException  {
		
		String catePath = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + category + ".sqlite";
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		
		Connection connection = DriverManager.getConnection(catePath);
		Statement catStm = connection.createStatement();
		
		String cmd = "CREATE TABLE IF NOT EXISTS " + category + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " TEXT)";
		
		catStm.execute(cmd);
		
		cmd = "SELECT * FROM " + category + " WHERE " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " = '" + subCategory + "'";
		
		ResultSet res = catStm.executeQuery(cmd);
		
		if ( !res.next() ) {    //ไม่มี sub-category ต้องเพิ่มใหม่
			cmd  = "INSERT INTO " + category + " (" + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + ") VALUES ('" + subCategory +"')";
			catStm.execute(cmd);
			warehouseController.loadCategory(); //reload category in warehouse page
		}
			
			/*cmd = "IF NOT EXISTS ( SELECT 1 FROM " + category + " WHERE subcategory = '" + subCategory + "') " +
					"INSERT INTO " + category + " (" + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + ") VALUES ('" + subCategory +"')";*/
			
	}
	
	private void createMainDatabase() throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.MAIN_DATEBASE_NAME +"(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NAME + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_ID + " INTEGER, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_UNIT + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_BUY_PRICE + " REAL, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SELL_PRICE + " REAL, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " INTEGER, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY + " TEXT, " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NOTE + " TEXT)";
	
			statement.execute(cmd);

	}
	
	private void insertDataToMainDatabase(String itemName, int itemId, String sn, String unit, double buyPrice
			, double sellPrice, int amount, String category, String subCategory, String note) throws Exception {
		String cmd = "INSERT INTO " + ApplicationFactory.MAIN_DATEBASE_NAME + "(" +
				ApplicationFactory.MAIN_DATABASE_ITEM_NAME + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_ID + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_UNIT + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_BUY_PRICE + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SELL_PRICE + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY + ", " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NOTE + ") VALUES('" +
				itemName + "', " +
				itemId + ", '" +
				sn + "', '" +
				unit + "', " +
				buyPrice + ", " +
				sellPrice + ", " +
				amount + ", '" +
				category + "', '" +
				subCategory + "', '" +
				note + "')";
		
				statement.executeUpdate(cmd);
				saveImage(itemId);
				showInfomationDialog("ฐานข้อมูลสินค้า", "เพิ่มสินค้าในคลังสินค้าสำเร็จ");
				
	}
	
	private void saveImage(int itemId) throws IOException {
		// save image with unique name <serial number>.jpg box.png if image that name not found
		if ( pickedImage == null ) {
			return;
		}
		
		String path = "./" + ApplicationFactory.MAIN_DATABASE_IMAGE_DIRECTORY + "/";
		File destination = new File(path);
		if ( !destination.exists() ) {
			destination.mkdirs();
		}
		
		File output = new File(path + itemId + ".jpg");
		ImageIO.write(pickedImage, "jpg", output);

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
			manipulateCategoryDatabase(category);
			manipulateSubCategoryDatabase(category, subCategory);
			insertDataToMainDatabase(itemName, Integer.parseInt(itemId), sn, unit, Double.parseDouble(buyPrice)
					, Double.parseDouble(sellPrice), Integer.parseInt(amount), category, subCategory, note);
			}
			catch(Exception e) {
				//e.printStackTrace();
				e.printStackTrace();
				showInfomationDialog("มีบางอย่างผิดพลาด", "กรุณาลองใหม่ภายหลัง");
				return;
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
		
		itemNameTf.setText("");
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

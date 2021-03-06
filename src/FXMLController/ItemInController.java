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
import factory.DatabaseCenter;
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
	
	private Statement mainStatement;
	
	private WarehouseController warehouseController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		mainStatement = DatabaseCenter.getMainStatement();
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
		chooser.setTitle("���͡�ٻ�Ҿ");
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
	
	private void manipulateCategoryDatabase(String category) throws SQLException {	
		Statement categoryStatement = DatabaseCenter.getCategoryStatement();
		
		String cmd = "SELECT * FROM " + ApplicationFactory.CATEGORY_DATABASE_NAME + " WHERE " + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + " = '" + category + "'";
		
		ResultSet res = categoryStatement.executeQuery(cmd);
		
		if ( !res.next() ) {
			cmd  = "INSERT INTO " + ApplicationFactory.CATEGORY_DATABASE_NAME + " (" + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + ") VALUES ('" + category +"')";
			categoryStatement.execute(cmd);
			warehouseController.loadCategory(); //reload category in warehouse page
		}
	}
	
	private void manipulateSubCategoryDatabase(String category, String subCategory) throws SQLException  {
		Statement subCategoryStatement = DatabaseCenter.getSubCategoryDatabaseConnection(category);
		
		String cmd = "CREATE TABLE IF NOT EXISTS " + category + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " TEXT)";
		
		subCategoryStatement.execute(cmd);
		
		cmd = "SELECT * FROM " + category + " WHERE " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " = '" + subCategory + "'";
		
		ResultSet res = subCategoryStatement.executeQuery(cmd);
		
		if ( !res.next() ) {    //����� sub-category ��ͧ��������
			cmd  = "INSERT INTO " + category + " (" + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + ") VALUES ('" + subCategory +"')";
			subCategoryStatement.execute(cmd);
			warehouseController.loadCategory(); //reload category in warehouse page
		}
			
			/*cmd = "IF NOT EXISTS ( SELECT 1 FROM " + category + " WHERE subcategory = '" + subCategory + "') " +
					"INSERT INTO " + category + " (" + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + ") VALUES ('" + subCategory +"')";*/
			
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
		
				mainStatement.executeUpdate(cmd);
				saveImage(itemId);
				showInfomationDialog("�ҹ�������Թ���", "�����Թ���㹤�ѧ�Թ��������");
				
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
			showInvalidInputDialog("��س��������Թ���");
		}
		else if ( itemId.equals("") ) {
			showInvalidInputDialog("��س���������Թ���");
		}
		else if ( !isNumeric(itemId) ) {
			showInvalidInputDialog("��س���������Թ����繵���Ţ");
		}
		else if ( sn.equals("") ) {
			showInvalidInputDialog("��س���� serial number");
		}
		else if ( unit.equals("") ) {
			showInvalidInputDialog("��س����˹����Թ���");
		}
		else if ( buyPrice.equals("") ) {
			showInvalidInputDialog("��س�����Ҥҫ����Թ���");
		}
		else if ( !isNumeric(buyPrice) ) {
			showInvalidInputDialog("��س�����Ҥҫ����Թ����繵���Ţ");
		}
		else if ( sellPrice.equals("") ) {
			showInvalidInputDialog("��س�����ҤҢ��");
		}
		else if ( !isNumeric(sellPrice) ) {
			showInvalidInputDialog("��س�����ҤҢ���Թ����繵���Ţ");
		}
		else if ( amount.equals("") ) {
			showInvalidInputDialog("��س����ӹǹ�Թ���");
		}
		else if ( !isNumeric(amount) ) {
			showInvalidInputDialog("��س����ӹǹ�Թ����鹵���Ţ");
		}
		else if ( category.equals("") ) {
			showInvalidInputDialog("��س������Ǵ�����Թ���");
		}
		else {
			try {
			manipulateCategoryDatabase(category);
			manipulateSubCategoryDatabase(category, subCategory);
			insertDataToMainDatabase(itemName, Integer.parseInt(itemId), sn, unit, Double.parseDouble(buyPrice)
					, Double.parseDouble(sellPrice), Integer.parseInt(amount), category, subCategory, note);
			}
			catch(Exception e) {
				//e.printStackTrace();
				e.printStackTrace();
				showInfomationDialog("�պҧ���ҧ�Դ��Ҵ", "��س��ͧ���������ѧ");
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
		showInfomationDialog("��س������������١��ͧ" ,message);
	}
	
	private  void showInfomationDialog(String title, String message) {
		Alert infomation = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
		infomation.setTitle(title);
		infomation.setHeaderText(title);
		infomation.show();
	}

}

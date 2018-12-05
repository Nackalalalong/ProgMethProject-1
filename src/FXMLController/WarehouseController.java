package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.DataSet;
import factory.ApplicationFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class WarehouseController implements Initializable {
	
	public static final int TABLE_ROW_SIZE  = 100;
	
	@FXML
	private TextField itemIdTf, itemNameTf;
	
	@FXML
	private ComboBox categoryCb, subCategoryCb;
	
	@FXML
	private Button searchBtn, clearBtn;
	
	@FXML
	private TableView table;
	
	@FXML
	private TableColumn imageTc, itemIdTc, snTc, itemNameTc, unitTc, amountTc, buyPriceTc, sellPriceTc, categoryTc, subCategoryTc, noteTc;
	
	private Statement statement;
	private Statement categoryStatement;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeDatabaseConnection();
		loadCategory();
		
		initializeTable();
		
	}
	
	
	public void loadCategory() {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CATEGORY_DATABASE_NAME + ".db";
		
		try {
			String dbPath = "./database/";
			File dir = new File(dbPath);
			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			Connection connection = DriverManager.getConnection(path);
			categoryStatement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String cmd = "SELECT " + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + " FROM " + ApplicationFactory.CATEGORY_DATABASE_NAME;
		try {
			ResultSet categoryRes = categoryStatement.executeQuery(cmd);
			ObservableList<String> cats = FXCollections.observableArrayList();
			cats.add("ทั้งหมด");
			while( categoryRes.next() ) {
				cats.add(categoryRes.getString("category"));
			}
			
			categoryCb.setItems(cats);
			categoryCb.getSelectionModel().select(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeDatabaseConnection() {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.MAIN_DATABASE_FILE_NAME + ".db";
		
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
	
	public void clearInput() {
		itemIdTf.setText("");
		itemNameTf.setText("");
		
		categoryCb.getSelectionModel().select(0);
		subCategoryCb.setItems(null);
	}
	
	private void initializeTable() {
		table.setFixedCellSize(TABLE_ROW_SIZE);
		String style = "-fx-alignment: CENTER";
		imageTc.setStyle(style);
		itemIdTc.setStyle(style);
		snTc.setStyle(style);
		itemNameTc.setStyle(style);
		unitTc.setStyle(style);
		amountTc.setStyle(style);
		buyPriceTc.setStyle(style);
		sellPriceTc.setStyle(style);
		categoryTc.setStyle(style);
		subCategoryTc.setStyle(style);
		noteTc.setStyle(style);
		
		imageTc.setCellValueFactory(new PropertyValueFactory<>("image"));
		itemIdTc.setCellValueFactory(new PropertyValueFactory<>("itemId"));
		snTc.setCellValueFactory(new PropertyValueFactory<>("itemSn"));
		itemNameTc.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		unitTc.setCellValueFactory(new PropertyValueFactory<>("unit"));
		amountTc.setCellValueFactory(new PropertyValueFactory<>("amount"));
		buyPriceTc.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
		sellPriceTc.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
		categoryTc.setCellValueFactory(new PropertyValueFactory<>("category"));
		subCategoryTc.setCellValueFactory(new PropertyValueFactory<>("subCategory"));
		noteTc.setCellValueFactory(new PropertyValueFactory<>("note"));
	}
	
	private void loadDataToTable(ResultSet res) {
		ObservableList<DataSet> dataSets = FXCollections.observableArrayList();
		
		try {
			while( res.next() ) {
				String itemId = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_ID);
				String itemSn = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER);
				String itemName = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_NAME);
				String unit = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_UNIT);
				String amount = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT);
				String buyPrice = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_BUY_PRICE);
				String sellPrice = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_SELL_PRICE);
				String category = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY);
				String subCategory = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY);
				String note = res.getString(ApplicationFactory.MAIN_DATABASE_ITEM_NOTE);
				String imagePath = "./" + ApplicationFactory.MAIN_DATABASE_IMAGE_DIRECTORY + "/" + itemId + ".jpg";
								
				DataSet ds = new DataSet(imagePath, itemId, itemSn, itemName, unit, amount, buyPrice, sellPrice, category, subCategory, note);
				dataSets.add(ds);
			}
			
			table.setItems(dataSets);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorDialog("มีบางอย่างผิดพลาดขณะค้นหาข้อมูล", "กรุณาลองใหม่ภายหลัง");
		}
	}
	
	public void searchData() {
		
		String itemId = itemIdTf.getText().trim();
		String itemName = itemNameTf.getText().trim();
		String category = categoryCb.getSelectionModel().getSelectedItem().toString();
		Object subSelected = subCategoryCb.getSelectionModel().getSelectedItem();
		String subCategory = "";
		
		if ( category.equals("ทั้งหมด") ) {
			category = "";
		}
		if ( subSelected != null ) {
			subCategory = subSelected.toString();
			if ( subCategory.equals("ทั้งหมด") ) {
				subCategory = "";
			}
		}
		
		String cmd = "SELECT * FROM '" + ApplicationFactory.MAIN_DATEBASE_NAME + "' WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_ID + " LIKE '%" + itemId + "%' AND " +
				ApplicationFactory.MAIN_DATABASE_ITEM_NAME + " LIKE '%" + itemName + "%' AND " +
				ApplicationFactory.MAIN_DATABASE_ITEM_CATEGORY + " LIKE '%" + category + "%' AND " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SUBCATEGORY + " LIKE '%" + subCategory + "%'";
		
		//System.out.println(cmd);
		
		try {
			ResultSet searchResult = statement.executeQuery(cmd);
			loadDataToTable(searchResult);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorDialog("มีบางอย่างผิดพลาดขณะค้นหาข้อมูล", "กรุณาลองใหม่ภายหลัง");
		}

	}
	
	private void showErrorDialog(String header, String message) {
		Alert infomation = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		infomation.setTitle("ข้อผิดพลาด");
		infomation.setHeaderText(header);
		infomation.show();
	}
	
	public void createSubCategory() {
		int selected = categoryCb.getSelectionModel().getSelectedIndex();   // "ทั้งหมด" index = 0
		
		if ( selected == 0 ) {
			subCategoryCb.setItems(null);
		}
		else {
			
			String category = categoryCb.getSelectionModel().getSelectedItem().toString();
			
			String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + category + ".db";
			
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
			
			String cmd = "SELECT " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " FROM " + category;
			
			try {
				
				ResultSet res = statement.executeQuery(cmd);
				ObservableList<String> subs = FXCollections.observableArrayList();
				subs.add("ทั้งหมด");
				
				while( res.next() ) {
					String subcate = res.getString(ApplicationFactory.SUB_CATEGORY_COLUMN_NAME);
					if ( !subcate.equals("") ) {
						subs.add(subcate);
					}
				}
				
				subCategoryCb.setItems(subs);
				subCategoryCb.getSelectionModel().select(0);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}

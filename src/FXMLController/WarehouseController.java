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

import dataModel.DataSet;
import dataModel.ItemOutDataSet;
import exceptions.MaxSellException;
import factory.ApplicationFactory;
import factory.DatabaseCenter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class WarehouseController implements Initializable {
	
	public static final int TABLE_ROW_SIZE  = 100;
	public static final int MAX_SELL_COUNT = 15;
	
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
	
	private Statement mainStatement;
	private Statement categoryStatement;
	
	private ItemOutController itemOutController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mainStatement = DatabaseCenter.getMainStatement();
		categoryStatement = DatabaseCenter.getCategoryStatement();
		loadCategory();
		
		initializeTable();
		
	}
	
	public ItemOutController getItemOutController() {
		return itemOutController;
	}
	
	public void setItemOutController(ItemOutController ioc) {
		itemOutController = ioc;
	}
	
	public void loadCategory() {

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
	
	public void clearInput() {
		itemIdTf.setText("");
		itemNameTf.setText("");
		
		categoryCb.getSelectionModel().select(0);
		subCategoryCb.setItems(null);
	}
	
	private void initializeTable() {
		table.setFixedCellSize(TABLE_ROW_SIZE);
		
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
		
		MenuItem menuItem = new MenuItem("ขาย");
		menuItem.setOnAction((ActionEvent event) -> {
			if ( itemOutController.getItemOutDataSets().size() >= MAX_SELL_COUNT ) {
				try {
					throw new MaxSellException();
				} catch (MaxSellException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
			    DataSet dataSet = (DataSet) table.getSelectionModel().getSelectedItem();
			    showSellDialog(dataSet);
			}
		});
		
		MenuItem addItem = new MenuItem("เพิ่มสินค้าเข้าคลัง");
		addItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				DataSet dataSet = (DataSet) table.getSelectionModel().getSelectedItem();
				showAddItemDialog(dataSet);
			}
			
		});
		
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(menuItem);
		menu.getItems().add(addItem);
		table.setContextMenu(menu);
	}
	
	private void showAddItemDialog(DataSet dataSet) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("เลือกจำนวนสินค้าที่จะเพิ่ม");;
		dialog.setHeaderText("จำนวนสินค้าในคลัง : " + dataSet.getAmount());
		dialog.setContentText("จำนวนสินค้าที่จะเพิ่ม ");;
		
		Optional<String> result = dialog.showAndWait();
		if ( result.isPresent() ) {
			try {
				int addAmount = Integer.parseInt(result.get().trim());
				if ( addAmount <= 0 ){
					showErrorDialog("กรุณาใส่จำนวนสินค้าให้ถูกต้อง", "");
				}
				else {
					Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
					confirm.setTitle("ยืนยัน");;
					confirm.setHeaderText("เพิ่มสินค้า " + dataSet.getItemName() + " จำนวน " + addAmount + " " + dataSet.getUnit());
					confirm.setContentText("ยืนยันการเพิ่มสินค้าเข้าคลังสินค้า?");
					Optional<ButtonType> confirmResult = confirm.showAndWait();
					if (confirmResult.get() == ButtonType.OK){
					    updateExistsStockItem(dataSet, addAmount);
					} 
					
				}
				
			}
			catch(SQLException e) {
				showErrorDialog("เกิดปัญหาในการเชื่อมต่อกับฐานข้อมูล", "กรุณาลองใหม่ภายหลัง");
			}
			catch(Exception e) {
				showErrorDialog("กรุณาใส่จำนวนสินค้าเป็นตัวเลข", "");
			}
		}
		
	}
	
	private void updateExistsStockItem(DataSet dataSet, int addAmount) throws SQLException {
		String cmd = "UPDATE " + ApplicationFactory.MAIN_DATEBASE_NAME + " SET " +
				ApplicationFactory.MAIN_DATABASE_ITEM_AMOUNT + " = " + (Integer.parseInt(dataSet.getAmount()) + addAmount ) + " WHERE " +
				ApplicationFactory.MAIN_DATABASE_ITEM_SERIAL_NUNBER + " = '" + dataSet.getItemSn() + "'";
		
		mainStatement.executeUpdate(cmd);
		Alert info =  new Alert(Alert.AlertType.INFORMATION, "เพิ่มสินค้าในคลังเสร็จสิ้น", ButtonType.OK);
		searchData();
		info.show();
		
	}

	private void showSellDialog(DataSet dataSet) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("เลือกจำนวนสินค้าที่จะขาย");
		dialog.setHeaderText("จำนวนสินค้าในคลัง : " + dataSet.getAmount());
		dialog.setContentText("จำนวนสินค้าที่จะขาย");
	
		Optional<String> result = dialog.showAndWait();
		if ( result.isPresent() ) {
			try {
				int sellAmount = Integer.parseInt(result.get().trim());
				if ( Integer.parseInt(dataSet.getAmount()) <= 0 ) {
					showErrorDialog("ไม่มีสินค้าในคลังในขณะนี้", "กรุณาลองใหม่ภายหลัง");
				}
				else if ( sellAmount > Integer.parseInt(dataSet.getAmount()) ) {
					showErrorDialog("จำนวนสินค้าในคลังไม่เพียงพอ", "คุณใส่จำนวนสินค้าที่จะขายมากกว่าจำนวนสินค้าในคลัง");
				}
				else {
					sendItemToItemOutPage(dataSet, sellAmount);
					
				}
			}
			catch(Exception e) {
				showErrorDialog("กรุณาใส่จำนวนสินค้าเป็นตัวเลข", "");
			}
		}
	
	}
	
	private void sendItemToItemOutPage(DataSet dataSet, int sellAmount) {
		for( ItemOutDataSet iods : itemOutController.getItemOutDataSets() ) {
			if ( iods.getItemSn().equals(dataSet.getItemSn()) ) {
				showErrorDialog("คุณได้เพิ่มสินค้านี้แล้ว", "");
				return ;
			}
		}
		ItemOutDataSet itemOutDataSet = new ItemOutDataSet(new DataSet(dataSet), sellAmount);
		itemOutController.getItemOutDataSets().add(itemOutDataSet);
		itemOutController.updateFinancialTextField();
	
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
		
		
		try {
			ResultSet searchResult = mainStatement.executeQuery(cmd);
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
			
			String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + category + ".sqlite";
			
			Statement stm;
			try {
				String dbPath = "./database/";
				File dir = new File(dbPath);
				if ( !dir.exists() ) {
					dir.mkdirs();
				}
				Connection connection = DriverManager.getConnection(path);
				stm = connection.createStatement();
				
				String cmd = "SELECT " + ApplicationFactory.SUB_CATEGORY_COLUMN_NAME + " FROM " + category;
				
				ResultSet res = stm.executeQuery(cmd);
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

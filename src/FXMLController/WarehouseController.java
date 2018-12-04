package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import factory.ApplicationFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class WarehouseController implements Initializable {
	
	@FXML
	private TextField itemIdTf, itemNameTf;
	
	@FXML
	private ComboBox categoryCb, subCategoryCb;
	
	@FXML
	private Button searchBtn, clearBtn;
	
	@FXML
	private TableView table;
	
	private Statement statement;
	private Statement categoryStatement;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initilizeDatabaseConnection();
		loadCategory();
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
	
	private void initilizeDatabaseConnection() {
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
	
	public void searchData() {
		String itemId = itemIdTf.getText().trim();
		String itemName = itemNameTf.getText().trim();
		String category = categoryCb.getSelectionModel().getSelectedItem().toString();
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
					subs.add(res.getString(ApplicationFactory.SUB_CATEGORY_COLUMN_NAME));
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}

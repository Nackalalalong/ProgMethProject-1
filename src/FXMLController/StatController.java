package FXMLController;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.DateThai;
import factory.ApplicationFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

public class StatController implements Initializable{
	
	@FXML
	private ComboBox xCb, yCb, itemNameCb;
		
	@FXML
	private LineChart chart;
	
	@FXML
	private NumberAxis yAxis;
	
	@FXML
	private CategoryAxis xAxis;
	
	private Statement statement;
	
	private XYChart.Series totalSellSeries, totalProfitSeries;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		try {
			initializeDatabaseConnection();
			initializeAxisComboBox();
			initializeItemNameComboBox();
			createChart();
			showChart();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupSeries() {
		chart.getData().clear();
		totalSellSeries = new XYChart.Series<>();
		totalProfitSeries = new XYChart.Series<>();
		
		totalSellSeries.setName("ยอดขาย");
		totalProfitSeries.setName("กำไร");
		
	}
	
	public void onItemNameChange() throws SQLException {
		createChart();
		showChart();
	}
	
	public void onYAxisChange() throws SQLException {
		createChart();
		showChart();
	}
	
	private void showChart() {
		String yItem = yCb.getSelectionModel().getSelectedItem().toString();
		
		if ( yItem.equals("ทั้งหมด") ) {
			chart.getData().add(totalSellSeries);
			chart.getData().add(totalProfitSeries);
		}
		
		else  if ( yItem.equals("ยอดขาย") ) {
			chart.getData().add(totalSellSeries);
		}
		else if ( yItem.equals("กำไร")){
			chart.getData().add(totalProfitSeries);
		}
	}
	
	private void createChart() throws SQLException {
		
		String itemName = itemNameCb.getSelectionModel().getSelectedItem().toString();
		
		String where = "";
		if ( !itemName.equals("ทั้งหมด") ) {
			where = " WHERE " + ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME + " = '" + itemName + "'";
		}
		
		String cmd = "SELECT " + ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_ITEM_ID_COLUMN_NAME + ", " + 
				ApplicationFactory.STATISTICS_DATABASE_ITEM_SERIAL_NUMBER_COLUMN_NAME + ", " +
				"sum(" + ApplicationFactory.STATISTICS_DATABASE_TOTAL_SELL_COLUMN_NAME + "), " +
				"sum(" + ApplicationFactory.STATISTICS_DATABASE_TOTAL_PROFIT_COLUMN_NAME + "), " +
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + " FROM " +
				ApplicationFactory.STATISTICS_DATABASE_NAME + where + " ORDER BY " + 
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + " ASC, " + 
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + " ASC";
		
		
		ResultSet res = statement.executeQuery(cmd);
		res.next();
		
		setupSeries();
		
		int countMonth = 1;
		while ( true ) {
			int month = res.getInt(ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME);
			if ( month == countMonth ) {
				double totalSell = Double.parseDouble(res.getString(4));  //select sum(totalsell) มาเป็นอันดับที่ 4 จาก cmd
				double totalProfit = Double.parseDouble(res.getString(5));
				if ( month == 6 ) {
					System.out.println("reach");
					int year = res.getInt(ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME);
					totalSellSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(month) + "\n\n" + (year + 543), totalSell));
					totalProfitSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(month) + "\n\n" + (year + 543), totalProfit));
				}
				else {
					totalSellSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(month), totalSell));
					totalProfitSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(month), totalProfit));
				}
				if ( !res.next() ) {
					break;
				}
			}
			else {
				if ( countMonth == 6 ) {
					int year = res.getInt(ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME);
					totalSellSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(countMonth)+ "\n\n" + (year + 543), 0));
					totalProfitSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(countMonth)+ "\n\n" + (year + 543), 0));
				}
				else {
					totalSellSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(countMonth), 0));
					totalProfitSeries.getData().add(new XYChart.Data<>(DateThai.getThaiAbrevMonth(countMonth), 0));
				}
			}
			countMonth++;
			if ( countMonth > 12 ) {
				countMonth = 1;
			}
		}
		
	}
	
	private void initializeDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.STATISTICS_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		statement = connection.createStatement();
	}
	
	private void initializeAxisComboBox() throws SQLException {
		ObservableList<String> yItems = FXCollections.observableArrayList();
		yItems.add("ทั้งหมด");
		yItems.add("ยอดขาย");
		yItems.add("กำไร");
	
		yCb.setItems(yItems);
		yCb.getSelectionModel().select(0);
		
		ObservableList<String> xItems = FXCollections.observableArrayList();
		xItems.add("1 เดือน");
		//xItems.add("ไตรมาส");
	
		xCb.setItems(xItems);
		xCb.getSelectionModel().select(0);
		
	}

	private void initializeItemNameComboBox() throws SQLException {
		ObservableList<String> itemNames = FXCollections.observableArrayList();
		itemNames.add("ทั้งหมด");
	
		String cmd = "SELECT DISTINCT " + ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME + " FROM " + 
				ApplicationFactory.STATISTICS_DATABASE_NAME;
		
		ResultSet res = statement.executeQuery(cmd);
		
		while ( res.next() ) {
			String itemName = res.getString(ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME);
			itemNames.add(itemName);
		}
		
		itemNameCb.setItems(itemNames);
		itemNameCb.getSelectionModel().select(0);
	}
	

}

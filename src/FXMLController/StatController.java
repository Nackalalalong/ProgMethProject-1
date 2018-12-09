package FXMLController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.DateThai;
import application.HoverNode;
import factory.ApplicationFactory;
import factory.DatabaseCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	
	@FXML
	private Button reloadBtn;
	
	private Statement statisticsStatement;
	
	private XYChart.Series totalSellSeries, totalProfitSeries;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		chart.setAnimated(false);
		statisticsStatement = DatabaseCenter.getStatisticsStatement();
		
		try {
			initializeAxisComboBox();
			initializeItemNameComboBox();
			//createChart();
			//showChart();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			showErrorDialog("มีบางอย่างผิดพลาดขณะกำลังเชื่อมต่อฐานข้อมูล", "กรุณาลองใหม่ภายหลัง");
			e.printStackTrace();
		}
		
	}
	
	public void reloadData() {
		try {
			yCb.getItems().clear();
			xCb.getItems().clear();
			itemNameCb.getItems().clear();
			initializeAxisComboBox();
			initializeItemNameComboBox();
			createChart();
			showChart();
		}
		catch (SQLException e) {
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
	
	public void showErrorDialog(String header, String message) {
		Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		error.setTitle("ข้อผิดพลาด");
		error.setHeaderText(header);
		error.show();
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
				ApplicationFactory.STATISTICS_DATABASE_NAME + where + " GROUP BY " +
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + ", " +
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + " ORDER BY " + 
				ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME + " ASC, " + 
				ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME + " ASC";
		
		//System.out.println(cmd);
		
		ResultSet res = statisticsStatement.executeQuery(cmd);
		if ( !res.next() ) {	//ตอนเปิดโปรแกรมครั้งแรกไม่มีไฟลื stat database จะ error เด้ง alert
			return ;
		}
		
		int countYear = res.getInt(ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME);
		int countMonth = res.getInt(ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME);
		
		setupSeries();
		boolean needFirstMarkYear = false;
		boolean needToBreak = false;
		boolean resEnd = false;
		if ( countMonth > 6 ) {
			needFirstMarkYear = true;
		}
		
		int nowYear  = DateThai.getCurrentYear();
		int nowMonth = DateThai.getCurrentMonthNumber();
				
		while ( countYear <= nowYear &&  countMonth <= nowMonth) {
			
			int month = countMonth;
			int year = countYear;
			if ( !resEnd ) {
				 month = res.getInt(ApplicationFactory.STATISTICS_DATABASE_MONTH_COLUMN_NAME);
				 year = res.getInt(ApplicationFactory.STATISTICS_DATABASE_YEAR_COLUMN_NAME);
			}
			String mon = DateThai.getThaiAbrevMonth(countMonth);
			double sellValue = 0.0;
			double profitValue = 0.0;
			
			for(int i=0; i<countYear; i++) mon+="\0";   //make x axis data unique
			
			if ( month == countMonth && year == countYear) {
				if ( !resEnd ) {
					sellValue = Double.parseDouble(res.getString(4));  //select sum(totalsell) มาเป็นอันดับที่ 4 จาก cmd
					profitValue = Double.parseDouble(res.getString(5));
				}
				//sellValue = totalSell;
				//profitValue = totalProfit;
				
				if ( !resEnd ) {
					if ( !res.next() ) {
						/*if ( ( countMonth < 6 || needFirstMarkYear ) && totalSellSeries.getData().size() > 0) {
							XYChart.Data lastData = (XYChart.Data)(totalSellSeries.getData().get((int)(totalSellSeries.getData().size()-1)/2));
							lastData.setXValue(lastData.getXValue().toString() + "\n\n" + (countYear + 543));
							lastData = (XYChart.Data)(totalProfitSeries.getData().get((int)(totalProfitSeries.getData().size()-1)/2));
							lastData.setXValue(lastData.getXValue().toString() + "\n\n" + (countYear + 543));
						}*/
						resEnd = true;
						//needToBreak = true;
					}
				}
				
			}
			
			if ( countMonth == 6 ) {
				mon += "\n\n" + ( countYear + 543 );
			}
			
			//System.out.println(month+" "+countMonth+" "+year+" "+countYear);
			XYChart.Data sellData = new XYChart.Data<>(mon, sellValue);		// ให้แสดงค่าที่จุดตอน mouse hover
			XYChart.Data profitData = new XYChart.Data<>(mon, profitValue);
			sellData.setNode(new HoverNode(sellValue + ""));
			profitData.setNode(new HoverNode(profitValue + ""));
			totalSellSeries.getData().add(sellData);
			totalProfitSeries.getData().add(profitData);
			
			if ( needToBreak ) {
				break;
			}
			
			if ( countMonth >= 12 ) {
				if ( needFirstMarkYear ) {
						XYChart.Data lastData = (XYChart.Data)(totalSellSeries.getData().get((int)(totalSellSeries.getData().size()-1)/2));
						lastData.setXValue(lastData.getXValue().toString() + "\n\n" + (countYear + 543));
						lastData = (XYChart.Data)(totalProfitSeries.getData().get((int)(totalProfitSeries.getData().size()-1)/2));
						lastData.setXValue(lastData.getXValue().toString() + "\n\n" + (countYear + 543));
					
					needFirstMarkYear = false;
				}
				countMonth = 0; // must be ++;
				countYear++;
			}
			
			countMonth++;

		}
		
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
		
		ResultSet res = statisticsStatement.executeQuery(cmd);
		
		while ( res.next() ) {
			String itemName = res.getString(ApplicationFactory.STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME);
			itemNames.add(itemName);
		}
		
		itemNameCb.setItems(itemNames);
		itemNameCb.getSelectionModel().select(0);
	}
	

}

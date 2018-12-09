package factory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseCenter {
	
	private static Statement mainStatement;
	private static Statement categoryStatement;
	private static Statement customerStatement;
	private static Statement billStatement;
	private static Statement statisticsStatement;
	
	static {
		 try {
			initializeMainDatabaseConnection();
			initializeCategoryDatabaseConnection();
			initializeCustomerDatabseConnection();
			initializeStatisticsDatabaseConnection();
			initializeBillsDatabaseConnection();
			prepareMainDatabase();
			prepareCategoryDatabase();
			prepareCustomerDatabase();
			prepareStatisticsDatabase();
			prepareBillsDatabase();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}

	public static Statement getMainStatement() {
		return mainStatement;
	}

	public static Statement getCategoryStatement() {
		return categoryStatement;
	}

	public static Statement getCustomerStatement() {
		return customerStatement;
	}

	public static Statement getBillStatement() {
		return billStatement;
	}

	public static Statement getStatisticsStatement() {
		return statisticsStatement;
	}
	
	private static void prepareCustomerDatabase() throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.CUSTOMER_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME + " TEXT, " +
				ApplicationFactory.CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME + " INTEGER, " + 
				ApplicationFactory.CUSTOMER_DATABASE_LASTEST_BUY_DATE_COLUMN_NAME + " TEXT, " +
				ApplicationFactory.CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME + " REAL, " +
				ApplicationFactory.CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME + " REAL, " +
				ApplicationFactory.CUSTOMER_DATABASE_NOTE_COLUMN_NAME + " TEXT)";
		
		customerStatement.execute(cmd);
	}
	
	private static void prepareStatisticsDatabase() throws SQLException {
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
	}
	
	private static void prepareCategoryDatabase() throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.CATEGORY_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.CATEGORY_DATABASE_COLUMN_NAME + " TEXT)";
		
		categoryStatement.execute(cmd);
	}
	
	private static void prepareBillsDatabase() throws SQLException {
		String cmd = "CREATE TABLE IF NOT EXISTS " + ApplicationFactory.BILL_DATABASE_NAME + "(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " TEXT, " +
				ApplicationFactory.BILL_DATABASE_BILL_DATE_COLUMN_NAME + " TEXT, " + 
				ApplicationFactory.BILL_DATABASE_CUSTOMER_NAME_COLUMN_NAME + " TEXT, " +
				ApplicationFactory.BILL_DATABASE_NOTE_COLUMN_NAME + " TEXT)";
		
		billStatement.execute(cmd);
	}
	
	private static void prepareMainDatabase() throws SQLException {
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
	
			mainStatement.execute(cmd);
	}

	public static Statement getSubCategoryDatabaseConnection(String categoryDatabaseName) throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + categoryDatabaseName + ".sqlite";
		
		Statement stm;
		String dbPath = "./database/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		Statement statement = connection.createStatement();
		
		return statement;
	}

	private static void initializeMainDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.MAIN_DATABASE_FILE_NAME + ".sqlite";
		
			String dbPath = "./database/";
			File dir = new File(dbPath);
			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			Connection connection = DriverManager.getConnection(path);
			mainStatement = connection.createStatement();

		
	}

	private static void initializeCategoryDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CATEGORY_DATABASE_NAME + ".sqlite";
		
			String dbPath = "./database/";
			File dir = new File(dbPath);
			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			Connection connection = DriverManager.getConnection(path);
			categoryStatement = connection.createStatement();

	}
	
	private static void initializeCustomerDatabseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.CUSTOMER_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		customerStatement = connection.createStatement();
	}
	
	private static void initializeBillsDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.BILL_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		billStatement = connection.createStatement();
	}
	
	private static void initializeStatisticsDatabaseConnection() throws SQLException {
		String path = "jdbc:sqlite:" + "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY + "/" + ApplicationFactory.STATISTICS_DATABASE_NAME + ".sqlite";
		
		String dbPath = "./" + factory.ApplicationFactory.MAIN_DATABASE_DIRECTORY +"/";
		File dir = new File(dbPath);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		Connection connection = DriverManager.getConnection(path);
		statisticsStatement = connection.createStatement();
	}

}

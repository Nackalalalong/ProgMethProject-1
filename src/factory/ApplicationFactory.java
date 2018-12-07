package factory;

public final class ApplicationFactory {

	public static final String MAIN_DATABASE_DIRECTORY = "database";
	public static final String MAIN_DATABASE_FILE_NAME = "mainDatabase";
	public static final String MAIN_DATEBASE_NAME = "stock";
	public static final String MAIN_DATABASE_ITEM_NAME = "itemname";
	public static final String MAIN_DATABASE_ITEM_ID = "itemid";
	public static final String MAIN_DATABASE_ITEM_SERIAL_NUNBER = "sn";
	public static final String MAIN_DATABASE_ITEM_UNIT = "unit";
	public static final String MAIN_DATABASE_ITEM_BUY_PRICE = "buyprice";
	public static final String MAIN_DATABASE_ITEM_SELL_PRICE = "sellprice";
	public static final String MAIN_DATABASE_ITEM_AMOUNT = "amount";
	public static final String MAIN_DATABASE_ITEM_CATEGORY = "category";
	public static final String MAIN_DATABASE_ITEM_SUBCATEGORY = "subcategory";
	public static final String MAIN_DATABASE_ITEM_NOTE = "note";
	
	public static final String MAIN_DATABASE_IMAGE_DIRECTORY = "images";
	
	public static final String CATEGORY_DATABASE_NAME = "categories";
	public static final String CATEGORY_DATABASE_COLUMN_NAME = "category";
	
	public static final String SUB_CATEGORY_COLUMN_NAME = "subcategory";

	
	//table
	public static final String  WAREHOUSE_TABLE_IMAGE_COLUMN_NAME = "รูป";
	public static final String	WAREHOUSE_TABLE_ITEM_ID_COLUMN_NAME = "รหัสสินค้า";
	public static final String	WAREHOUSE_TABLE_ITEM_SERIAL_NUMBER_COLUMN_NAME = "serial number"; 
	public static final String	WAREHIUSE_TABLE_ITEM_NAME_COLUMN_NAME = "ชื่อสินค้า";
	public static final String	WAREHOUSE_TABLE_UNIT_COLUMN_NAME = "หน่วย";
	public static final String	WAREHOUSE_TABLE_AMOUNT_COLUMN_NAME = "จำนวน";
	public static final String 	WAREHOUSE_TABLE_BUY_PRICE_COLUMN_NAME = "ราคาซื้อ";
	public static final String	WAREHOUSE_TABLE_SELL_PRICE_COLUMN_NAME = "ราคาขาย";
	public static final String	WAREHOUSE_TABLE_CATEGORY_COLUMN_NAME = "หมวดหมู่";
	public static final String	WAREHOUSE_TABLE_SUB_CATEGORY_COLUMN_NAME = "หมวดหมู่ย่อย";
	public static final String	WAREHOUSE_TABLE_NOTE_COLUMN_NAME = "หมายเหตุ";

	//item out
	public static final String DEFAULT_BILL_DIRECTORY_NAME = "bills";
	public static final String CUSTOMER_DATABASE_NAME = "customers";
	public static final String STATISTICS_DATABASE_NAME = "statistics";
	
	//customer
	public static final String CUSTOMER_DATABASE_CUSTOMER_NAME_COLOUMN_NAME = "customername";
	public static final String CUSTOMER_DATABASE_BUY_AMOUNT_COLUMN_NAME = "buyAmount";
	public static final String CUSTOMER_DATABASE_LASTEST_BUY_DATE_COLUMN_NAME = "lastestbuydate";
	public static final String CUSTOMER_DATABASE_TOTAL_BUY_COLUMN_NAME = "totalbuy";
	public static final String CUSTOMER_DATABASE_TOTAL_PROFIT_COLUMN_NAME = "totalprofit";
	public static final String CUSTOMER_DATABASE_NOTE_COLUMN_NAME = "note";
	
	//statistics
	public static final String STATISTICS_DATABASE_ITEM_NAME_COLUMN_NAME = "itemname";
	public static final String STATISTICS_DATABASE_ITEM_ID_COLUMN_NAME = "itemid";
	public static final String STATISTICS_DATABASE_ITEM_SERIAL_NUMBER_COLUMN_NAME = "sn";
	public static final String STATISTICS_DATABASE_SELL_AMOUNT_COLUMN_NAME = "sellamount";
	public static final String STATISTICS_DATABASE_TOTAL_SELL_COLUMN_NAME = "totalsell";
	public static final String STATISTICS_DATABASE_TOTAL_PROFIT_COLUMN_NAME = "totalprofit";
	public static final String STATISTICS_DATABASE_MONTH_COLUMN_NAME = "month";	// 1-12
	public static final String STATISTICS_DATABASE_YEAR_COLUMN_NAME = "year"; // คศ
	
	//bill
	public static final String BILL_DATABASE_NAME = "bills";
	public static final String BILL_DATABASE_BILL_ID_COLUMN_NAME = "billid";
	public static final String BILL_DATABASE_BILL_DATE_COLUMN_NAME = "billdate";
	public static final String BILL_DATABASE_CUSTOMER_NAME_COLUMN_NAME = "customername";
	public static final String BILL_DATABASE_NOTE_COLUMN_NAME = "note";
	
}

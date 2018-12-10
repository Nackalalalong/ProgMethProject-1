package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.DateThai;

public final class BillsIdGenerator {
	
	public static String getBillId() {
		Statement billStatement = DatabaseCenter.getBillStatement();
		
		int year = DateThai.getCurrentYear();
		String prefixYear = (year+"").substring(2);
		
		
		
		try {
			String cmd = "SELECT " + ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " FROM " +
					ApplicationFactory.BILL_DATABASE_NAME + " WHERE " +
					ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " LIKE '" + prefixYear + "%'" + " ORDER BY " +
					ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME + " DESC ";
			
			ResultSet res = billStatement.executeQuery(cmd);
			res.next();
			
			String max = res.getString(ApplicationFactory.BILL_DATABASE_BILL_ID_COLUMN_NAME);
			return ( Integer.parseInt(max) + 1 ) + "" ;
			
		}
		catch( SQLException e) {
			return prefixYear + "00000"; 
		}
		
	}

}

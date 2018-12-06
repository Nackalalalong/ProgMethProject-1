package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateThai {

	private DateThai() {
		
	}
	
	public static String getCurrentThaiDate() {
		String Months[] = {
			      "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน",
			      "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม",
			      "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
		
		String abrevMonths[] = {
			      "ม.ค", "ก.พ", "มี.ค", "เม.ย",
			      "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
			      "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};
			
		int year=0,month=0,day=0;
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);  	
		
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
	
		return String.format("%s %s %s", day,Months[month],year+543);
	}
	
}

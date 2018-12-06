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
			      "���Ҥ�", "����Ҿѹ��", "�չҤ�", "����¹",
			      "����Ҥ�", "�Զع�¹", "�á�Ҥ�", "�ԧ�Ҥ�",
			      "�ѹ��¹", "���Ҥ�", "��Ȩԡ�¹", "�ѹ�Ҥ�"};
		
		String abrevMonths[] = {
			      "�.�", "�.�", "��.�", "��.�",
			      "�.�", "��.�", "�.�", "�.�",
			      "�.�", "�.�", "�.�", "�.�"};
			
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

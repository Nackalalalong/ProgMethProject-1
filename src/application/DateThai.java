package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateThai {
	
	private static Calendar c;
	private static String Months[] = {
		      "���Ҥ�", "����Ҿѹ��", "�չҤ�", "����¹",
		      "����Ҥ�", "�Զع�¹", "�á�Ҥ�", "�ԧ�Ҥ�",
		      "�ѹ��¹", "���Ҥ�", "��Ȩԡ�¹", "�ѹ�Ҥ�"};
	
	private static String  abrevMonths[] = {
		      "�.�", "�.�", "��.�", "��.�",
		      "�.�", "��.�", "�.�", "�.�",
		      "�.�", "�.�", "�.�", "�.�"};
		
	
	static {
		Date date = new Date();
		c = Calendar.getInstance();
		c.setTime(date);
	}

	private DateThai() {
		
	}
	
	public static String getThaiAbrevMonth(int month) {
		return abrevMonths[month-1];
	}
	
	public static String getCurrentThaiDate() {
		
		int year=0,month=0,day=0;
		  	
		
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
	
		return String.format("%s %s %s", day,Months[month],year+543);
	}
	
	public static String getNumberDateFormat() {
		int day = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		
		return day + "-" + month + "-" + year;
	}
	
	public static String numberDateToThaiDate(String date) {
		String[] dmy = date.trim().split("-");
		String month = Months[Integer.parseInt(dmy[1]) - 1];
		int year = Integer.parseInt(dmy[2]) + 543;
		
		return dmy[0] + " " + month + " " + year;
	}
	
	public static int getCurrentMonthNumber() {
		return c.get(Calendar.MONTH);
	}
	
	public static int getCurrentYear() {
		return c.get(Calendar.YEAR);
	}
}

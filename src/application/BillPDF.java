package application;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import dataModel.ItemOutDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class BillPDF {
	private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	private int billNum ;
	private boolean isFirst;
	private double totalAmount;
		

	 
	public static void printPDF(ObservableList<ItemOutDataSet> items, String customerName, String date, String billNum, String path , String discountBath, String taxBath, String price, String netPrice) {
		boolean isFirst = true;
		double totalAmount = 0;
		
		try {

			PdfReader pdfTemplate = new PdfReader(ClassLoader.getSystemResource("Draft.pdf"));
			FileOutputStream fileOutputStream = new FileOutputStream(path);   // ใช้ path แทน รวม .pdf ไวเในชื่อแล้ว

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(pdfTemplate, fileOutputStream);
			stamper.setFormFlattening(true);
			/////////////////////////////////////////////////////////////////////
			int order = 1;
			for(ItemOutDataSet e : items) {
				if(isFirst) {
					stamper.getAcroFields().setField("order", order + "");
					stamper.getAcroFields().setField("name", e.getItemId() + "   " + e.getItemName());
					stamper.getAcroFields().setField("amount", e.getSellAmount());
					stamper.getAcroFields().setField("pricePer", e.getSellPrice());
					stamper.getAcroFields().setField("sum", e.getTotalPrice());
					
				}
				else {
					stamper.getAcroFields().setField("order", "\n" + order + "");
					stamper.getAcroFields().setField("name", "\n" + e.getItemId() + "   " + e.getItemName());
					stamper.getAcroFields().setField("amount", "\n" + e.getSellAmount());
					stamper.getAcroFields().setField("pricePer", "\n" + e.getSellPrice());
					stamper.getAcroFields().setField("sum", "\n" + e.getTotalPrice());
				}
				totalAmount += Double.parseDouble(e.getSellPrice());
				isFirst = false;
				order++;
			}
			if(!discountBath.equals("")) {
				totalAmount -= Double.parseDouble(discountBath);
				stamper.getAcroFields().setField("sum", "\nส่วนลด " + discountBath);
			}
			stamper.getAcroFields().setField("sumTotal", price + "");
			//double tax = (Double.parseDouble(taxPercent)/100) * totalAmount;
			stamper.getAcroFields().setField("tax",  taxBath);
			stamper.getAcroFields().setField("net", String.valueOf(netPrice));
			//////////////////////////////////////////////////////////////////////////////
			stamper.close();
			out.close();
		}catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Print bill done");
	}
	
	public static void readPDF(String fileName) {
		if(Desktop.isDesktopSupported()){
			try {
				File myFile = new File(fileName + ".pdf");
				Desktop.getDesktop().open(myFile);
			
			}catch(Exception e) {
				e.printStackTrace();
			
			}
		}
	}
	

}

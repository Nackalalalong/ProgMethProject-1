package factory;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import dataModel.ItemOutDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;


public class BillPDF {
	private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	private int billNum ;
	private double totalAmount;
		

	 
	public static void printPDF(ObservableList<ItemOutDataSet> items, String customerName, String date, String billNum, String path , String discountBath, String taxBath, String price, String netPrice) {
		boolean isFirst = true;
		double totalAmount = 0;
		
		try {

			PdfReader pdfTemplate = new PdfReader(ClassLoader.getSystemResource("Draft.pdf"));
			FileOutputStream fileOutputStream = new FileOutputStream(path);   // เน�เธ�เน� path เน�เธ—เธ� เธฃเธงเธก .pdf เน�เธงเน€เน�เธ�เธ�เธทเน�เธญเน�เธฅเน�เธง

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/Angsana New.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			PdfStamper stamper = new PdfStamper(pdfTemplate, fileOutputStream);
			stamper.setFormFlattening(true);
			/////////////////////////////////////////////////////////////////////
			stamper.getAcroFields().setField("customerName", customerName);
			stamper.getAcroFields().setField("billNum", billNum);
			stamper.getAcroFields().setField("date", date);
			int order = 1;
			
			for(ItemOutDataSet e : items) {
				String detail = "ID:" + e.getItemId() + " S/N:" + e.getItemSn() + " " + e.getItemName();
				if(isFirst) {
					stamper.getAcroFields().setField("order", order + "");
					stamper.getAcroFields().setField("name", detail);
					stamper.getAcroFields().setField("amount", e.getSellAmount());
					stamper.getAcroFields().setField("pricePer", e.getSellPrice());
					stamper.getAcroFields().setField("sum", e.getTotalPrice());
					
				}
				else {
					stamper.getAcroFields().setField("order", "\n" + order + "");
					stamper.getAcroFields().setField("name", detail);
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
			}
			stamper.getAcroFields().setField("sumTotal", price + "");
			//double tax = (Double.parseDouble(taxPercent)/100) * totalAmount;
			stamper.getAcroFields().setField("discount",  discountBath);
			stamper.getAcroFields().setField("tax",  taxBath);
			stamper.getAcroFields().setField("net", String.valueOf(netPrice));
			//////////////////////////////////////////////////////////////////////////////
			stamper.close();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		System.out.println("Print bill done");
	}
	
	public static void readPDF(String fileName) {
		if(Desktop.isDesktopSupported()){
			try {
				File myFile = new File(fileName);
				Desktop.getDesktop().open(myFile);
			
			}catch(Exception e) {
				e.printStackTrace();
			
			}
		}
	}
	

}

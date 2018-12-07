package application;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.InputStream;

import dataModel.ItemOutDataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

<<<<<<< HEAD:src/application/BillPDF.java
public class BillPDF {
	private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	int billNum ;
	boolean isFirst = true;
	double totalAmount = 0;
	public BillPDF() {
		
	}
||||||| merged common ancestors
public class BillPFD {
	private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	int billNum ;
	boolean isFirst = true;
	double totalAmount = 0;
=======
public class BillPDF {
	//private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	//int billNum ;
>>>>>>> 862b082edfe3c1e95b958af8bea83f44651861dc:src/application/BillPDF.java
	
<<<<<<< HEAD:src/application/BillPDF.java
	public BillPDF(ObservableList<ItemOutDataSet> items, int billNum) {
||||||| merged common ancestors
	public BillPFD(ObservableList<ItemOutDataSet> items, int billNum) {
=======
	/*public BillPFD(ObservableList<ItemOutDataSet> items, int billNum) {
>>>>>>> 862b082edfe3c1e95b958af8bea83f44651861dc:src/application/BillPDF.java
		this.items = items;
<<<<<<< HEAD:src/application/BillPDF.java
		this.billNum = billNum;	
||||||| merged common ancestors
		this.billNum = billNum;
		
=======
		this.billNum = billNum;
		
	}*/
	
	private BillPDF() {
		
>>>>>>> 862b082edfe3c1e95b958af8bea83f44651861dc:src/application/BillPDF.java
	}
																				  // path ÁÕá¤è directory àªè¹ /bills/ ÂÑ§äÁèÃÇÁª×èÍä¿Åì		          // price ÃÒ¤ÒÃÇÁ·Ñé§ËÁ´ÂÑ§äÁèÅ´
	public static void printPDF(ObservableList<ItemOutDataSet> items, int billNum, String path , String discountBath, String taxPercent, String price, String netPrice) {
		boolean isFirst = true;
		double totalAmount = 0;
		
		try {
<<<<<<< HEAD:src/application/BillPDF.java
			PdfReader pdfTemplate = new PdfReader("Draft.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream(billNum + ".pdf");
||||||| merged common ancestors
			PdfReader pdfTemplate = new PdfReader("Draft.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream("test.pdf");
=======
			PdfReader pdfTemplate = new PdfReader(ClassLoader.getSystemResource("Draft.pdf"));
			FileOutputStream fileOutputStream = new FileOutputStream("test.pdf");
>>>>>>> 862b082edfe3c1e95b958af8bea83f44651861dc:src/application/BillPDF.java
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
			stamper.getAcroFields().setField("sumTotal", totalAmount + "");
			double tax = (Double.parseDouble(taxPercent)/100) * totalAmount;
			stamper.getAcroFields().setField("tax",  String.valueOf(tax));
			stamper.getAcroFields().setField("net", String.valueOf(tax + totalAmount));
			//////////////////////////////////////////////////////////////////////////////
			stamper.close();
			out.close();
		}catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Print bill done");
	}
	
	public void readPDF(String fileName) {
		if(Desktop.isDesktopSupported()){
			try {
				File myFile = new File(fileName + ".pdf");
				Desktop.getDesktop().open(myFile);
			
			}catch(Exception e) {
				System.out.println(e);
			
			}
		}
	}
	

}

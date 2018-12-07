package application;

import java.io.ByteArrayOutputStream;
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

public class BillPDF {
	//private ObservableList<ItemOutDataSet> items = FXCollections.observableArrayList();
	//int billNum ;
	
	/*public BillPFD(ObservableList<ItemOutDataSet> items, int billNum) {
		this.items = items;
		this.billNum = billNum;
		
	}*/
	
	private BillPDF() {
		
	}
																				  // path มีแค่ directory เช่น /bills/ ยังไม่รวมชื่อไฟล์		          // price ราคารวมทั้งหมดยังไม่ลด
	public static void printPDF(ObservableList<ItemOutDataSet> items, int billNum, String path , String discountBath, String taxPercent, String price, String netPrice) {
		boolean isFirst = true;
		double totalAmount = 0;
		
		try {
			PdfReader pdfTemplate = new PdfReader(ClassLoader.getSystemResource("Draft.pdf"));
			FileOutputStream fileOutputStream = new FileOutputStream("test.pdf");
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
			stamper.close();
			out.close();
		}catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Print bill done");
	}

}

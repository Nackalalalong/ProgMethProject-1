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
	int billNum ;
	boolean isFirst = true;
	double totalAmount = 0;
		
	 
	private BillPDF() {
		
	}
																				  // path ร�ร•รกยครจ directory ร ยชรจยน /bills/ ร�ร‘ยงรคร�รจร�ร�ร�ยชร—รจร�รคยฟร…รฌ		          // price ร�ร’ยคร’ร�ร�ร�ยทร‘รฉยงร�ร�ยดร�ร‘ยงรคร�รจร…ยด
	public static void printPDF(ObservableList<ItemOutDataSet> items, int billNum, String path , String discountBath, String taxPercent, String price, String netPrice) {
		boolean isFirst = true;
		double totalAmount = 0;
		
		try {

			PdfReader pdfTemplate = new PdfReader("Draft.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream(billNum + ".pdf");

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

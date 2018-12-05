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

public class billPFDF {
	
	public billPFDF() {
		
	}
	public void printPDF() {
		try {
			//java.io.InputStream docA = getClass().getResourceAsStream();
			PdfReader pdfTemplate = new PdfReader("Draft.pdf");
			FileOutputStream fileOutputStream = new FileOutputStream("test.pdf");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(pdfTemplate, fileOutputStream);
			//Document document = new Document();
			stamper.setFormFlattening(true);
			stamper.getAcroFields().setField("order", "1\n2");
			stamper.getAcroFields().setField("name", "WowZa\nAHA");
			stamper.getAcroFields().setField("amount", "2\n99");
			stamper.getAcroFields().setField("pricePer", "10000\n9999");
			double sum = 2*1000 + 99 * 9999;
			stamper.getAcroFields().setField("sum", 2*1000 + "\n"+ 99 * 9999999);
			stamper.getAcroFields().setField("sumTotal", String.valueOf(sum));
			double tax = (7*(2*1000+99 * 9999)/100);
			stamper.getAcroFields().setField("tax",  String.valueOf(tax));
			stamper.getAcroFields().setField("net", String.valueOf(tax+sum));
			//PdfWriter.getInstance(document, new FileOutputStream("d:/bills.pdf"));
			//document.open();
			//document.add(new Paragraph("WOW"));
			//document.close();
			stamper.close();
			//pdfTemplate.close();
			out.close();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		System.out.println("Print bill done");
	}

}

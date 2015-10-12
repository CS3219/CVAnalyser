package com.UI;

import java.io.IOException;
import java.io.PrintWriter;
public class ConvertPDF {
	    public static void main(String[] args) throws IOException {

	       PDFConverter pdfconverter = new PDFConverter();
	       pdfconverter.setFilePath("C:\\Users\\Ganga\\Downloads\\CVs\\Different Formats\\Donnabelle Embodo_CV_04082015.pdf");
	       PrintWriter writer = new PrintWriter("C:\\Users\\Ganga\\Downloads\\CVs\\Different Formats\\file3.txt", "UTF-8");
	       writer.println(pdfconverter.ToText());
	       writer.close();
	       System.out.println(pdfconverter.ToText());       
	    
	}
}

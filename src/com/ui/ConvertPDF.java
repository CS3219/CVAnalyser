package com.ui;

import java.io.IOException;
import java.io.PrintWriter;
public class ConvertPDF {
	    public ConvertPDF() {
	    }
	    public String convertTxtToPDF(String fileName) throws IOException{
	       PDFConverter pdfconverter = new PDFConverter();
	       pdfconverter.setFilePath(fileName);
	       String txtFilename = fileName.replace(".pdf", ".txt"); 
	       PrintWriter writer = new PrintWriter(txtFilename, "UTF-8");
	       writer.println(pdfconverter.ToText());
	       System.out.println(pdfconverter.ToText());
	       writer.close();    
	    return txtFilename;
	}
}

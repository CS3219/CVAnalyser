package com.controller;

import java.util.ArrayList;

import com.ui.ConvertPDF;
import com.ui.PDFConverter;
import com.anayser.*;
import com.parser.*;

public class Controller {
ArrayList<CVObject> cvs = new ArrayList<CVObject>();

	public Controller(){
		
	}
	
	public ArrayList<ArrayList<String>> processJobDescAndCV(String position,String eduReq,String techSkills, ArrayList<String> CV){
		CVParser cvParser = new CVParser();
		JobDescParser jobDescParser = new JobDescParser();
		Analyser analyser = new Analyser();
		ConvertPDF convert = new ConvertPDF();

		for(int i = 0; i < CV.size(); i++) {
			String fileName = convert.convertTxtToPDF(CV.get(i));
			cvs.add(cvParser.parseCV(fileName));
		}
		
		JobDescObject jobDescriptions = jobDescParser.parseJobDesc(position,eduReq,techSkills);
		ArrayList<ArrayList<String>> results = analyser.analyse(jobDescriptions, cvs);
		
		/*	System.out.println("Technical: " +techSkills);
		System.out.println("Position: " + position);
		System.out.println("Education: "+eduReq);
		System.out.println("Technical: " +techSkills);*/
		
		return results;
	}


	 
}

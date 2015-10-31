package com.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.ui.ConvertPDF;
import com.ui.PDFConverter;
import com.anayser.*;
import com.parser.*;
import com.storage.*;

public class Controller {
ArrayList<CVObject> cvs = new ArrayList<CVObject>();

	public Controller(){
		
	}
	
	public ArrayList<ArrayList<String>> processJobDescAndCV(String position,String eduReq,String techSkills, ArrayList<String> CV){
		CVParser cvParser = new CVParser();
		JobDescParser jobDescParser = new JobDescParser();
		Analyser analyser = new Analyser();
		ConvertPDF convert = new ConvertPDF();

		cvs.clear();
		for(int i = 0; i < CV.size(); i++) {
			String fileName = null;
			try {
				fileName = convert.convertTxtToPDF(CV.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public JobDescObject parseJobDesc(String jobDescFileName){
		JobDescParser jobDescParser = new JobDescParser();
		ConvertPDF convert = new ConvertPDF();
		ArrayList<JobDescObject> jobDescs = new ArrayList<JobDescObject>();
		String fileName = null;
			try {
				fileName = convert.convertTxtToPDF(jobDescFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			JobDescObject jobDescriptions = jobDescParser.parseJobDesc(fileName);
			return jobDescriptions;
	}
	
	public ArrayList<CVObject> parseCVs(ArrayList<String> cvsFileName) {
		CVParser cvParser = new CVParser();
		ConvertPDF convert = new ConvertPDF();

		cvs.clear();
		for(int i = 0; i < cvsFileName.size(); i++) {
			String fileName = null;
			try {
				fileName = convert.convertTxtToPDF(cvsFileName.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cvs.add(cvParser.parseCV(fileName));
		}
		return cvs;
		
	}
	
	/*public void storeInLanguageHashMap(ArrayList<String> keyword){
		try {
			Storage storage = new Storage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void storeInSkillHashMap(ArrayList<String> keyword){
		
	}
	
	public void storeInExperienceHashMap(ArrayList<String> keyword){
		
	}*/

	public ArrayList<ArrayList<String>> analyse(JobDescObject jobdesc, ArrayList<CVObject> cvs){
		Analyser analyser = new Analyser();
		
		ArrayList<ArrayList<String>> results = analyser.analyse(jobdesc, cvs);
		return results;
		
	}
	
//	public boolean matchKeyword(String keyword) {
	//	return false;
		
//	}
	



	 
}

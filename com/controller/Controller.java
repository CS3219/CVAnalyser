package com.controller;

import java.util.ArrayList;

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
		
		for(int i = 0; i < CV.size(); i++) {
			cvs.add(cvParser.parseCV(CV.get(i)));
		}
		
		JobDescObject jobDescriptions = jobDescParser.parseJobDesc(position,eduReq,techSkills);
		ArrayList<ArrayList<String>> results = analyser.analyse(jobDescriptions, cvs);
		
		return results;
	}


	 
}

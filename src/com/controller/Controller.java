package com.controller;

import java.util.ArrayList;
import com.parser.*;
import Analyser.*;

public class Controller {
ArrayList<CVObject> cvs = new ArrayList<CVObject>();

	public Controller(){
		
	}
	
	public ArrayList<ResultDetail> processJobDescAndCV(String jobDesc, ArrayList<String> CV){
		CVParser cvParser = new CVParser();
		JobDescParser jobDescParser = new JobDescParser();
		Analyser analyser = new Analyser();
		
		for(int i = 0; i < CV.size(); i++) {
			cvs.add(cvParser.parseCV(CV.get(i)));
		}
		JobDescObject jobDescriptions = jobDescParser.parseJobDesc(jobDesc);
		
		ArrayList<ResultDetail> results = analyser.analyze(jobDescriptions, cvs);
		
		return results;
	}


	 
}

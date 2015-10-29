package com.parser;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import com.parser.Annotation;
import com.parser.CoreLabel;
import com.parser.CoreMap;
import stanfordnlp.LemmaAnnotation;
import stanfordnlp.SentencesAnnotation;
import stanfordnlp.StanfordCoreNLP;
import stanfordnlp.TextAnnotation;
import stanfordnlp.TokensAnnotation;

public class JobDescParser {
	
	private static final ArrayList<String> Headers = new ArrayList<String>();
	private static JobDescObject jobDescObject = new JobDescObject();
	private ArrayList<Integer> headerLines = new ArrayList<Integer>();
	
	public JobDescParser() {
		Headers.add(0, "Responsibilities");
		Headers.add(1,"Minimum Requirements");
		Headers.add(2, "Preferred Qualifications");
		
	}
	
	public JobDescObject parseJobDesc(String fileName) {
		//extract the lines from file
				ArrayList<String> lines = extractLines(fileName);
				sectionsExtractor Se = new sectionsExtractor();
				headerLines = Se.extractSections(lines);
				for( int i=0 ;i<lines.size(); i++) {
					String line = lines.get(i);
					lines.set(i, line.toLowerCase());
				}
				ArrayList<String > updatedLines = parseNlp(lines);
				ArrayList<String> responsibilities = (ArrayList<String>) updatedLines.subList(headerLines.get(0), headerLines.get(1)- 1);
				ArrayList<String> minReq = (ArrayList<String>) updatedLines.subList(headerLines.get(1), headerLines.get(2) - 1);
				ArrayList<String> bonusQualifications = (ArrayList<String>) updatedLines.subList(headerLines.get(2), headerLines.size()-1);
parseResponsibilities(responsibilities);
parseMinReq(minReq);
parseBonus(bonusQualifications);
		return jobDescObject;
	}
	
	private void parseResponsibilities(ArrayList<String> responsibilities) {
		
	
	}
	
	private void parseMinReq(ArrayList<String> minReq) {
		for(int i=0;i<minReq.size();i++) {
			String line = minReq.get(i);
			
		}
	}
	

	private void parseBonus(ArrayList<String> bonus) {
		
	}
	
	private ArrayList<String> parseNlp(ArrayList<String> lines) {
		Properties props = new Properties(); 
	    props.put("annotators", "tokenize, ssplit, pos, lemma"); 
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
	    
	    for(int i=0;i<lines.size();i++) {
	    String line = lines.get(i); 
	    Annotation document = pipeline.process(line);  

	    for(CoreMap sentence: document.get(SentencesAnnotation.class))
	    {    
	        for(CoreLabel token: sentence.get(TokensAnnotation.class))
	        {       
	            String word = token.get(TextAnnotation.class);      
	            String lemma = token.get(LemmaAnnotation.class); 
	            System.out.println("lemmatized version :" + lemma);
	            lines.set(i, lemma);
	        }
	    }
	    }
	    
	    return lines;
	}
   private ArrayList<String> extractLines(String fileName) {
	  
   }
   
  	
}

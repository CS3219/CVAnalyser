package com.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
//import java.util.StringTokenizer;



import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

class JobDescParser {

	private static final ArrayList<String> Headers = new ArrayList<String>();
	private static JobDescObject jobDescObject = new JobDescObject();
	//private ArrayList<Integer> headerLines = new ArrayList<Integer>();
	
	public JobDescParser() {
		Headers.add(0, "responsibilities");
		Headers.add(1,"minimum requirements");
		Headers.add(2, "preferred qualifications");
		
	}
	
	public JobDescObject parseJobDesc(String fileName) {
			
				PreProcessor pp = new PreProcessor();
				ArrayList<String> lines = pp.preprocess(fileName);
				lines.add(0, null);
				SectionExtractor Se = new SectionExtractor();
				ArrayList<SectionHeader>sections = Se.extractSections(lines, Headers);
				ArrayList<String> responsibilities, minReq, bonusQualifications, updatedLines;
				
				for(int i=1; i<sections.size(); i++) {
					SectionHeader section = sections.get(i);
					int lineNum = section.getLineNum();
					int nextLineNum; 
					if(section.getHeader().equalsIgnoreCase("responsibilities")) {
						if(i!=(sections.size()-1)) {
						nextLineNum = sections.get(i+1).getLineNum();
						 responsibilities = (ArrayList<String>) lines.subList(lineNum,nextLineNum -1);
						} else {
							 responsibilities = (ArrayList<String>) lines.subList(lineNum,lines.size()-1);
						}
						updatedLines = parseNlp(responsibilities);
						 parseResponsibilities(updatedLines);
					} else if(section.getHeader().equalsIgnoreCase("minimum requirements")) {
						if(i!=(sections.size()-1)) {
							nextLineNum = sections.get(i+1).getLineNum();
							 minReq = (ArrayList<String>) lines.subList(lineNum,nextLineNum -1);
							} else {
								 minReq = (ArrayList<String>) lines.subList(lineNum,lines.size()-1);
							}
						updatedLines = parseNlp(minReq);
						parseMinReq(updatedLines);
					} else {
						if(i!=(sections.size()-1)) {
							nextLineNum = sections.get(i+1).getLineNum();
							 bonusQualifications = (ArrayList<String>) lines.subList(lineNum,nextLineNum -1);
							} else {
								 bonusQualifications = (ArrayList<String>) lines.subList(lineNum,lines.size()-1);
							}
						updatedLines = parseNlp(bonusQualifications);
						parseBonus(updatedLines);
					}
				}
				
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
		//	if(tokens[j].matches(".*\\d+.*")) {
	}
	
	private static ArrayList<String> parseNlp(ArrayList<String> lines) {
		Properties props = new Properties(); 
	    props.put("annotators", "tokenize, ssplit, pos, lemma"); 
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
	    ArrayList<String> PARAMS = new ArrayList<String> 
        (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ", "CC"));
	    
	    for(int i=0;i<lines.size();i++) {
	    String line = lines.get(i); 
	    Annotation document = pipeline.process(line);  
	    line ="";
	    for(CoreMap sentence: document.get(SentencesAnnotation.class))
	    {    
	        for(CoreLabel token: sentence.get(TokensAnnotation.class))
	        {       
	            String word = token.get(TextAnnotation.class);      
	        	String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                //String ne = token.get(NamedEntityTagAnnotation.class); 
                //System.out.println(word+" "+pos);
                
                if (PARAMS.contains(pos)) {
                    //System.out.println("yes");
                    //System.out.println(word+pos);
                    line += word + " ";
                }     
	        }
	    }
	    lines.set(i,line);
	    }
	    return lines;
	}
}

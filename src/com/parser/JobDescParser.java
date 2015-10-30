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
	
	public static void main (String[] args) {
		//ArrayList<String> test = new ArrayList<String>();
			//	test.add("Bachelor degree in computer science or a related field.");
		//test.add("At least 1.5 year of relevant work experience with iOS application development, published apps or.");
		//parseNlp(test);
	}
	
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
				updatedLines = parseNlp(lines);
				ArrayList<String> responsibilities = (ArrayList<String>) lines.subList(headerLines.get(0), headerLines.get(1)- 1);
				//ArrayList<String> minReq = (ArrayList<String>) updatedLines.subList(headerLines.get(1), headerLines.get(2) - 1);
				//ArrayList<String> bonusQualifications = (ArrayList<String>) updatedLines.subList(headerLines.get(2), headerLines.size()-1);
//parseResponsibilities(responsibilities);
//parseMinReq(minReq);
//parseBonus(bonusQualifications);
		return jobDescObject;
	}
	
	//private void parseResponsibilities(ArrayList<String> responsibilities) {
		
	
	//}
	
	/*private void parseMinReq(ArrayList<String> minReq) {
		for(int i=0;i<minReq.size();i++) {
			String line = minReq.get(i);
			
		}
	}
	

	private void parseBonus(ArrayList<String> bonus) {
		
	}
	*/
	private static void parseNlp(ArrayList<String> lines) {
		Properties props = new Properties(); 
	    props.put("annotators", "tokenize, ssplit, pos, lemma"); 
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
	    ArrayList<String> PARAMS = new ArrayList<String> 
        (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));
	    
	    for(int i=0;i<lines.size();i++) {
	    String line = lines.get(i); 
	    Annotation document = pipeline.process(line);  

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
                    System.out.println(word+pos);
                    line += word + " ";
                }     
	        }
	    }
	    }
	}
	
   private ArrayList<String> extractLines(String fileName) {
	return null;
	  
   }
   
  	
}

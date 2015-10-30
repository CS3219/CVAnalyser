package com.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
//import java.util.StringTokenizer;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

class JobDescParser {

	private static final ArrayList<String> Headers = new ArrayList<String>();
	private static JobDescObject jobDescObject = new JobDescObject();
	//private ArrayList<Integer> headerLines = new ArrayList<Integer>();
	private static ArrayList<ParsedObject> minSkills = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> minEdu = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> minWorkExp = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> bonusSkills = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> bonusWorkExp = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> minEdu = new ArrayList<ParsedObject>();
	
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
		ArrayList<String> words = new ArrayList<String>();
		
		for(int i=0;i<minReq.size();i++) {
			String line = minReq.get(i);
			ParsedObject parsed = new ParsedObject();
			Pattern pattern = Pattern.compile(".*\\bbachelor|masters|phd|diploma\\b.*");
			Matcher matcher = pattern.matcher(line);
			
			//education line
			if(matcher.find()) {
				int index = matcher.start();
				line = line.substring(index);
				if(line.contains(",")) {
					if((line.matches(".*\\bor|and\\b.*"))) {
						if(line.matches(".*\\bor\\b.*")) {
							parsed.setType("or");
							} else {
								parsed.setType("and");
							}
						String tokens[] = line.split(",");
						for(int j=0;j<tokens.length;j++) {
							if(j!=tokens.length-1) {
								words.add(j, tokens[j]);
							} else {
								String end[] = tokens[j].split(" ");
								words.add(end[0]);
								words.add(end[1]);
							}
						}
					} else {
						String[] tokens = line.split(",");
						for(int j=0;j<tokens.length;j++) {
							words.add(tokens[j]);
						}
					}
				} else {
					words.add(line);
				}
				//work exp
			} else if() {
				
				
				
				
			//skills	
			} else {
			
			if((line.matches(".*\\bor|and\\b.*"))) {
				if(line.matches(".*\\bor\\b.*")) {
				parsed.setType("or");
				} else {
					parsed.setType("and");
				}
				if(line.contains(",")){
				String tokens[] = line.split(",");
				for(int j=0;j<tokens.length;j++) {
					if(j!=tokens.length-1) {
						words.add(j, tokens[j]);
					} else {
						String end[] = tokens[j].split(" ");
						words.add(end[0]);
						words.add(end[1]);
					}
				}
				} else {
				String[] tokens = line.split(" ");	
				words.add(tokens[0]);
				words.add(tokens[2]);
				}
			} else {
				parsed.setType("none");
				if(line.contains(",")) {
				String tokens[] = line.split(",");
				for(int j=0;j<tokens.length;j++) {
						words.add(j, tokens[j]);	
				}
			} else {
				words.add(0,line);
			}
			}
			parsed.setWords(words);
			words.clear();
			}
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

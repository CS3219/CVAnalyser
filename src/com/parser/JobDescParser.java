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

public class JobDescParser {

	private static final ArrayList<String> Headers = new ArrayList<String>();
	private static JobDescObject jobDescObject = new JobDescObject();
	//private ArrayList<Integer> headerLines = new ArrayList<Integer>();
	private static ArrayList<ParsedObject> minSkills = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> minEdu = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> minWorkExp = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> bonusSkills = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> bonusWorkExp = new ArrayList<ParsedObject>();
	private static ArrayList<ParsedObject> responsibilities = new ArrayList<ParsedObject>();
	
	public JobDescParser() {
		Headers.add(0, "responsibilities");
		Headers.add(1,"minimum requirements");
		Headers.add(2, "preferred qualifications");	
	}
	
	/*
	public static void main(String[] args) {
		JobDescParser jp = new JobDescParser();
		JobDescObject jdo = jp.parseJobDesc("C:\\Users\\x\\Documents\\CVAnalyser1\\sample\\JobDesc1.txt");
		ArrayList<ParsedObject> po = jdo.getBonusSkills();
		ArrayList<String> words;
		for(int i=0;i<po.size();i++) {
			System.out.println("Type: "+po.get(i).getType());
			words = po.get(i).getWords();
			for(int j=0;j<words.size();j++) {
				System.out.println(words.get(j));
			}
			
		}
	} */
	public JobDescObject parseJobDesc(String fileName) {
				PreProcessor pp = new PreProcessor();
				ArrayList<String> lines = pp.preprocess(fileName);
				lines.add(0, "Empty");
				SectionExtractor Se = new SectionExtractor();
				ArrayList<SectionHeader>sections = Se.extractSections(lines, Headers);
				ArrayList<String> responsibilitiesSection, minReq, bonusQualifications, updatedLines;
				//System.out.println(sections.get(0).getLineNum());
				//System.out.println(sections.get(1).getLineNum());
				for(int i=1; i<sections.size(); i++) {
					SectionHeader section = sections.get(i);
					int lineNum = section.getLineNum();
					int nextLineNum; 
					if(section.getHeader().equalsIgnoreCase(Headers.get(0))) {
						//System.out.println("Entered!");
						if(i!=(sections.size()-1)) {
						nextLineNum = sections.get(i+1).getLineNum();
						 responsibilitiesSection = new ArrayList<String> (lines.subList(lineNum + 1 ,nextLineNum));
						} else {
							 responsibilitiesSection = new ArrayList<String> (lines.subList(lineNum + 1,lines.size()));
						}
						//System.out.println("Responsibilities section lines: ");
						//for(int j=0;j<responsibilitiesSection.size();j++) {
						//System.out.println(responsibilitiesSection.get(j));
						//}
						responsibilitiesSection = separateByFullStop(responsibilitiesSection);
						updatedLines = parseNlp(responsibilitiesSection);
						 parseLines(updatedLines, section.getHeader());
					} else if(section.getHeader().equalsIgnoreCase(Headers.get(1))) {
						if(i!=(sections.size()-1)) {
							nextLineNum = sections.get(i+1).getLineNum();
							 minReq = new ArrayList<String>(lines.subList(lineNum + 1,nextLineNum));
							} else {
								 minReq = new ArrayList<String> (lines.subList(lineNum + 1,lines.size()));
							}
						//System.out.println("Min req section lines: ");
						//for(int j=0;j<minReq.size();j++) {
						//System.out.println(minReq.get(j));
						//}
						minReq = separateByFullStop(minReq);
						updatedLines = parseNlp(minReq);
						//for(int j=0;j<updatedLines.size();j++) {
						//System.out.println(updatedLines.get(j));
						//}
						parseLines(updatedLines, section.getHeader());
					} else {
						if(i!=(sections.size()-1)) {
							nextLineNum = sections.get(i+1).getLineNum();
							 bonusQualifications = new ArrayList<String> (lines.subList(lineNum + 1 ,nextLineNum));
							} else {
								 bonusQualifications = new ArrayList<String> (lines.subList(lineNum + 1 ,lines.size()));
							}
						//System.out.println("Preferred qualifications section lines: ");
						//for(int j=0;j<bonusQualifications.size();j++) {
						//System.out.println(bonusQualifications.get(j));
						//}
						bonusQualifications = separateByFullStop(bonusQualifications);
						updatedLines = parseNlp(bonusQualifications);
						parseLines(updatedLines, section.getHeader());
					}
				}
		jobDescObject.setMinEdu(minEdu);
		jobDescObject.setMinSkills(minSkills);
		jobDescObject.setMinWorkExp(minWorkExp);
		jobDescObject.setBonusSkills(bonusSkills);
		jobDescObject.setBonusWorkExp(bonusWorkExp);
		jobDescObject.setResponsibilities(responsibilities);
		return jobDescObject;
	}
	private ArrayList<String> separateByFullStop(ArrayList<String> list) {
		ArrayList<String> updated = new ArrayList<String>();
		String line = "";
		
		for(int i=0;i<list.size();i++) {
		line = line + list.get(i).trim() + " ";
		}
		String separated[] = line.split("[.]\\s+");
		for(int j=0;j<separated.length;j++) {
			updated.add(separated[j]);
		}
		return updated;
	}
		
	private void parseLines(ArrayList<String> lines, String category) {
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			ParsedObject parsed = new ParsedObject();
			ArrayList<String> words = new ArrayList<String>();
			
			Pattern patternEdu = Pattern.compile(".*\\bbachelor|masters|phd|diploma\\b.*");
			Matcher matcherEdu = patternEdu.matcher(line);
	
			Pattern patternWorkExp = Pattern.compile(".*\\d+.*");
			Matcher matcherWorkExp = patternWorkExp.matcher(line);

			//education line
			if(matcherEdu.find()) {
				int index = matcherEdu.start();
				line = line.substring(index);
				//System.out.println("Education -  " + lines.get(i));
				parseEducation(line,parsed, words);
			//work exp
			} else if(matcherWorkExp.find()) {
				int index = matcherWorkExp.start();
				line = line.substring(index);
				//System.out.println("Work Exp -  " + lines.get(i));
				parseWorkExp(line,parsed, words,category);
			//skills	
			} else {
				if(category.equalsIgnoreCase(Headers.get(0))) {
					//System.out.println("Responsibilities -  " + lines.get(i));
					parseResponsibilities(line,parsed,words);
				} else {
			//System.out.println("Skills -  " + lines.get(i));
			parseMinBonusSkills(line,parsed, words,category);
				}
			}
		}
			
	}
	
	private void parseMinBonusSkills(String line, ParsedObject parsed, ArrayList<String> words, String category) {
		ParsedObject parsedSkills = parseSkills(line, parsed,words);
		if(category.equalsIgnoreCase(Headers.get(1))) {
		minSkills.add(parsedSkills);
		} else {
			bonusSkills.add(parsedSkills);
		}
	}
	
	private void parseWorkExp(String line, ParsedObject parsed, ArrayList<String> words, String category) {
		int indexExp = line.indexOf("experience");
		String duration = line.substring(0, indexExp-1);
		//System.out.println(duration);
		words.add(duration.trim());
		line = line.substring(indexExp+"experience".length());
		ParsedObject parsedWorkExp = parseSkills(line.trim(),parsed,words);
		if(category.equalsIgnoreCase(Headers.get(1))) {
		minWorkExp.add(parsedWorkExp);
		} else {
			bonusWorkExp.add(parsedWorkExp);
		}
	}
	
	private void parseEducation(String line, ParsedObject parsed, ArrayList<String> words) {
		ParsedObject parsedEdu = parseSkills(line,parsed,words);
		minEdu.add(parsedEdu);
	}
	
	private ParsedObject parseSkills(String line, ParsedObject parsed, ArrayList<String> words) {
		if((line.matches(".*\\b(or|and)\\b.*"))) {
			if(line.matches(".*\\bor\\b.*")) {
			parsed.setType("or");
			} else {
				parsed.setType("and");
			}
			//System.out.println(line);
			if(line.contains(",")){
			//System.out.println("line with comma: " + line);
			String tokens[] = line.split(",");
			if((line.matches(".*\\b(or|and)\\b.*"))) {
				if(line.matches(".*\\bor\\b.*")) {
					extractOr(line,words);
				} else {
					extractAnd(line, words);	
			} 
			}else {
			for(int j=0;j<tokens.length;j++) {
				//System.out.println(tokens[j]);
				if(j!=tokens.length-1) {
					words.add(tokens[j].trim());
				} else {
					String end[] = tokens[j].trim().split(" ");
					words.add(end[0].trim());
					String last = "";
					for(int i=2;i<end.length;i++) {
					last = last + " " + end[i].trim();
					}
					words.add(last.trim());
				}
			}
			}
			} else {
				if(line.matches(".*\\bor\\b.*")) {
					extractOr(line,words);
				} else {
					extractAnd(line, words);
					}
			}
		} else {
			parsed.setType("none");
			if(line.contains(",")) {
			String tokens[] = line.split(",");
			for(int j=0;j<tokens.length;j++) {
					words.add(tokens[j].trim());	
			}
		} else {
			words.add(line.trim());
		}
		}
		parsed.setWords(words);
		return parsed;
	}

private void extractOr(String line, ArrayList<String> words) {
	while(line.matches(".*\\bor\\b.*")) {
		int index =line.indexOf(" or ");
		String first = line.substring(0, index);
		words.add(first.trim());
		String second = line.substring(index);
		second = second.replaceFirst("or", " ");
		line = second;
		}
		words.add(line.trim());
}
	private void extractAnd(String line, ArrayList<String> words) {
		if(line.matches(".*\\band\\b.*")) {
			while(line.matches(".*\\band\\b.*")) {
			int index =line.indexOf(" and ");
			String first = line.substring(0, index);
			words.add(first.trim());
			String second = line.substring(index);
			second = second.replaceFirst("and", " ");
			line = second.trim();
			}
			words.add(line.trim());
		}
	}
	
	private void parseResponsibilities(String line, ParsedObject parsed, ArrayList<String> words) {
		ParsedObject parsedResponsibilities = parseSkills(line,parsed,words);
		responsibilities.add(parsedResponsibilities);
	}
	
	private static ArrayList<String> parseNlp(ArrayList<String> lines) {
		Properties props = new Properties(); 
	    props.put("annotators", "tokenize, ssplit, pos, lemma"); 
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
	    ArrayList<String> PARAMS = new ArrayList<String> 
        (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ", "CC", ","));
	    
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

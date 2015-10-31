package com.parser;

import java.util.ArrayList;

public class SectionParser {
	
	private CVObject cvObject;
	
	public SectionParser() {
		cvObject = new CVObject();
	}
	
	public CVObject getCVObject() {
		return cvObject;
	}
	
	public void parseSections(String header, ArrayList<String> lines) {
		if(header.equalsIgnoreCase("Name")) {
			cvObject.setName(lines.get(0));
			
		} else if(header.equalsIgnoreCase("Education")) {
			ArrayList<String> parsedEdu;
			parsedEdu = parseEducation(lines);
			cvObject.setEducation(parsedEdu);
			
		} else if(header.equalsIgnoreCase("Skills")) {
			ArrayList<String> parsedSkills;
			parsedSkills = parseSkills(lines);
			cvObject.setSkills(parsedSkills);
		}
	}
	
	//parses education by spaces for now
	public ArrayList<String> parseEducation(ArrayList<String> lines) {
		ArrayList<String> parsedEdu = new ArrayList<String>();
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			String regex = "\\b(did|and|or|of|from|the|have|to|by|in|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sept|Oct|Nov|Dec|\\d+)\\b";
			String keywords = line.replaceAll(regex, "");
			//String tokens[] = keywords.split("\\s*,\\s*");
			String tokens[] = keywords.split(" +");
		//	for(int j=0;j<tokens.length;j++) {
			//	if(tokens[j].matches(".*\\d+.*")) {
				//	tokens[j] = "";
				//} 
			//}
			
			String parsed ="";
			for(int k=0;k<tokens.length;k++) {
				parsed = parsed + tokens[k] + " ";
				parsed.replaceAll("\\s+", " ");
				parsed = parsed.toLowerCase();
			}
			tokens = parsed.split(",");
			for(int p = 0; p < tokens.length; p++) {
				parsedEdu.add(tokens[p].trim());
			}
			
		}
		return parsedEdu;
	}
	
	//parses skills by separating them using commas, removes unnecessary words
	public ArrayList<String> parseSkills(ArrayList<String> lines) {
		ArrayList<String> parsedSkills = new ArrayList<String>();
		
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			String regex = " \\b(and|or|of|from|the|have|to|by|in)\\b ";
			String keywords = line.replaceAll(regex, " ");
			String tokens[] = keywords.split("\\s*,\\s*");
			for(int j=0;j<tokens.length;j++) {
				tokens[j].replaceAll("\\s+", " ");
				tokens[j] = tokens[j].toLowerCase();
				parsedSkills.add(tokens[j].trim());
			}
		}
		return parsedSkills;
	}
}

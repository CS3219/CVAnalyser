package com.parser;
import java.util.ArrayList;

public class SectionsParser {
	
	private CVObject cvObject;
	
	public SectionsParser() {
		cvObject = new CVObject();
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
	
	private ArrayList<String> parseEducation(ArrayList<String> lines) {
		ArrayList<String> parsedEdu = new ArrayList<String>();
		
		
		return parsedEdu;
	}
	
	private ArrayList<String> parseSkills(ArrayList<String> lines) {
		ArrayList<String> parsedSkills = new ArrayList<String>();
		
		for(int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			String keywords = line.replaceAll(" and|or|of|from|the|have|to|in ", " ");
			String tokens[] = keywords.split("\\s*,\\s*");
			for(int j=0;j<tokens.length;j++) {
				parsedSkills.add(tokens[j].trim());
			}
		}
		return parsedSkills;
	}
}

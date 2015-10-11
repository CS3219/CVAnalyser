package com.parser;
import java.util.ArrayList;
public class CVObject {
	private String name;
	private ArrayList<String> education;
	private ArrayList<String> skills;
	
	public CVObject() {
		
	}
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setEducation(ArrayList<String> education) {
		this.education = education;
	}
	
	public void setSkills(ArrayList<String> skills) {
		this.skills = skills;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getEducation() {
		return education;
	}
	
	public ArrayList<String> getSkills() {
		return skills;
	}
	
}

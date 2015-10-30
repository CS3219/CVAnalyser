package com.parser;

import java.util.ArrayList;

public class CVObject {
	private String name;
	private ArrayList<String> education;
	private ArrayList<WorkExpObject> workExp = new ArrayList<WorkExpObject>();
	private ArrayList<String> skills = new ArrayList<String>();
	private ArrayList<String> languages;
	
	/*public CVObject() {
		
	}*/
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setEducation(ArrayList<String> education) {
		this.education = education;
	}
	
	public void setWorkExp(ArrayList<WorkExpObject> workExp) {
	    this.workExp.addAll(workExp);
	}
	   
	public void setSkills(ArrayList<String> skills) {
		this.skills.addAll(skills);
	}
	
	public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getEducation() {
		return education;
	}
	
	public ArrayList<WorkExpObject> getWorkExp() {
        return this.workExp;
    }
	
	public ArrayList<String> getSkills() {
		return this.skills;
	}
	
	public ArrayList<String> getLanguages() {
        return this.languages;
    }
	
}

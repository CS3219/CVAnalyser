package com.parser;

import java.util.ArrayList;

public class CVObject {
	private String name;
	private ArrayList<String> education;
	private ArrayList<ExpObject> workExp;
	private ArrayList<ExpObject> publications;
	private ArrayList<ExpObject> projects;
	private ArrayList<String> skills;
	private ArrayList<String> languages;
	private boolean hasReferences;
	private boolean hasVolunteerExp;
	
	public CVObject() {
		name = null;
		education = new ArrayList<String>();
		workExp = new ArrayList<ExpObject>();
		publications = new ArrayList<ExpObject>();
		projects = new ArrayList<ExpObject>();
		skills = new ArrayList<String>();
		languages = null;
		hasReferences = false;
		hasVolunteerExp = false;
	}
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setEducation(ArrayList<String> education) {
		this.education = education;
	}
	
	public void setWorkExp(ArrayList<ExpObject> workExp) {
	    this.workExp.addAll(workExp);
	}
	
	public void setPublications(ArrayList<ExpObject> publications) {
        this.publications.addAll(publications);
    }
	
	public void setProjects(ArrayList<ExpObject> projects) {
        this.projects.addAll(projects);
    }
	   
	public void setSkills(ArrayList<String> skills) {
		this.skills.addAll(skills);
	}
	
	public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }
	
	public void setHasReferences() {
        hasReferences = true;
    }
	
	public void setHasVolnteerExp() {
	    hasVolunteerExp = true;
    }
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getEducation() {
		return education;
	}
	
	public ArrayList<ExpObject> getWorkExp() {
        return this.workExp;
    }
	
	public ArrayList<ExpObject> getPublications() {
        return this.publications;
    }
	
	public ArrayList<ExpObject> getProjects() {
        return this.projects;
    }
	
	public ArrayList<String> getSkills() {
		return this.skills;
	}
	
	public ArrayList<String> getLanguages() {
        return this.languages;
    }
	
	public boolean gethasReferences() {
        return hasReferences;
    }
	
	public boolean gethasVolunteerExp() {
        return hasVolunteerExp;
    }
	
}

package com.parser;

import java.util.ArrayList;

public class JobDescObject {

	private ArrayList<ParsedObject> responsibilities;
	private ArrayList<ParsedObject>minSkills;
	//private String residentialStatus;
	private ArrayList<ParsedObject> minEdu;
	private ArrayList<ParsedObject> minWorkExp;
	private ArrayList<ParsedObject>bonusSkills;
	private ArrayList<ParsedObject>bonusWorkExp;

	public JobDescObject() {

	}

	public ArrayList<ParsedObject> getResponsibilities() {
		return responsibilities;
	}

	public ArrayList<ParsedObject> getMinSkills() {

		return minSkills;
	}
	
	public ArrayList<ParsedObject> getMinEdu() {
		return minEdu;
	}
	
	public ArrayList<ParsedObject> getMinWorkExp() {
		return minWorkExp;
	}

	public ArrayList<ParsedObject> getBonusSkills() {
		return bonusSkills;
	}
	
	public ArrayList<ParsedObject> getBonusWorkExp() {
	return bonusWorkExp;	
		
	}
	
	public void  setResponsibilities(ArrayList<ParsedObject> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public void setMinSkills(ArrayList<ParsedObject> minSkills) {
		this.minSkills = minSkills;
	}
	
	public void setMinEdu(ArrayList<ParsedObject> minEdu) {
		this.minEdu = minEdu;
	}
	
	public void setMinWorkExp(ArrayList<ParsedObject> minWorkExp) {
		this.minWorkExp = minWorkExp;
	}
	
	public void setBonusSkills(ArrayList<ParsedObject> bonusSkills) {
		this.bonusSkills = bonusSkills;
	}
	
	public void setBonusWorkExp(ArrayList<ParsedObject> bonusWorkExp) {
		this.bonusWorkExp = bonusWorkExp;
	}
	
}

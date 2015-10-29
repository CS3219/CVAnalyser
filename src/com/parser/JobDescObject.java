package com.parser;

import java.util.ArrayList;

public class JobDescObject {

	private ArrayList<String> responsibilities;
	private ArrayList<String>minSkills;
	//private String residentialStatus;
	private ArrayList<String> minEdu;
	private ArrayList<workExp> minWorkExp;
	private ArrayList<String>bonusSkills;
	private ArrayList<String>bonusEdu;
	private ArrayList<workExp>bonusWorkExp;

	public JobDescObject() {

	}

	public ArrayList<String> getResponsibilites() {
		return responsibilities;
	}

	public ArrayList<String> getMinSkills() {

		return minSkills;
	}
	
	public ArrayList<String> getMinEdu() {
		return minEdu;
	}
	
	public ArrayList<workExp> getMinWorkExp() {
		return minWorkExp;
	}

	public ArrayList<String> getBonusSkills() {
		return bonusSkills;
	}
	
	public ArrayList<String> getBonusEdu() {
		return bonusEdu;
	}
	
	public ArrayList<workExp> getBonusWorkExp() {
	return bonusWorkExp;	
		
	}
	
	public void  setResponsibilites(ArrayList<String> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public void setMinSkills(ArrayList<String> minSkills) {
		this.minSkills = minSkills;
	}
	
	public void setMinEdu(ArrayList<String> minEdu) {
		this.minEdu = minEdu;
	}
	
	public void setMinWorkExp(ArrayList<workExp> minWorkExp) {
		this.minWorkExp = minWorkExp;
	}
	
	public void setBonusSkills(ArrayList<String> bonusSkills) {
		this.bonusSkills = bonusSkills;
	}
	
	public void setBonusEdu(ArrayList<String> bonusEdu) {
		this.bonusEdu = bonusEdu;
	}
	
	public void setBonusWorkExp(ArrayList<workExp> bonusWorkExp) {
		this.bonusWorkExp = bonusWorkExp;
	}
	
}

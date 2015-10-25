package com.parser;

import java.util.ArrayList;

public class JobDescObject {

	private ArrayList<String> description;
	private ArrayList<String> responsibilites;
	private ArrayList<String> bonusQ;
	private String residentialStatus;
	private String position;
	private ArrayList<String> eduReq;
	private ArrayList<String> techSkillReq;

	public JobDescObject(String position, ArrayList<String> eduReq, ArrayList<String> techSkillReq) {

		this.position = position;
		this.eduReq = eduReq;
		this.techSkillReq = techSkillReq;
	}

	public String getPosition() {
		return position;
	}

	public ArrayList<String> getEduReq() {
		SectionParser sectionParser = new SectionParser();
		eduReq = sectionParser.parseEducation(eduReq);

		return eduReq;
	}

	public ArrayList<String> getTechSkillReq() {
		SectionParser sectionParser = new SectionParser();
		techSkillReq = sectionParser.parseSkills(techSkillReq);
		return techSkillReq;
	}

}

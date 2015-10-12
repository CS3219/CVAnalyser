package com.anayser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.parser.*;

public class Analyser {
	//o--> name 1--> score
	ArrayList<ResultDetail> results =  new ArrayList<ResultDetail>();
	
	public Analyser(){
		
	}
	
	public ArrayList<ResultDetail> analyse(JobDescObject jobDesc, ArrayList<CVObject> cvs){
		ArrayList<String> cvSkill;
		ArrayList<String> cvEducation;
		ArrayList<String> temp;
		ArrayList<String> skillReq = jobDesc.getTechSkillReq();
		ArrayList<String> eduReq = jobDesc.getEduReq();
		int skillScore = 0, eduScore = 0;
		double score = 0;

		for(int i = 0; i < cvs.size(); i++){
			cvSkill = cvs.get(i).getSkills();
			cvEducation = cvs.get(i).getEducation();
			skillScore = compareSkill(skillReq, cvSkill);
			eduScore = compareEdu(eduReq, cvEducation);
			score = 0.25*skillScore + 0.25*eduScore;
			ResultDetail resultDetail = new ResultDetail(cvs.get(i).getName(), Double.toString(score), score); 
			results.add(resultDetail);	
		}
		sortResults();
		return results;
	}

	private void sortResults() {
		Collections.sort(results, new Comparator<ResultDetail>() {
			@Override
			public int compare(ResultDetail arg0, ResultDetail arg1) {
				 return Double.compare(arg0.score, arg1.score);
			}

	    });
		
	}

	private int compareEdu(ArrayList<String> eduReq, ArrayList<String> cvEducation) {
		int eduNum = 0;
		for(int i = 0; i < eduReq.size(); i++) {
			for(int j = 0; j < cvEducation.size(); j++) {
				if(eduReq.get(i).equals(cvEducation.get(j))) {
					eduNum++;
				}
			}
		}
		int eduScore = eduNum / eduReq.size();
		return eduScore;
		
	}

	//score 
	//= 0.25*skill_score + 0.25*education_score 
	//+ 0.25*work_experience_score + 0.25*language_score
	private int compareSkill(ArrayList<String> skillReq, ArrayList<String> cvSkill) {
		int skillNum = 0;
		for(int i = 0; i < skillReq.size(); i++) {
			for(int j = 0; j < cvSkill.size(); j++) {
				if(skillReq.get(i).equals(cvSkill.get(j))) {
					skillNum++;
				}
			}
		}
		int skillScore = skillNum / skillReq.size();
		return skillScore;
		
	}
}

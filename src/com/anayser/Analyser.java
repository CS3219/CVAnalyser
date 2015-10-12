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
	
	public ArrayList<ArrayList<String>> analyse(JobDescObject jobDesc, ArrayList<CVObject> cvs){
		ArrayList<String> cvSkill;
		ArrayList<String> cvEducation;
		ArrayList<String> skillReq = jobDesc.getTechSkillReq();
		ArrayList<String> eduReq = jobDesc.getEduReq();
		int skillScore = 0, eduScore = 0;
		double score = 0;

		System.out.println("cv size = "+cvs.size());
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
		ArrayList<ArrayList<String>> results_2nd = convert();
		
		//for(int k = 0; k < results_2nd.size(); k++) {
			//for(int j = 0; j<results_2nd.get(k).size(); j++){
				//System.out.println("results = " + results_2nd.get(k).get(j));
		//	}
		//}
		return results_2nd;
	}

	private ArrayList<ArrayList<String>> convert() {
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<ArrayList<String>> results_2nd = new ArrayList<ArrayList<String>> ();
		//System.out.println("size = " + results.size());
		for(int i = 0; i < results.size(); i++) {
			temp.add(results.get(i).name);
		//	System.out.println("name = " + temp.get(0));
			temp.add(results.get(i).scoreInString);
		//	System.out.println("score = " + temp.get(1));
			results_2nd.add(new ArrayList<String>());
			results_2nd.get(i).addAll(temp);
			temp.clear();
			//System.out.println("results = " + results_2nd.get(i).get(0));
		}
		return results_2nd;
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
				System.out.println("eduReq = " + eduReq.get(i) + " cvEdu = " + cvEducation.get(j));
				if(cvEducation.get(j).contains(eduReq.get(i))) {
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
			//	System.out.println("skillReq = " + skillReq.get(i) + " cvSkill = " + cvSkill.get(j));
			skillReq.set(i, skillReq.get(i).toLowerCase());
				if(skillReq.get(i).equals(cvSkill.get(j))) {
					skillNum++;
				}
			}
		}
		int skillScore = skillNum / skillReq.size();
		return skillScore;
		
	}
}

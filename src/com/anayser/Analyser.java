package com.anayser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parser.*;

public class Analyser {
	//o--> name 1--> score
	ArrayList<ResultDetail> results =  new ArrayList<ResultDetail>();
	
	public Analyser(){
		
	}
	
	public ArrayList<ArrayList<String>> analyse(JobDescObject jobDesc, ArrayList<CVObject> cvs){
		ArrayList<String> cvSkill, cvEducation, cvLanguage;
		ArrayList<WorkExpObject> cvExp, cvProj, cvPublication;
		ArrayList<String> minSkillReq = jobDesc.getMinSkills();
		ArrayList<String> extraSkillReq = jobDesc.getExtraSkills();
		ArrayList<String> minEduReq = jobDesc.getMinEdu();
		ArrayList<String> extraEduReq = jobDesc.getExtraEdu();
		ArrayList<String> minWorkExp = jobDesc.getMinWorkExp();
		ArrayList<String> extraWorkExp = jobDesc.getExtraWorkExp();
		double minSkillScore = 0.0, extraSkillScore = 0.0, minEduScore = 0.0, extraEduScore = 0.0, minExpScore = 0.0, extraExpScore = 0.0;
		double score = 0.0;

		System.out.println("cv size = "+cvs.size());
		for(int i = 0; i < cvs.size(); i++){
			System.out.println("cvname = " + cvs.get(i).getName());
			cvSkill = cvs.get(i).getSkills();
			cvEducation = cvs.get(i).getEducation();
			cvExp = cvs.get(i).getWorkExp();
			cvProj = cvs.get(i).getProj();
			cvPublication = cvs.get(i).getPublication();
			cvLanguage =cvs.get(i).getLanguage();
			cvSkill.addAll(cvLanguage);
			
			minSkillScore = compareSkill(minSkillReq, cvSkill);
			extraSkillScore = compareSkill(extraSkillReq, cvSkill);
			
			minEduScore = compareEdu(minEduReq, cvEducation);
			extraEduScore = compareEdu(extraEduReq, cvEducation);
			
			minExpScore = compareExp(minWorkExp, cvExp, cvProj, cvPublication, jobDesc.getPosition());
			extraExpScore = compareExp(extraWorkExp, cvExp, cvProj, cvPublication, jobDesc.getPosition());
			
			if(minEduScore == -1 && minSkillScore > 0.5){
				minEduScore = 1;
			}
			if((minWorkExp.size() > 0 && minExpScore == 0.0) || (minEduReq.size() > 0 && minEduScore == 0.0) || (minSkillReq.size() > 0 && minSkillScore == 0.0)) {
				score = 0.0;
			} else {
				score = 0.3*minSkillScore + 0.3*minExpScore + 0.3*minEduScore + 0.1*(extraExpScore + extraSkillScore + extraEduScore);
			}
			
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

	private ArrayList<ArrayList<String> > convert() {
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<ArrayList<String>> results_2nd = new ArrayList<ArrayList<String>> ();
		//System.out.println("size = " + results.size());
		for(int i = 0; i < results.size(); i++) {
			temp.add(results.get(i).name);
			System.out.println("name = " + temp.get(0));
			temp.add(results.get(i).scoreInString);
			System.out.println("score = " + temp.get(1));
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

	//exp score --> higher exp higher score.
	private double compareExp(ArrayList<WorkExpObject> workExp, ArrayList<WorkExpObject> cvExp, ArrayList<WorkExpObject> cvProj, ArrayList<WorkExpObject> cvPublication, String position) {
		double expNum = 0.0, expScore = 0.0;
		
		cvExp.addAll(cvProj);
		cvExp.addAll(cvPublication);
		Pattern patternForSimilar = Pattern.compile("\\bsimilar\\b|\\bequivalent\\b|\\brelevant\\b|\\brelated\\b|\\binterrelated\\b|\\bsame\\b");
		
		for(int i = 0; i < workExp.size(); i++) {
			String[] parts = workExp.get(i).getArea().split("\\bor\\b|\\band\\b|,");
			for(int k = 0; k < parts.length; k++) {
		         Matcher matcher = patternForSimilar.matcher(parts[k]);
	
		         for(int j = 0; j < cvExp.size(); j++) {
		        	 if(cvExp.get(j).getArea().contains(parts[k]) || (matcher.find() && cvExp.get(j).getArea().contains(position))) {
					    expNum += cvExp.get(j).getNumYears();
				}
			}
		}
			if(expNum >= workExp.get(i).getNumYears()) {
				expScore += expNum / workExp.get(i).getNumYears();
			} 
			
			expNum = 0.0;
		}
		
		return expScore;
	}
	
	private double compareEdu(ArrayList<String> eduReq, ArrayList<String> cvEducation) {
		int eduNum = 0;
		boolean isSimilar = false;
		Pattern patternForDiploma = Pattern.compile("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b|\\bdiploma\\b");
		Pattern patternForBE = Pattern.compile("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b");
		Pattern patternForMaster = Pattern.compile("\\bmaster\\b|\\bphd\\b");
		Pattern patternForPhd = Pattern.compile("\\bphd\\b");
		Pattern patternForSimilar = Pattern.compile("\\bsimilar\\b|\\bequivalent\\b|\\brelevant\\b|\\brelated\\b|\\binterrelated\\b|\\bsame\\b");
	
		Matcher matcher;
		String prev = "";
		for(int i = 0; i < eduReq.size(); i++) {
			matcher = patternForSimilar.matcher(eduReq.get(i));
			eduReq.set(i, eduReq.get(i).replaceAll("\\bgraduate\\b|\\bgraduated\\b|\\bdegree(s|)\\b", ""));
			eduReq.set(i,eduReq.get(i).replaceAll("\\bbe\\b|\\bbachelors\\b|\\bbs\\b", "bachelor"));
			eduReq.set(i,eduReq.get(i).replaceAll("\\bmsc\\b|\\bmasters\\b|\\bms\\b", "master"));
			eduReq.set(i,eduReq.get(i).replaceAll("\\bdiploma\\b|\\bdiplomas\\b", "diploma"));
			eduReq.set(i,eduReq.get(i).replaceAll("\\bphd\\b|\\bph.d\\b|\\bdoctorate(s|)\\b|\\bdoctor(s|)\\b|\\bDoctor of Philosophy\\b", "phd"));
			
			if(matcher.find()) {
				isSimilar = true;
			}
			for(int j = 0; j < cvEducation.size(); j++) {
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bgraduate\\b|\\bgraduated\\b|\\bdegree(s|)\\b", ""));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bbe\\b|\\bbachelors\\b|\\bbs\\b", "bachelor"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bmsc\\b|\\bmasters\\b|\\bms\\b", "master"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bphd\\b|\\bph.d\\b|\\bdoctorate(s|)\\b|\\bdoctor(s|)\\b|\\bDoctor of Philosophy\\b", "phd"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bdiploma\\b|\\bdiplomas\\b", "diploma"));
				
				//System.out.println("eduReq = " + eduReq.get(i) + " cvEdu = " + cvEducation.get(j));
				
				if(Pattern.compile("\\bbachelor\\b").matcher(eduReq.get(i)).find()) {
					matcher = patternForBE.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).replaceAll("\\bbachelor\\b", "");
						cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
						//		cvEducation.get(j).contains(eduReq.get(i))){
							eduNum = 1;
						} else {
							prev = "bachelor";
						}
					}	
				} else if(Pattern.compile("\\bmaster\\b").matcher(eduReq.get(i)).find()){
					matcher = patternForMaster.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).replaceAll("\\bmaster\\b", "");
						cvEducation.get(j).replaceAll("\\bmaster\\b|\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum = 1;
						} else {
							prev = "master";
						}
					}	
				} else if(Pattern.compile("\\bphd\\b").matcher(eduReq.get(i)).find()){
					matcher = patternForPhd.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).replaceAll("\\bphd\\b", "");
						cvEducation.get(j).replaceAll("\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum = 1;
						} else {
							prev = "phd";
						}
					}	
				}  else if(Pattern.compile("\\bdiploma\\b").matcher(eduReq.get(i)).find()){
					matcher = patternForDiploma.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).replaceAll("\\bdiploma\\b", "");
						cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b|\\bdiploma\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum = 1;
						} else {
							prev = "diploma";
						}
					}	
				} else {
					if(Pattern.compile("\\b"+ eduReq.get(i) + "(s|)\\b").matcher(cvEducation.get(j)).find() && Pattern.compile("\\b"+ prev + "\\b").matcher(cvEducation.get(j)).find()){
						eduNum = 1;
				 }
				}
			}
		}
		double eduScore;
		if(eduNum != 1 && isSimilar) {
			eduScore = -1;
		} else {
		   eduScore = (double)eduNum;
		}
		return eduScore;
		
	}

	//miss out soft skill
	private double compareSkill(ArrayList<String> skillReq, ArrayList<String> cvSkill) {
		int skillNum = 0; 
		for(int i = 0; i < skillReq.size(); i++) {
			skillReq.set(i, bypassUnwanted(skillReq.get(i)));
			for(int j = 0; j < cvSkill.size(); j++) {
				cvSkill.set(j, bypassUnwanted(cvSkill.get(j)));
				 String patternString = "\\b"+cvSkill.get(j)+"(s|)\\b";
		         Pattern pattern =Pattern.compile(patternString);
		         Matcher matcher = pattern.matcher(skillReq.get(i));
				if(matcher.find()) {
					skillNum++;
					cvSkill.set(j, "");
					System.out.println("skillnum = " + skillNum);
				}
			}
		}
		double skillScore = (double)skillNum / skillReq.size();
		System.out.println("skillscore = "+skillReq.size());
		return skillScore;
		
	}
	
	private String bypassUnwanted(String word){
		word = word.replaceAll("(strong |deep |good |excellence |great |excellent |)\\bproficiency\\b|\\bspecialist domains\\b|\\btechnical knowledge\\b|\\bknowledge\\b|\\bexpertise\\b|\\bability\\b|\\bfoundation\\b", "");
		return word;
	}
}

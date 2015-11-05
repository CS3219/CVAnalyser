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
	
	public ArrayList<ArrayList<String>> analyse(JobDescObject jobDesc, ArrayList<CVObject> cvs, String position){
		ArrayList<String> cvSkill, cvEducation, cvLanguage;
		ArrayList<ExpObject> cvExp, cvProj, cvPublication;
		ArrayList<ParsedObject> minSkillReq = jobDesc.getMinSkills();
		ArrayList<ParsedObject> extraSkillReq = jobDesc.getBonusSkills();
		ArrayList<ParsedObject> minEduReq = jobDesc.getMinEdu();
		ArrayList<ParsedObject> minWorkExp = jobDesc.getMinWorkExp();
		ArrayList<ParsedObject> extraWorkExp = jobDesc.getBonusWorkExp();
		double minSkillScore = 0.0, extraSkillScore = 0.0, minEduScore = 0.0, minExpScore = 0.0, extraExpScore = 0.0;
		double score = 0.0;

		System.out.println("cv size = "+cvs.size());
		for(int i = 0; i < cvs.size(); i++){
			System.out.println("cvname = " + cvs.get(i).getName());
			cvSkill = cvs.get(i).getSkills();
			cvEducation = cvs.get(i).getEducation();
			cvExp = cvs.get(i).getWorkExp();
			cvProj = cvs.get(i).getProjects();
			cvPublication = cvs.get(i).getPublications();
			cvLanguage =cvs.get(i).getLanguages();
			//cvCertification = cvs.get(i).getCertifications();
			cvSkill.addAll(cvLanguage);
			//cvSkill.addAll(cvCertification);
			
			minSkillScore = compareSkill(minSkillReq, cvSkill);
			extraSkillScore = compareSkill(extraSkillReq, cvSkill);
			
			minEduScore = compareEdu(minEduReq, cvEducation);
			
			minExpScore = compareExp(minWorkExp, cvExp, cvProj, cvPublication, position);
			extraExpScore = compareExp(extraWorkExp, cvExp, cvProj, cvPublication, position);
			
			if(minEduScore == -1 && minSkillScore > 0.5){
				minEduScore = 1;
			}
			
			//if one min req is unfulfilled, score = 0
			if((minWorkExp.size() > 0 && minExpScore == 0.0) || (minEduReq.size() > 0 && minEduScore == 0.0) || (minSkillReq.size() > 0 && minSkillScore == 0.0)) {
				score = 0.0;
			} else {
				score = 0.3*minSkillScore + 0.3*minExpScore + 0.3*minEduScore + 0.1*(extraExpScore + extraSkillScore);
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
	private double compareExp(ArrayList<ParsedObject> workExp, ArrayList<ExpObject> cvExp, ArrayList<ExpObject> cvProj, ArrayList<ExpObject> cvPublication, String position) {
		double expNum = 0.0, expScore = 0.0, numOfYears = 0.0, totalNumOfYears = 0.0;
		
		cvExp.addAll(cvProj);
		cvExp.addAll(cvPublication);
		Pattern patternForSimilar = Pattern.compile("\\bsimilar\\b|\\bequivalent\\b|\\brelevant\\b|\\brelated\\b|\\binterrelated\\b|\\bsame\\b");
		
		for(int i = 0; i < workExp.size(); i++) {
			numOfYears =  Double.parseDouble(workExp.get(i).getWords().get(0));
			if(workExp.get(i).getType().equals("or")){
				totalNumOfYears = numOfYears;
			} else {
				totalNumOfYears = workExp.get(i).getWords().size() * numOfYears;
			}
			for(int k = 1; k < workExp.get(i).getWords().size(); k++){
			
		        Matcher matcher = patternForSimilar.matcher(workExp.get(i).getWords().get(k));
	
		         for(int j = 0; j < cvExp.size(); j++) {
		        	 if(cvExp.get(j).getDescription().contains(workExp.get(i).getWords().get(k)) || (matcher.find() && cvExp.get(j).getDescription().contains(position))) {
					    expNum += cvExp.get(j).getDuration();
				}
			}
		
			}
			//now if it doesn't reach 1.5 yrs, score will count as 0
			if(expNum >= totalNumOfYears) {
				expScore += expNum / totalNumOfYears;
			} 
			
			expNum = 0.0;
			totalNumOfYears = 0.0;
		}
		
		return expScore;
	}
	
	private double compareEdu(ArrayList<ParsedObject> eduReq, ArrayList<String> cvEducation) {
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
			for(int k = 0;k < eduReq.get(i).getWords().size(); k++) {
			matcher = patternForSimilar.matcher(eduReq.get(i).getWords().get(k));
			eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bgraduate\\b|\\bgraduated\\b|\\bdegree(s|)\\b", ""));
			eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bbe\\b|\\bbachelors\\b|\\bbs\\b|\\bundergraduate\\b", "bachelor"));
			eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bmsc\\b|\\bmasters\\b|\\bms\\b|\\bpostgraduate\\b", "master"));
			eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bdiploma\\b|\\bdiplomas\\b|\\bpecialist diploma\\b", "diploma"));
			eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bphd\\b|\\bph.d\\b|\\bdoctorate(s|)\\b|\\bdoctor(s|)\\b|\\bDoctor of Philosophy\\b", "phd"));
			
			if(matcher.find()) {
				isSimilar = true;
			}
			for(int j = 0; j < cvEducation.size(); j++) {
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bgraduate\\b|\\bgraduated\\b|\\bdegree(s|)\\b", ""));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bbe\\b|\\bbachelors\\b|\\bbs\\b|\\bundergraduate\\b", "bachelor"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bmsc\\b|\\bmasters\\b|\\bms\\b|\\bpostgraduate\\b", "master"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bphd\\b|\\bph.d\\b|\\bdoctorate(s|)\\b|\\bdoctor(s|)\\b|\\bDoctor of Philosophy\\b", "phd"));
				cvEducation.set(j,cvEducation.get(j).replaceAll("\\bdiploma\\b|\\bdiplomas\\b|\\bpecialist diploma\\b", "diploma"));
				
				//System.out.println("eduReq = " + eduReq.get(i) + " cvEdu = " + cvEducation.get(j));
				
				if(Pattern.compile("\\bbachelor\\b").matcher(eduReq.get(i).getWords().get(k)).find()) {
					matcher = patternForBE.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).getWords().get(k).replaceAll("\\bbachelor\\b", "");
						cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
						//		cvEducation.get(j).contains(eduReq.get(i))){
							eduNum++;
						} else {
							prev = "bachelor";
						}
					}	
				} else if(Pattern.compile("\\bmaster\\b").matcher(eduReq.get(i).getWords().get(k)).find()){
					matcher = patternForMaster.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).getWords().get(k).replaceAll("\\bmaster\\b", "");
						cvEducation.get(j).replaceAll("\\bmaster\\b|\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum++;
						} else {
							prev = "master";
						}
					}	
				} else if(Pattern.compile("\\bphd\\b").matcher(eduReq.get(i).getWords().get(k)).find()){
					matcher = patternForPhd.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).getWords().get(k).replaceAll("\\bphd\\b", "");
						cvEducation.get(j).replaceAll("\\bphd\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum++;
						} else {
							prev = "phd";
						}
					}	
				}  else if(Pattern.compile("\\bdiploma\\b").matcher(eduReq.get(i).getWords().get(k)).find()){
					matcher = patternForDiploma.matcher(cvEducation.get(j));
					if(matcher.find()) {
						eduReq.get(i).getWords().get(k).replaceAll("\\bdiploma\\b", "");
						cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b|\\bdiploma\\b", "");
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum++;
						} else {
							prev = "diploma";
						}
					}	
				} else {
					if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find() && Pattern.compile("\\b"+ prev + "\\b").matcher(cvEducation.get(j)).find()){
						eduNum++;
				 }
				}
			}
		}
		}
		double eduScore;
		if(eduNum < 1 && isSimilar) {
			eduScore = -1;
		} else {
		   eduScore = (double)eduNum;
		}
		return eduScore;
		
	}

	//miss out soft skill
	private double compareSkill(ArrayList<ParsedObject> skillReq, ArrayList<String> cvSkill) {
		int skillNum = 0; int totalNum = 0;
		for(int i = 0; i < skillReq.size(); i++) {
			if(skillReq.get(i).getType().equals("or")){
				totalNum += 1;
			} else {
				totalNum += skillReq.get(i).getWords().size();
			}
			for(int k = 0; k < skillReq.get(i).getWords().size(); k++) {
				skillReq.get(i).getWords().set(k, bypassUnwanted(skillReq.get(i).getWords().get(k)));
			
			for(int j = 0; j < cvSkill.size(); j++) {
				if(cvSkill.get(j).equals("")) {
					break;
				}
				cvSkill.set(j, bypassUnwanted(cvSkill.get(j)));
				 String patternString = "\\b"+cvSkill.get(j)+"(s|)\\b";
		         Pattern pattern =Pattern.compile(patternString);
		         Matcher matcher = pattern.matcher(skillReq.get(i).getWords().get(k));
				if(matcher.find()) {
					skillNum++;
				//	cvSkill.set(j, "");
					System.out.println("skillnum = " + skillNum);
				}
			}
		}
		}
		double skillScore = (double)skillNum / totalNum;
		System.out.println("skillscore = "+totalNum);
		return skillScore;
		
	}
	
	private String bypassUnwanted(String word){
		word = word.replaceAll("\\b(strong |deep |good |excellence |great |excellent |)proficiency\\b|specialist domains\\b|technical knowledge\\b|knowledge\\b|expertise\\b|ability\\b|foundation\\b", "");
		return word;
	}
}

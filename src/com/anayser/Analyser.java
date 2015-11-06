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

		//System.out.println("cv size = "+cvs.size());
		for(int i = 0; i < cvs.size(); i++){
			System.out.println("cvname = " + cvs.get(i).getName());
			cvSkill = cvs.get(i).getSkills();
			cvEducation = cvs.get(i).getEducation();
			cvExp = cvs.get(i).getWorkExp();
			cvProj = cvs.get(i).getProjects();
			cvPublication = cvs.get(i).getPublications();
			cvLanguage =cvs.get(i).getLanguages();
			cvSkill.addAll(cvLanguage);
			
			minSkillScore = compareSkill(minSkillReq, cvSkill);
			extraSkillScore = compareSkill(extraSkillReq, cvSkill);
			
			minEduScore = compareEdu(minEduReq, cvEducation);
			
			minExpScore = compareExp(minWorkExp, cvExp, cvProj, cvPublication, position);
		//	extraExpScore = compareExp(extraWorkExp, cvExp, cvProj, cvPublication, position);
			
			if(minEduScore == -1 && minSkillScore > 0.5){
				minEduScore = 1;
			}
			System.out.println("minEduScore = "+minEduScore+ " minExpScore = "+minExpScore+ " minSkillScore = "+minSkillScore + " extraExpScore = "+extraExpScore);
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
		double expNum = 0.0, expScore = 0.0, totalNumOfYears = 0.0;
		double numOfYears = 0.0; 
		String year = "", temp ="";
		int count = 0;
		 String[] word;
		
		cvExp.addAll(cvProj);
		cvExp.addAll(cvPublication);
		Pattern patternForSimilar = Pattern.compile("\\bsimilar\\b|\\bequivalent\\b|\\brelevant\\b|\\brelated\\b|\\binterrelated\\b|\\bsame\\b");
		
		for(int i = 0; i < workExp.size(); i++) {
			System.out.println("workExp = " + workExp.get(i).getWords().get(0));
			temp = workExp.get(i).getWords().get(0).replaceAll("\\d+(.|)\\d+","");
			year = workExp.get(i).getWords().get(0).replace(temp, "");
			numOfYears =  Double.parseDouble(year);
			
			//System.out.println("numOfYears = " + numOfYears);
			workExp.get(i).getWords().add(1, workExp.get(i).getWords().get(0).replaceAll("\\d+(.|)\\d+ year(s|)",""));
			if(workExp.get(i).getType().equals("or")){
				totalNumOfYears = numOfYears;
			} else {
				totalNumOfYears = workExp.get(i).getWords().size() * numOfYears;
			}
			for(int k = 1; k < workExp.get(i).getWords().size(); k++){
		        Matcher matcher = patternForSimilar.matcher(workExp.get(i).getWords().get(k));
	
		        workExp.get(i).getWords().set(k, workExp.get(i).getWords().get(k).replaceAll("\\s+", " "));
		        workExp.get(i).getWords().set(k, workExp.get(i).getWords().get(k).trim());
		         for(int j = 0; j < cvExp.size(); j++) {
		        	 
		        //	temp = cvExp.get(j).getDescription().get(index)
		        	//		.replaceAll("\\byear(s|)\\b|\\bmonth(s|)\\b|\\bday(s|)\\b", "");
		        	 for(int y = 0; y < cvExp.get(j).getDescription().size(); y++) {
		        		 System.out.println("job desc = " + workExp.get(i).getWords().get(k) + " cv = " + cvExp.get(j).getDescription().get(y));
		        		 word = workExp.get(i).getWords().get(k).split("\\s");
		        		
		        		 for(int p = 0; p < word.length; p++){
		        			 System.out.println("word[p] = "+ word[p] + " p = " + p);
		        		
		        			 if(!word[p].equals("") && Pattern.compile("\\b"+word[p]+"\\b").matcher(cvExp.get(j).getDescription().get(y)).find()){
		        			//		 cvExp.get(j).getDescription().get(y).contains(word[p]) || (matcher.find() && cvExp.get(j).getDescription().contains(position))) {
		        				 count++;
		        				 System.out.println("count = "+ count);
		        				 } 
		        			 //else if(word[p].contains("ment") || word[p].contains("tion")) {
		        				//	 count++;
		        				 //System.out.println("count = "+ count );
		        				 //}
		        			 
		        		 }
		        		 System.out.println("word length = "+ word.length);
		        		 if(count == word.length){
		        			 expNum += cvExp.get(j).getDuration();
		        			 System.out.println("expNum = " + expNum + " cv = " + cvExp.get(j).getDuration());
		        		 }
		        		 count = 0;
		        		 
		        		 if(matcher.find()) {
		        			 String[] wordForPosition = position.split("\\s");
		        			 for(int u = 0; u < wordForPosition.length; u++){
		        				
		        				  if(Pattern.compile("\\b"+wordForPosition[u] +"\\b").matcher(cvExp.get(j).getDescription().get(y)).find()){
		        					  count++;
		        				  } 
		        				  //else if(wordForPosition[u].contains("ment")) {
			        				//	 count++;
				        			//	 System.out.println("count = "+ count );
				        				// }
		        			 }
		        			if(count == wordForPosition.length){
		        			 expNum += cvExp.get(j).getDuration();
		        		 }
		        		 count = 0;
		        		 }
		        		 
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
		System.out.println("expScore = "+ expScore);
		
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
	//	System.out.println("eduReq'size = " + eduReq.size() + " cvEducation.size() = " + cvEducation.size());
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
				
			//	System.out.println("cv = " + cvEducation.get(j) +" job desc = " + eduReq.get(i).getWords().get(k));
				if(Pattern.compile("\\bbachelor\\b").matcher(eduReq.get(i).getWords().get(k)).find()) {
					matcher = patternForBE.matcher(cvEducation.get(j));
					
					if(matcher.find()) {
					//	System.out.println("in1");
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bbachelor\\b", ""));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b", ""));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\s+", " "));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\s+", " "));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).trim());
						cvEducation.set(j, cvEducation.get(j).trim());
						
					//	System.out.println("inCVs = " + cvEducation.get(j) + " injob desc = " + eduReq.get(i).getWords().get(k));
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
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bmaster\\b", ""));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\bmaster\\b|\\bphd\\b", ""));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\s+", " "));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\s+", " "));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).trim());
						cvEducation.set(j, cvEducation.get(j).trim());
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
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bphd\\b", ""));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\bphd\\b", ""));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\s+", " "));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\s+", " "));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).trim());
						cvEducation.set(j, cvEducation.get(j).trim());
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
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\bdiploma\\b", ""));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\bbachelor\\b|\\bmaster\\b|\\bphd\\b|\\bdiploma\\b", ""));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\s+", " "));
						cvEducation.set(j, cvEducation.get(j).replaceAll("\\s+", " "));
						
						eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).trim());
						cvEducation.set(j, cvEducation.get(j).trim());
						//remind parser to split by "or" "," when bachelor of comp sci or related field in diff index
						if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
							eduNum++;
						} else {
							prev = "diploma";
						}
					}	
				} else {
					eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).replaceAll("\\s+", " "));
					cvEducation.set(j, cvEducation.get(j).replaceAll("\\s+", " "));
					
					eduReq.get(i).getWords().set(k, eduReq.get(i).getWords().get(k).trim());
					cvEducation.set(j, cvEducation.get(j).trim());
					
					//System.out.println("prev = " +prev + " inCVs = " + cvEducation.get(j) + " injob desc = " + eduReq.get(i).getWords().get(k));
					if(Pattern.compile("\\b"+ eduReq.get(i).getWords().get(k) + "(s|)\\b").matcher(cvEducation.get(j)).find()){
			
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
	
		System.out.println("eduScore = " +eduScore);
		return eduScore;
		
	}

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
			//	if(!skillReq.get(i).getWords().get(k).equals("")){
				//  skillReq.get(i).getWords().set(k, skillReq.get(i).getWords().get(k).substring(0, skillReq.get(i).getWords().get(k).length()-1));
				//}
			//	System.out.println("JD's skill = " + skillReq.get(i).getWords().get(k));
			for(int j = 0; j < cvSkill.size(); j++) {
				if(cvSkill.get(j).equals("")) {
					break;
				}
				cvSkill.set(j, bypassUnwanted(cvSkill.get(j)));
				if(!cvSkill.get(j).equals("") && cvSkill.get(j).endsWith(" ")){
					cvSkill.set(j, cvSkill.get(j).substring(0, cvSkill.get(j).length()-1));
					}
				
				 String patternString = "\\b"+cvSkill.get(j)+"(s|)\\b";
		//		 System.out.println("cv = " + cvSkill.get(j) + " JD's skill = " + skillReq.get(i).getWords().get(k));
		         Pattern pattern =Pattern.compile(patternString);
		         Matcher matcher = pattern.matcher(skillReq.get(i).getWords().get(k));
				if(matcher.find()) {
					System.out.println("in");
					skillNum++;
					//cvSkill.set(j, "");
				//	System.out.println("skillnum = " + skillNum +" cv = " + cvSkill.get(j)+ " JD's skill = " + skillReq.get(i).getWords().get(k));
				}
			}
		}
		}
		double skillScore = (double)skillNum / totalNum;
		System.out.println("skillscore = "+skillScore);
		return skillScore;
		
	}
	
	private String bypassUnwanted(String word){
		word = word.replaceAll("\\b(strong |deep |good |excellence |great |excellent |)(proficiency\\b|specialist domains\\b|technical knowledge\\b|knowledge\\b|expertise\\b|ability\\b|foundation\\b|experience\\b)", "");
		return word;
	}
}

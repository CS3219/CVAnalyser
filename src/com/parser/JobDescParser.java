package com.parser;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class JobDescParser {
	
	public JobDescParser() {
		
	}
	
    public JobDescObject parseJobDesc(String position, String eduReq, String techSkills) {
        StringTokenizer st = new StringTokenizer(eduReq, "\n");
        ArrayList<String> edu = new ArrayList<String>();
        
        while(st.hasMoreTokens()) {
            edu.add(st.nextToken());
        }
        
        st = new StringTokenizer(techSkills, "\n,");
        ArrayList<String> tech = new ArrayList<String>();
        
        while(st.hasMoreTokens()) {
            tech.add(st.nextToken());
        }
        
        return new JobDescObject(position, edu, tech);
    }
	
}

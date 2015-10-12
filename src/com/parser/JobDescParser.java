package com.parser;
import java.util.ArrayList;

public class JobDescParser {
	
    JobDescObject parseJobDesc(String position, ArrayList<String> eduReq, 
            ArrayList<String> techSkills) {
        return new JobDescObject(position, eduReq, techSkills);
    }
	
}

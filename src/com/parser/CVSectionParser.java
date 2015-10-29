package com.parser;

import java.util.ArrayList;
import java.util.Arrays;

public class CVSectionParser {

    private static final ArrayList<String> KEYWORDS_CV = new ArrayList<String>
    (Arrays.asList("education", "skills", "languages", "work experience", "interests", "referees", 
        "CCA", "extracurricular activities")); 
    
    public CVObject parseCV(ArrayList<String> cv) {
        SectionExtractor se = new SectionExtractor();
        ArrayList<Integer> sections = new ArrayList<Integer>();

        sections = se.extractSections(cv, KEYWORDS_CV);

        for (int i = 0; i < sections.size(); i++) {
            System.out.println("index = "+sections.get(i));
        }
        
        
        return null;
    }
}

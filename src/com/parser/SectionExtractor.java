package com.parser;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SectionExtractor {

    private static final String KEYWORD_NAME = "name";

    public ArrayList<SectionHeader> extractSections(ArrayList<String> file, 
            ArrayList<String> KEYWORDS) {
        
        int i = 0;
        ArrayList<SectionHeader> sectionIndices = new ArrayList<SectionHeader>();

        sectionIndices.add(new SectionHeader(i, KEYWORD_NAME)); //name
        i++;

        for (; i < file.size(); i++) {
            String header = parseSection(file.get(i), KEYWORDS);
            
            if (header != null) {
                sectionIndices.add(new SectionHeader(i, header));
            }
        }

        return sectionIndices;
    }

    private String parseSection(String strToCompare, ArrayList<String>list) {

        for(String str:list) {
            if(str.toLowerCase().contains(strToCompare) || strToCompare.toLowerCase().contains(str)) {
                StringTokenizer tokens = new StringTokenizer(strToCompare);
                if (tokens.countTokens() < 5) {
                    return str;
                }
            }
        }
        return null;
    }
}

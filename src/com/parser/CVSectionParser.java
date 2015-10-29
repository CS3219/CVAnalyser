package com.parser;

import java.util.ArrayList;

public class CVSectionParser {

    public CVObject parseCV(ArrayList<String> cv) {
        SectionExtractor se = new SectionExtractor();
        ArrayList<Integer> sections = new ArrayList<Integer>();

        sections = se.extractSections(cv, true);

        
        
        return null;
    }
}

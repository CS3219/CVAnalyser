package com.parser;

import java.util.ArrayList;

public class CVParser {
    
    public CVObject parseCV(String filename) {
        PreProcessor pp = new PreProcessor();
        ArrayList<String> cv = pp.preprocess(filename);
        
        CVSectionParser sp = new CVSectionParser();
        return sp.parseCV(cv);
    }
}

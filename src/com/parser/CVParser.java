package com.parser;

import java.util.ArrayList;

public class CVParser {
    
    public CVObject parseCV(String filename) {
        PreProcessor pp = new PreProcessor();
        ArrayList<String> cv = pp.preprocess(filename);
        
        CVSectionParser sp = new CVSectionParser();
        return sp.parseCV(cv);
        
        
        /*SectionExtractor se = new SectionExtractor();
        try {
            return se.extractSections(filename);
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("An exception was thrown");
            e.printStackTrace();
        }*/
        
        //return null;
    }
    
    public static void main(String[] args) {
        CVParser cvp = new CVParser();
        cvp.parseCV("C:\\Users\\Manika\\Documents\\GitHub\\CVAnalyser\\sample\\YaminiBhaskar.txt");
        //JobDescParser jbp = new JobDescParser();
        //jbp.parseJobDesc("C:\\Users\\Manika\\Documents\\GitHub\\CVAnalyser\\sample\\jobdesc1.txt");
    }
}

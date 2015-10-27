package com.parser;

public class CVParser {
    
    public CVObject parseCV(String filename) {
        SectionExtractor se = new SectionExtractor();
        try {
            return se.extractSections(filename);
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("An exception was thrown");
            e.printStackTrace();
        }
        
        return null;
    }
}

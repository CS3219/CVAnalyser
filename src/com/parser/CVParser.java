package com.parser;

public class CVParser {
    
    public CVObject parseCV(String filename) {
        SectionsExtractor se = new SectionsExtractor();
        return se.extractSections(filename);
    }
}

package com.parser;

public class SectionHeader {
    private int lineNum;
    private String header;
    
    public SectionHeader(int lineNum, String header) {
        this.lineNum = lineNum;
        this.header = header;
    }
    
    public int getLineNum() {
        return this.lineNum;
    }
    
    public String getHeader() {
        return this.header;
    }
}

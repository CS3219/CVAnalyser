package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class PreProcessor {

    public ArrayList<String> preprocess(String filename) {
        BufferedReader br = null;
        ArrayList<String> lines = new ArrayList<String>();
        
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String curLine = null;
          
            //name shouldn't become small letters
            while ((curLine = br.readLine()) != null) {
                if (curLine.length() > 1) {
                    curLine = curLine.trim().replaceAll(" +", " ");
                    curLine = curLine.replaceAll("\t", "");
                    curLine = curLine + "\n";
                    lines.add(curLine);
                    break;
                }
            }

            while ((curLine = br.readLine()) != null) {
                if (curLine.length() > 1) {
                    curLine = process(curLine);
                    lines.add(curLine);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return lines;
    }

    private String process(String line) {
        line = line.trim().replaceAll(" +", " ");
        line = line.replaceAll("\t", "");
        line = line.replaceAll("[^\\x00-\\x7F]", "");
        line = line.toLowerCase();
        line = line + "\n";

        return line;
    }
}

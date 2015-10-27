package com.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class PreProcessor {

    public void preprocess(String filename) {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new FileReader(new File(filename)));


            String curLine = null;
            List<String> lines = new ArrayList<String>();

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

            bw = new BufferedWriter(new FileWriter(new File(filename)));

            for(String s : lines)
                bw.write(s);

            bw.flush();
            bw.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String process(String line) {
        line = line.trim().replaceAll(" +", " ");
        line = line.replaceAll("\t", "");
        line = line.toLowerCase();
        line = line + "\n";

        return line;
    }

    /*public static void main(String[] args) {
        PreProcessor p = new PreProcessor();
        p.preprocess("C:\\Users\\Manika\\Documents\\GitHub\\CVAnalyser\\sample\\resume1.txt");
    }*/
}

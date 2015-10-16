package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*;  

public class SectionsExtractor {

    //private static final String[] KEYWORDS = Arrays.asList("education", "work experience");
    private static final ArrayList<String> KEYWORDS = new ArrayList<String>
        (Arrays.asList("education", "skills"));
    private static final String KEYWORD_NAME = "name";
    
    private ArrayList<String> section = new ArrayList<String>(); 
    private String head = " "; 

    
    public CVObject extractSections(String filename) {
        BufferedReader br = null;
        SectionsParser sp = new SectionsParser();
        
        try {
            String curLine;
            
            br = new BufferedReader(new FileReader(new File(filename)));

            if ((curLine = br.readLine()) != null) {
                ArrayList<String> cur = new ArrayList<String>();
                cur.add(curLine);
                sp.parseSections(KEYWORD_NAME, cur);
                //head = KEYWORD_NAME;
                //section.add(curLine);
                //printSection();
                //section = new ArrayList<String>();
            }
            
            String header;
            
            while ((curLine = br.readLine()) != null) {
                header = parseSection(curLine);
                //System.out.println(header);
                if (header == null) {
                    section.add(curLine);
                } else if (header != head) {
                    if (!section.isEmpty()) {
                        sp.parseSections(head, section);
                        printSection();
                        head = header;
                        section = new ArrayList<String>();
                        //section.add(curLine);
                    }
                }
            }
            
            sp.parseSections(head, section);
            printSection();
            
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sp.getCVObject();
    }
    
    private String parseSection(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        
        if (tokenizer.hasMoreTokens()) {
            String firstWord = tokenizer.nextToken();
            //System.out.println(firstWord);
            if (containsCaseInsensitive(firstWord, KEYWORDS)) {
                return firstWord;
            }
        }
        return null;
    }
    
    private boolean containsCaseInsensitive(String strToCompare, ArrayList<String>list) {
        for(String str:list) {
            if(str.equalsIgnoreCase(strToCompare)) {
                return true;
            }
        }
        return false;
    }
    
    private void printSection() {
        /*System.out.println("header: " + head);
        
        for (int i = 0; i < section.size(); i++) {
            System.out.println(section.get(i));
        }*/
        
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text = section.get(0); 
        Annotation document = pipeline.process(text);  

        for(CoreMap sentence: document.get(SentencesAnnotation.class))
        {    
            for(CoreLabel token: sentence.get(TokensAnnotation.class))
            {       
                String word = token.get(TextAnnotation.class);      
                String lemma = token.get(LemmaAnnotation.class); 
                System.out.println("lemmatized version :" + lemma);
            }
        }

        
    }
}

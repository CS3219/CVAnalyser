package com.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

import com.joestelmach.natty.*;

public class CVSectionParser {

    private static final ArrayList<String> KEYWORDS = new ArrayList<String>
            (Arrays.asList("education", "work experience", "skills", "language", 
             "interests", "refere", "CCA", "extracurricular activities", "volunteer", 
             "publication", "paper", "project")); 
    
    private static final ArrayList<String> PARAMS = new ArrayList<String>
            (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));
    
    private ArrayList<String> CV;

    public CVObject parseCV(ArrayList<String> cv) {
        CV = cv;
        SectionExtractor se = new SectionExtractor();
        ArrayList<SectionHeader> sectionIndices = new ArrayList<SectionHeader>();

        sectionIndices = se.extractSections(CV, KEYWORDS);

        /*for (int i = 0; i < sectionIndices.size(); i++) {
            System.out.println("index = "+sectionIndices.get(i).getLineNum());
            System.out.println("header = "+sectionIndices.get(i).getHeader());
        }*/

        CVObject cvobj = new CVObject();
        cvobj.setName(CV.get(sectionIndices.get(0).getLineNum()));

        parseSections(sectionIndices, cvobj);

        return cvobj;
    }

    private void parseSections(ArrayList<SectionHeader> sectionIndices, CVObject cvobj) {
        StanfordCoreNLP pipeline = initialisePipeline();

        for (int i = 1; i < sectionIndices.size(); i++) {
            String header = sectionIndices.get(i).getHeader();
            ArrayList<String> section = getSection(sectionIndices, i), 
                    parsedSection = new ArrayList<String>();
            //System.out.println("header = "+header);

            for (int j = 0; j < section.size(); j++) {
                String text = section.get(j); 
                String line = "";
                
                Annotation document = new Annotation(text);
                pipeline.annotate(document);
                List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                for(CoreMap sentence: sentences) {
                    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                        String word = token.get(TextAnnotation.class);
                        String pos = token.get(PartOfSpeechAnnotation.class);
                        //String lemma = token.get(LemmaAnnotation.class);    
                        //String ne = token.get(NamedEntityTagAnnotation.class); 
                        //System.out.println(word+" "+pos);

                        line = setLine(header, parsedSection, line,
                                word, pos);
                    }
                }                
                //System.out.println("line = "+line);
                if (line.length() > 1) {
                    parsedSection.add(line);
                }
            }

            setSection(cvobj, header, parsedSection);
        }
    }

    private void setSection(CVObject cvobj, String header,
            ArrayList<String> parsedSection) {
        if (header == KEYWORDS.get(0)) {
            cvobj.setEducation(parsedSection);
        } else if (header == KEYWORDS.get(1) || header == KEYWORDS.get(9) || header == KEYWORDS.get(10) || 
                header == KEYWORDS.get(11)) { 

            parseExpObj(cvobj, header, parsedSection);

        } else if (header == KEYWORDS.get(2)) {
            /*for (int m = 0; m < parsedSection.size(); m++) {
                System.out.println(parsedSection.get(m));
            }*/
            cvobj.setSkills(parsedSection);
        } /*else if (header == KEYWORDS.get(9) || header == KEYWORDS.get(9) || 
                header == KEYWORDS.get(9)) {

        } */else if (header == KEYWORDS.get(5)) {
            cvobj.setHasReferences();
        } else if (header == KEYWORDS.get(8)) {
            cvobj.setHasVolnteerExp();
        } else if (header == KEYWORDS.get(3)) {
            //System.out.println("lang");
            /*for (int l = 0; l < parsedSection.size(); l++) {
                System.out.println("language:" + parsedSection.get(l));
            }*/

            cvobj.setLanguages(parsedSection);
        }
    }

    private String setLine(String header, ArrayList<String> parsedSection, 
            String line, String word, String pos) {
        if (header == KEYWORDS.get(1)) {
            if (PARAMS.contains(pos) || word.equals("-") || word.equals("to")) {
                //System.out.println("lemmatized version: "+lemma);
                line += word + " ";
            }
        } else if (header == KEYWORDS.get(2) || header == KEYWORDS.get(3)) {
            //System.out.println("lemmatized version: "+lemma+" "+pos);
            if (PARAMS.contains(pos) && !word.equals("fluent") && !word.equals("proficient")
                    && !word.equals("reading") && !word.equals("speaking") 
                    && !word.equals("writing")) {

                line += word + " ";
            } else if (word.equals(",") || word.equals("and")) {
                if (line.length() > 1) {
                    parsedSection.add(line);
                }
                line = "";
            }
        } else {
            if (PARAMS.contains(pos)) {
                line += word + " ";
            }
        }
        return line;
    }

    private ArrayList<String> getSection(
            ArrayList<SectionHeader> sectionIndices, int i) {
        ArrayList<String> section;
        if (i != sectionIndices.size() - 1) {
            section = new ArrayList<String>(CV.subList(sectionIndices.get(i).getLineNum() + 1, 
                    sectionIndices.get(i + 1).getLineNum()));
        } else {
            section = new ArrayList<String>(CV.subList(sectionIndices.get(i).getLineNum() + 1, 
                    CV.size()));
        }
        return section;
    }

    private void parseExpObj(CVObject cvobj, String header,
            ArrayList<String> parsedSection) {
        
        ArrayList<ExpObject> expArr = new ArrayList<ExpObject>();
        ExpObject exp = new ExpObject();
        ArrayList<String> job = new ArrayList<String>();

        Parser parser = new Parser();
        for (int k = 0; k < parsedSection.size(); k++) {
            String line = parsedSection.get(k);
            //System.out.println(line);
            List<DateGroup> groups = parser.parse(line);

            if (groups.size() > 0) {
                double duration = getDuration(groups);
                //System.out.println(startYear+" "+ endYear + " "+ startMonth + " "+ endMonth + " ");

                if (header == KEYWORDS.get(1)){
                    if (!job.isEmpty()) {
                        //System.out.println("yes "+k);
                        exp.setDesc(job);
                        expArr.add(exp);
                        job = new ArrayList<String>();
                        exp = new ExpObject();
                    }

                    exp.setDuration(duration);
                    line = removeDates(line, parser);
                    job.add(line);
                } else {
                    exp.setDuration(duration);
                    exp.setDesc(job);
                    expArr.add(exp);
                    job = new ArrayList<String>();
                    exp = new ExpObject();
                }
                //System.out.println("line = "+line);
            } else {
                if (header == KEYWORDS.get(1)){
                    job.add(line);
                }
            }

        }

        if (header == KEYWORDS.get(1)){
            exp.setDesc(job);
            expArr.add(exp);
        }
        /*System.out.println("header = "+header);
        for (int l = 0; l < expArr.size(); l++) {
            System.out.println("desc:" + expArr.get(l).getDescription());
            System.out.println("dur:" + expArr.get(l).getDuration());
        }*/

        if (header == KEYWORDS.get(1)){
            cvobj.setWorkExp(expArr);
        } else if (header == KEYWORDS.get(9) || header == KEYWORDS.get(10)){
            cvobj.setPublications(expArr);
        } else if (header == KEYWORDS.get(11)){
            cvobj.setProjects(expArr);
        }
    }

    private double getDuration(List<DateGroup> groups) {
        DateGroup dateGroup = groups.get(0);
        List<Date> dates = dateGroup.getDates();

        Date start = dates.get(0);
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(start);
        int startYear = calStart.get(Calendar.YEAR);
        int startMonth = calStart.get(Calendar.MONTH);

        Date end = new Date();
        if (dates.size() == 2) {
            end = dates.get(1);
        } 

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        int endYear = calEnd.get(Calendar.YEAR);
        int endMonth = calEnd.get(Calendar.MONTH);

        double duration = (endYear - startYear) + (endMonth - (startMonth-1))/12.0;
        return duration;
    }

    private String removeDates(String line, Parser parser) {
        StringTokenizer tokens = new StringTokenizer(line);
        String result = "";

        while (tokens.hasMoreTokens()) {
            String word = tokens.nextToken();
            List<DateGroup> groups = parser.parse(word);

            if (groups.size() == 0) {
                result += word + " ";
            }
        }

        return result.trim();
    }

    private StanfordCoreNLP initialisePipeline() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");//, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        return pipeline;
    }
}

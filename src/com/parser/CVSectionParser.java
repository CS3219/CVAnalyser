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
            "publication", "paper", "project", "certificat", "experience", "awards")); 

    private static final ArrayList<String> PARAMS = new ArrayList<String>
    (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));

    private static final ArrayList<String> IGNORE_LANGUAGE = new ArrayList<String>
    (Arrays.asList("fluent", "proficient", "reading", "speaking", "writing", 
            "native", "bilingual", "proficiency", "full", "professional"));

    private static final ArrayList<String> MONTHS = new ArrayList<String>
    (Arrays.asList("jan", "feb", "mar", "apr", "may", 
            "jun", "jul", "aug", "sept", "oct", "nov", "dec"));

    private ArrayList<String> CV;

    public CVObject parseCV(ArrayList<String> cv) {
        CV = cv;
        SectionExtractor se = new SectionExtractor();
        ArrayList<SectionHeader> sectionIndices = new ArrayList<SectionHeader>();

        sectionIndices = se.extractSections(CV, KEYWORDS);

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

            String line = "";

            for (int j = 0; j < section.size(); j++) {
                String text = section.get(j);     

                Annotation document = new Annotation(text);
                pipeline.annotate(document);
                List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                for(CoreMap sentence: sentences) {
                    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                        String word = token.get(TextAnnotation.class);
                        String pos = token.get(PartOfSpeechAnnotation.class);
                        line = addToLine(header, parsedSection, line,
                                word, pos);
                    }
                }                

                if ((header != KEYWORDS.get(1) && header != KEYWORDS.get(9) && 
                        header != KEYWORDS.get(10) && header != KEYWORDS.get(11) && 
                        header != KEYWORDS.get(13)) 
                        && line.length() > 1) {
                    parsedSection.add(line);
                    line = "";
                }
            }

            setSection(cvobj, header, parsedSection);
        }
    }

    private String addToLine(String header, ArrayList<String> parsedSection, 
            String line, String word, String pos) {

        if (header == KEYWORDS.get(1) || header == KEYWORDS.get(9) || 
                header == KEYWORDS.get(10) || header == KEYWORDS.get(11) || 
                header == KEYWORDS.get(13)) {
            if (PARAMS.contains(pos) || word.equals("-") || word.equals("to") || 
                    word.equals(".") || isMonth(word)) {
                if (word.equals(".")) {
                    if (line.length() > 1) {
                        parsedSection.add(line);
                        line = "";
                    }
                } else {
                    line += word + " ";
                }
            }
        } else if (header == KEYWORDS.get(2) || header == KEYWORDS.get(3)) {
            if (PARAMS.contains(pos) && !IGNORE_LANGUAGE.contains(word)) {
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

    private boolean isMonth(String word) {
        for(String str: MONTHS) {
            if(word.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    private void setSection(CVObject cvobj, String header,
            ArrayList<String> parsedSection) {
        if (header == KEYWORDS.get(0)) {
            cvobj.setEducation(parsedSection);
        } else if (header == KEYWORDS.get(1) || header == KEYWORDS.get(9) || 
                header == KEYWORDS.get(10) || header == KEYWORDS.get(11) || 
                header == KEYWORDS.get(13)) { 

            parseExpObj(cvobj, header, parsedSection);

        } else if (header == KEYWORDS.get(2)) {
            cvobj.setSkills(parsedSection);
        } else if (header == KEYWORDS.get(5)) {
            cvobj.setHasReferences();
        } else if (header == KEYWORDS.get(8)) {
            cvobj.setHasVolnteerExp();
        } else if (header == KEYWORDS.get(3)) {
            cvobj.setLanguages(parsedSection);
        }
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

            List<DateGroup> groups = parser.parse(line);

            if (groups.size() > 0) {
                double duration = getDuration(groups);

                if (header == KEYWORDS.get(1) || header == KEYWORDS.get(13) || 
                        header == KEYWORDS.get(11)){
                    if (!job.isEmpty()) {
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
            } else {
                if (header == KEYWORDS.get(1) || header == KEYWORDS.get(13)|| 
                        header == KEYWORDS.get(11)){
                    job.add(line);
                }
            }

        }

        if (header == KEYWORDS.get(1) || header == KEYWORDS.get(9) || 
                header == KEYWORDS.get(10) || header == KEYWORDS.get(11) || 
                header == KEYWORDS.get(13)){
            exp.setDesc(job);
            expArr.add(exp);
        }
        if (header == KEYWORDS.get(1) || header == KEYWORDS.get(13)){
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
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        return pipeline;
    }
}

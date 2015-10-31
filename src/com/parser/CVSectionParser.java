package com.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

import com.joestelmach.natty.*;

public class CVSectionParser {

    private static final ArrayList<String> KEYWORDS = new ArrayList<String>
    (Arrays.asList("education", "work experience", "skills", "languages", "interests", 
            "refere", "CCA", "extracurricular activities", "volunteer")); 
    private ArrayList<String> CV;

    public CVObject parseCV(ArrayList<String> cv) {
        CV = cv;
        SectionExtractor se = new SectionExtractor();
        ArrayList<SectionHeader> sectionIndices = new ArrayList<SectionHeader>();

        sectionIndices = se.extractSections(CV, KEYWORDS);

        for (int i = 0; i < sectionIndices.size(); i++) {
            System.out.println("index = "+sectionIndices.get(i).getLineNum());
            System.out.println("header = "+sectionIndices.get(i).getHeader());
        }

        CVObject cvobj = new CVObject();
        cvobj.setName(CV.get(sectionIndices.get(0).getLineNum()));

        parseSections(sectionIndices, cvobj);

        return cvobj;
    }

    private void parseSections(ArrayList<SectionHeader> sectionIndices, CVObject cvobj) {
        StanfordCoreNLP pipeline = initialisePipeline();
        /*ArrayList<String> PARAMS_ALL = new ArrayList<String> 
            (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));*/
        ArrayList<String> PARAMS = new ArrayList<String> 
        (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));
        ArrayList<String> extraWords = new ArrayList<String> 
        (Arrays.asList("and", ","));

        for (int i = 1; i < sectionIndices.size(); i++) {
            String header = sectionIndices.get(i).getHeader();
            ArrayList<String> section, parsedSection = new ArrayList<String>();
            int parsedSecLineNum = 0;

            if (i != sectionIndices.size() - 1) {
                section = new ArrayList<String>(CV.subList(sectionIndices.get(i).getLineNum() + 1, 
                        sectionIndices.get(i + 1).getLineNum()));
            } else {
                section = new ArrayList<String>(CV.subList(sectionIndices.get(i).getLineNum() + 1, 
                        CV.size() - 1));
            }

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
                        //String ne = token.get(NamedEntityTagAnnotation.class); 
                        //System.out.println(word+" "+pos);

                       if (header == KEYWORDS.get(1)) {
                            if (PARAMS.contains(pos) || word.equals("-") || word.equals("to")) {
                                line += word + " ";
                            }
                        } else if (header == KEYWORDS.get(2)) {
                            if (PARAMS.contains(pos)) {// || word.equals(",") || word.equals("to")) {
                                line += word + " ";
                            } else if (word.equals(",") || word.equals("and")) {
                                parsedSection.add(line);
                                line = "";
                            }
                        } else {
                            if (PARAMS.contains(pos)) {
                                line += word + " ";
                            }
                        }
                        
                        
                    }
                }

                //System.out.println("line = "+line);
                if (line.length() > 1) {
                    parsedSection.add(line);
                }
                
            }

            if (header == KEYWORDS.get(0)) {
                cvobj.setEducation(parsedSection);
            } else if (header == KEYWORDS.get(1)) { 
                ArrayList<WorkExpObject> workExpArr = new ArrayList<WorkExpObject>();
                WorkExpObject workExp = new WorkExpObject();
                ArrayList<String> job = new ArrayList<String>();
                
                Parser parser = new Parser();
                int count = 0;
                for (int k = 0; k < parsedSection.size(); k++) {
                    String line = parsedSection.get(k);
                    //System.out.println(line);
                    List<DateGroup> groups = parser.parse(line);

                    if (groups.size() > 0) {
                        count++;
                        DateGroup dateGroup = groups.get(0);
                        List<Date> dates = dateGroup.getDates();
                        
                        Date start = dates.get(0);
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(start);
                        int startYear = cal1.get(Calendar.YEAR);
                        int startMonth = cal1.get(Calendar.MONTH);
                        
                        Date end = new Date();
                        if (dates.size() == 2) {
                            end = dates.get(1);
                        } 
                        
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(end);
                        int endYear = cal2.get(Calendar.YEAR);
                        int endMonth = cal2.get(Calendar.MONTH);
                        
                        double duration = (endYear - startYear) + (endMonth - (startMonth-1))/12.0;
                        //System.out.println(startYear+" "+ endYear + " "+ startMonth + " "+ endMonth + " ");
                        if (!job.isEmpty()) {
                            //System.out.println("yes "+k);
                            workExp.setDesc(job);
                            workExpArr.add(workExp);
                            job = new ArrayList<String>();
                            workExp = new WorkExpObject();
                        }
                        
                        workExp.setDuration(duration);
                        job.add(line);
                        //System.out.println("line = "+line);
                    } else {
                        job.add(line);
                    }

                }

                workExp.setDesc(job);
                workExpArr.add(workExp);
                //System.out.println("count = "+count);
                /*for (int l = 0; l < workExpArr.size(); l++) {
                    System.out.println("desc:" + workExpArr.get(l).getDescription());
                    System.out.println("dur:" + workExpArr.get(l).getDuration());
                }*/
                cvobj.setWorkExp(workExpArr);
            } else if (header == KEYWORDS.get(2)) {
                /*for (int m = 0; m < parsedSection.size(); m++) {
                    System.out.println(parsedSection.get(m));
                }*/
                cvobj.setSkills(parsedSection);
            } else if (header == KEYWORDS.get(5)) {
                cvobj.setHasReferences();
            }
        }
    }

    private StanfordCoreNLP initialisePipeline() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");//, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        return pipeline;
    }
}

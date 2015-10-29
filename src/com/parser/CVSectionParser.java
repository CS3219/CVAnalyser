package com.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

public class CVSectionParser {

    private static final ArrayList<String> KEYWORDS = new ArrayList<String>
            (Arrays.asList("education", "skills", "languages", "work experience", "interests", 
                    "referees", "CCA", "extracurricular activities")); 
    private ArrayList<String> CV;
    
    public CVObject parseCV(ArrayList<String> cv) {
        CV = cv;
        SectionExtractor se = new SectionExtractor();
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();

        sectionIndices = se.extractSections(CV, KEYWORDS);

        /*for (int i = 0; i < sectionIndices.size(); i++) {
            System.out.println("index = "+sectionIndices.get(i));
        }*/
        
        CVObject cvobj = new CVObject();
        cvobj.setName(CV.get(sectionIndices.get(0)));
        
        parseSections(sectionIndices);
     
        return cvobj;
    }

    private void parseSections(ArrayList<Integer> sectionIndices) {
        StanfordCoreNLP pipeline = initialisePipeline();
        ArrayList<String> PARAMS = new ArrayList<String> 
                (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD", "JJ"));
        
        for (int i = 1; i < sectionIndices.size(); i++) {
            String header = CV.get(sectionIndices.get(i));
            ArrayList<String> section;
            
            if (i != sectionIndices.size() - 1) {
                section = new ArrayList<String>(CV.subList(sectionIndices.get(i) + 1, 
                        sectionIndices.get(i + 1)));
            } else {
                section = new ArrayList<String>(CV.subList(sectionIndices.get(i) + 1, 
                        CV.size() - 1));
            }
            
            for (int j = 0; j < section.size(); j++) {
                // read some text in the text variable
                String text = section.get(j); 
                String line = "";

                // create an empty Annotation just with the given text
                Annotation document = new Annotation(text);

                // run all Annotators on this text
                pipeline.annotate(document);

                // these are all the sentences in this document
                // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
                List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                for(CoreMap sentence: sentences) {
                    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                        String word = token.get(TextAnnotation.class);
                        String pos = token.get(PartOfSpeechAnnotation.class);
                        // this is the NER label of the token
                        //String ne = token.get(NamedEntityTagAnnotation.class); 
                        //System.out.println(word+" "+pos);
                        
                        if (PARAMS.contains(pos)) {
                            //System.out.println("yes");
                            //System.out.println(word+pos);
                            line += word + " ";
                        }     
                    }
                }
                
                if (line.length() > 1) {
                    System.out.println("section = "+section.get(j));
                    System.out.println("line = "+line);
                    section.set(j, line);
                }
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

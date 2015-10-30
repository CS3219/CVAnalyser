package com.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*; 

public class SectionExtractor {

    //private static final String[] KEYWORDS = Arrays.asList("education", "work experience");
    /*private static final ArrayList<String> KEYWORDS_CV = new ArrayList<String>
            (Arrays.asList("education", "skills", "languages", "work experience", "interests", "referees", 
                "CCA", "extracurricular activities")); 
    private static final ArrayList<String> KEYWORDS_JOBDESC = new ArrayList<String>
            (Arrays.asList("minimal requirements")); //to be completed*/
    
    private static final String KEYWORD_NAME = "name";
    private static final ArrayList<String> PARAMS = new ArrayList<String>
            (Arrays.asList("NN", "NNS", "NNP", "NNPS", "CD"));

    //private ArrayList<String> section = new ArrayList<String>(); 
    //private String head = " "; 

    public ArrayList<SectionHeader> extractSections(ArrayList<String> file, ArrayList<String> KEYWORDS) {
        int i = 0, countSecLines = 0;
        ArrayList<SectionHeader> sectionIndices = new ArrayList<SectionHeader>();
        //String head = null;
        
        sectionIndices.add(new SectionHeader(i, KEYWORD_NAME)); //name
        i++;
        
        for (; i < file.size(); i++) {
            String header = parseSection(file.get(i), KEYWORDS);

            if (header == null) {
                countSecLines++;
            } else {
                if (countSecLines != 0) {
                    countSecLines = 0;
                    sectionIndices.add(new SectionHeader(i, header));
                    //System.out.println("index = "+i);
                }
            }
        }
        
        return sectionIndices;
    }
    
    
    /*public CVObject extractSections(String filename) throws ClassCastException, ClassNotFoundException {
        BufferedReader br = null;
        SectionParser sp = new SectionParser();

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
                
                if (header == null) {
                    section.add(curLine);
                } else if (header != head) {
                    if (!section.isEmpty()) {
                        sp.parseSections(head, section);
                        //System.out.println("header == "+header);
                        //printSection();
                        head = header;
                        section = new ArrayList<String>();
                        //section.add(curLine);
                    }
                }
            }

            sp.parseSections(head, section);
            //printSection();

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sp.getCVObject();
    }*/

    /*private String parseSection(String line, ArrayList<String> KEYWORDS) {
        //System.out.println("line = ."+line.trim()+".");
        //System.out.println("size = "+line.length());
        String checkedLine;
        if ((checkedLine = containsCaseInsensitive(line, KEYWORDS)) != null) {
            //System.out.println("yes line = ."+line.trim()+".");
            return checkedLine;
        }
        
        /*StringTokenizer tokenizer = new StringTokenizer(line);

        if (tokenizer.hasMoreTokens()) {
            String firstWord = tokenizer.nextToken();
            //System.out.println(firstWord);
            if (containsCaseInsensitive(firstWord, KEYWORDS)) {
                return firstWord;
            }
        }*/
        //return null;
    //}

    private String parseSection(String strToCompare, ArrayList<String>list) {
        for(String str:list) {
            if(str.toLowerCase().contains(strToCompare) || strToCompare.toLowerCase().contains(str)) {
                return str;
            }
        }
        return null;
    }

    private void printSection() throws ClassCastException, ClassNotFoundException, IOException {
        //System.out.println("header: " + head);

        /*for (int i = 0; i < section.size(); i++) {
            System.out.println(section.get(i));
        }*/

        /*Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");//, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        for (int i = 0; i < section.size(); i++) {
            // read some text in the text variable
            String text = section.get(i); 
            String line = "";

            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);

            for(CoreMap sentence: sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                    // this is the text of the token
                    String word = token.get(TextAnnotation.class);
                    // this is the POS tag of the token
                    String pos = token.get(PartOfSpeechAnnotation.class);
                    // this is the NER label of the token
                    //String ne = token.get(NamedEntityTagAnnotation.class); 
                    //System.out.println(word+pos);
                    if (PARAMS.contains(pos)) {
                        //System.out.println("yes");
                       // System.out.println(word+pos);
                        line += word + " ";
                    }
                    
                }

                // this is the parse tree of the current sentence
                //Tree tree = sentence.get(TreeAnnotation.class);
                // System.out.println(tree);

                // this is the Stanford dependency graph of the current sentence
                //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            }
            
            if (line.length() > 1) {
                System.out.println("line = "+line);
                section.set(i, line);
            }
        }*/
    }
}

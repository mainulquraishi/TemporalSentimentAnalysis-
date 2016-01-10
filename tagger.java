package star_extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class tagger {
	 public static void main(String[] argv) throws ClassNotFoundException, IOException{
		 long startTime = System.currentTimeMillis();
		 List<String> fileList = new ArrayList<String>();


		 File[] files = new File("E:\\sentiment_directory\\1999\\Books\\positive").listFiles();
		 //If this pathname does not denote a directory, then listFiles() returns null. 

		 for (File file : files) {
		     if (file.isFile()) {
		         fileList.add(file.getName());
		     }
		 }
		 
	Collections.shuffle(fileList);
		 

	
	// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	 Properties props = new Properties();
	 props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
	 StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	 for(int i=0; i<5000;i++){
		 
	 
String text=null;
String line=null;
BufferedReader br = new BufferedReader(new FileReader("E:\\sentiment_directory\\1999\\Books\\positive\\"+fileList.get(i)));
	 while ((line = br.readLine()) != null){
	  if(text==null)
		  text=line;
	  else
		  text=text+line;
	  
	    }

	 // create an empty Annotation just with the given text
	 Annotation document = new Annotation(text);

	 // run all Annotators on this text
	 pipeline.annotate(document);
	 PrintWriter os=new PrintWriter("E:\\sentiment_directory\\1999\\Books\\tagged_positive\\pos_tagged_"+fileList.get(i));
	 pipeline.prettyPrint(document, os);
	
	 
	 
	 }
	 long endTime   = System.currentTimeMillis();
	 long totalTime = endTime - startTime;
	 System.out.println(totalTime);
        }
	
	 }

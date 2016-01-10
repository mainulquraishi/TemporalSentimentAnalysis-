package star_extractor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Adjective_Counter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// List <Long> conter=new ArrayList<Long>();
		long startTime = System.currentTimeMillis();
		Map<String, Map<String, ArrayList<Long>>> mainMap = new HashMap<String, Map<String, ArrayList<Long>>>();
		String sentiment = null;
		List<String> stopwords = Arrays.asList("i", "me", "my", "myself", "we", "our", "ourselves", "you", "your",
				"yourself", "yourselves", "he", "his", "him", "himself", "she", "her", "hers", "herself", "it", "its",
				"itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this",
				"that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had",
				"having", "do", "did", "doees", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as",
				"untill", "while", "of", "at", "by", "for", "with", "against", "about", "between", "against", "into",
				"through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on",
				"off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why",
				"how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not",
				"only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should",
				"now", "'s", ".", ",", "/", ";", "?", "!", "``", "'", "\\", "[", "]", "-", "_", "=", "+", "/", "<", ">",
				"#", "%");

		Counter<String> adjectivePositiveCounts = new ClassicCounter<String>();
		Counter<String> adjectiveNegativeCounts = new ClassicCounter<String>();
		List<String> negfileList = new ArrayList<String>();
		List<String> posfileList = new ArrayList<String>();

		File[] posfiles = new File("E:\\sentiment_directory\\2003\\Books\\positive").listFiles();
		File[] negfiles = new File("E:\\sentiment_directory\\2003\\Books\\negative").listFiles();
		// If this pathname does not denote a directory, then listFiles()
		// returns null.

		for (File file : posfiles) {
			if (file.isFile()) {
				posfileList.add(file.getName());
			}
		}

		for (File file : negfiles) {
			if (file.isFile()) {
				negfileList.add(file.getName());
			}
		}

		Collections.shuffle(posfileList);
		Collections.shuffle(negfileList);

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		PrintWriter os;
		for (int j = 0; j < 2; j++) {
			List fileList;
			if (j == 0) {
				sentiment = "positive";
				fileList = posfileList;
			} else {
				sentiment = "negative";
				fileList = negfileList;
			}
			for (int i = 0; i < 5000; i++) {
				String text = null;
				String line = null;
				BufferedReader br = new BufferedReader(
						new FileReader("E:\\sentiment_directory\\2003\\Books\\" + sentiment + "\\" + fileList.get(i)));
				while ((line = br.readLine()) != null) {
					if (text == null)
						text = line;
					else
						text = text + line;

				}
				Annotation document = new Annotation(text);
				pipeline.annotate(document);
				if (j == 0)
					os = new PrintWriter(
							"E:\\sentiment_directory\\2003\\Books\\tagged_positive\\pos_tagged_" + fileList.get(i));
				else
					os = new PrintWriter(
							"E:\\sentiment_directory\\2003\\Books\\tagged_negative\\neg_tagged_" + fileList.get(i));
				pipeline.prettyPrint(document, os);
				// System.out.println(document);

				for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
					for (CoreLabel cl : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
						String pos = cl.get(CoreAnnotations.PartOfSpeechAnnotation.class);

						String word = (cl.word());
						word = word.toLowerCase();
						if (stopwords.contains(word))
							continue;
						else {
							if (mainMap.containsKey(word)) {
								Map tempMap1 = mainMap.get(word);
								if (tempMap1.containsKey(pos)) {
									ArrayList<Long> tempArrayList1 = (ArrayList) tempMap1.get(pos);
									if (sentiment.equals("positive"))
										tempArrayList1.set(0, (long) tempArrayList1.get(0) + 1);
									else
										tempArrayList1.set(1, (long) tempArrayList1.get(1) + 1);

									tempMap1.put(pos, tempArrayList1);
									mainMap.put(word, tempMap1);
								} else {
									ArrayList<Long> tempArrayList3 = new ArrayList<Long>();
									if (sentiment.equals("positive")) {
										tempArrayList3.add((long) 1.0);
										tempArrayList3.add((long) 0.0);
									} else {
										tempArrayList3.add((long) 0.0);
										tempArrayList3.add((long) 1.0);
									}
									tempMap1.put(pos, tempArrayList3);
									mainMap.put(word, tempMap1);
								}
							} else {
								Map<String, ArrayList<Long>> tempMap2 = new HashMap<String, ArrayList<Long>>();
								ArrayList<Long> tempArrayList2 = new ArrayList<Long>();
								if (sentiment.equals("positive")) {
									tempArrayList2.add((long) 1.0);
									tempArrayList2.add((long) 0.0);
								} else {
									tempArrayList2.add((long) 0.0);
									tempArrayList2.add((long) 1.0);
								}
								tempMap2.put(pos, tempArrayList2);
								mainMap.put(word, tempMap2);
							}
						}

					}
				}
			}
		}
		System.out.println(mainMap);
		long endTimeOfMapping = System.currentTimeMillis();
		long MappingTime = endTimeOfMapping - startTime;
		System.out.println("Mapping Time:" + MappingTime);
		/*
		 * System.out.println(mainMap); Connection connection = null; String url
		 * = "jdbc:mysql://localhost/star_count"; String user = "root"; String
		 * password = ""; connection = DriverManager.getConnection(url, user,
		 * password); Statement stmt = connection.createStatement();
		 * 
		 * String sql = "INSERT INTO star_count." + entry.getKey() +
		 * " VALUES ('Kindle_Store'," + "'" + map1.get(1) + "'," + "'" +
		 * map1.get(2) + "'," + "'" + map1.get(3) + "'," + "'" + map1.get(4) +
		 * "'," + "'" + map1.get(5) + "'," + "'" + map1.get(0) + "'" + ")";
		 * System.out.println(sql); stmt.execute(sql);
		 */
		Connection connection = null;
		String url = "jdbc:mysql://localhost/word_count";
		String user = "root";
		String password = "";
		connection = DriverManager.getConnection(url, user, password);
		Statement stmt = connection.createStatement();
		for (String word : mainMap.keySet()) {
			Map<String, ArrayList<Long>> posMap = mainMap.get(word);
			for (String pos : posMap.keySet()) {
				ArrayList posNegList = posMap.get(pos);
				if (word.contains("\""))
					word = word.replaceAll("\"", "");
				if (word.contains("'"))
					word = word.replaceAll("\'", "");
				if (word.contains("."))
					word = word.replaceAll("\\.", "");

				String sql = "INSERT INTO word_count.2003_book_word VALUES ('" + word + "'," + "'" + pos + "'," + "'"
						+ posNegList.get(0) + "'," + "'" + posNegList.get(1) + "'" + ")";
				stmt.execute(sql);

			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total Time:" + totalTime);
	}

}

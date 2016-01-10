package star_extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Directory_Maker {
	  public static void main(String[] argv) throws IOException, SQLException, ParseException {
		    String FILENAME = "E:\\amazon data\\without_duplicate\\reviews_Books.json.gz";
	        FileInputStream fin = new FileInputStream(FILENAME);
		    GZIPInputStream gzis = new GZIPInputStream(fin);
		    InputStreamReader xover = new InputStreamReader(gzis);
		    String line;
		    Integer year;
		    String review_text;
		    BufferedReader is = new BufferedReader(xover);
		    JSONParser parser=new JSONParser();
		    Calendar mydate = Calendar.getInstance();
		    
		    while ((line = is.readLine()) != null){
		    	JSONObject jsob=(JSONObject) parser.parse(line);
		    	double score= (double) jsob.get("overall");
		    	long unixtime=(long) jsob.get("unixReviewTime");
		    	mydate.setTimeInMillis(unixtime*1000);
		    	year=mydate.get(Calendar.YEAR);
		    	review_text=(String) jsob.get("reviewText");
		    	String revietime=(String) jsob.get("reviewTime");
		    	String product_id=(String) jsob.get("asin");
		    	
		    	File file = null;
		    	if(score<3)
		            file = new File("E:\\sentiment_directory\\"+year+"\\Books\\"+"negative\\"+product_id+".txt");
		    	else if(score>3)
		    		file = new File("E:\\sentiment_directory\\"+year+"\\Books\\"+"positive\\"+product_id+".txt");
		    	else
		    		continue;
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
        
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(review_text);
				bw.close();

		    	
		    }
		        
	  }

}

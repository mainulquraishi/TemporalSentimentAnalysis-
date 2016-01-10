package star_extractor;

import java.io.File;

public class directory {
	 public static void main(String[] argv){
		 for(int i=1995;i<=2014;i++){
		 File file1 = new File("E:\\sentiment_directory\\"+i+"\\Books\\positive");
		 File file2 = new File("E:\\sentiment_directory\\"+i+"\\Books\\negative");
		 File file3 = new File("E:\\sentiment_directory\\"+i+"\\Electronics\\positive");
		 File file4 = new File("E:\\sentiment_directory\\"+i+"\\Electronics\\negative");
		 File file5 = new File("E:\\sentiment_directory\\"+i+"\\Movie_&_TV\\positive");
		 File file6 = new File("E:\\sentiment_directory\\"+i+"\\Movie_&_TV\\negative");
		 
				if(file1.mkdirs()){
					System.out.println("created");
				}
				else
					System.out.println("prob");
				file2.mkdirs();
				file3.mkdirs();
				file4.mkdirs();
				file5.mkdirs();
				file6.mkdirs();
			
				
					
				
			}
		 }
	 }


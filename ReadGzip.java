package star_extractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ReadGzip {
	public static void main(String[] argv) throws IOException, SQLException {
		String FILENAME = "C:\\Users\\Mainul Quraishi\\Downloads\\reviews_Beauty.json.gz";
		FileInputStream fin = new FileInputStream(FILENAME);
		GZIPInputStream gzis = new GZIPInputStream(fin);
		InputStreamReader xover = new InputStreamReader(gzis);
		BufferedReader is = new BufferedReader(xover);
		Map<Integer, HashMap<Integer, Integer>> map = new HashMap<Integer, HashMap<Integer, Integer>>();
		String line;
		String[] arr = null;
		Integer score = 0;
		Integer year;
		Calendar mydate = Calendar.getInstance();

		while ((line = is.readLine()) != null) {
			String a = "hgf";
			continue;
		}

		while ((line = is.readLine()) != null) {
			arr = line.split("[:]");
			if (arr[0].equals("review/score")) {
				arr[1] = arr[1].replaceAll("\\s+", "");
				float tempFloat = Float.parseFloat(arr[1]);
				score = Math.round(tempFloat);
				// System.out.println(score);

			}
			if (arr[0].equals("review/time")) {
				arr[1] = arr[1].replaceAll("\\s+", "");
				mydate.setTimeInMillis(Long.parseLong(arr[1]) * 1000);
				year = mydate.get(Calendar.YEAR);
				if (year.equals(1970)) {
					score = 0;
					continue;
				}
				if (!map.containsKey(year)) {
					Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
					tempMap.put(1, 0);
					tempMap.put(2, 0);
					tempMap.put(3, 0);
					tempMap.put(4, 0);
					tempMap.put(5, 0);
					tempMap.put(0, 0);
					if (!score.equals(0)) {
						tempMap.put(score, tempMap.get(score) + 1);
					}
					if (score.equals(0)) {
						tempMap.put(0, tempMap.get(0) + 1);
					}
					map.put(year, (HashMap<Integer, Integer>) tempMap);

				} else {
					Map tempMap1 = map.get(year);
					tempMap1.put(score, (int) tempMap1.get(score) + 1);
					map.put(year, (HashMap<Integer, Integer>) tempMap1);

				}
				score = 0;
				// System.out.println(year);
			}
		}
		// System.out.println(map);
		Connection connection = null;
		String url = "jdbc:mysql://localhost/star_count";
		String user = "root";
		String password = "";
		connection = DriverManager.getConnection(url, user, password);
		Statement stmt = connection.createStatement();
		for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue());
			Map<Integer, Integer> map1 = entry.getValue();
			/*
			 * for (Map.Entry<Integer,Integer> entry1 : map1.entrySet()) {
			 * System.out.println(entry1.getKey() + "/" + entry1.getValue());
			 * 
			 * 
			 * }
			 */

			String sql = "INSERT INTO star_count." + entry.getKey() + " VALUES ('Kindle_Store'," + "'" + map1.get(1)
					+ "'," + "'" + map1.get(2) + "'," + "'" + map1.get(3) + "'," + "'" + map1.get(4) + "'," + "'"
					+ map1.get(5) + "'," + "'" + map1.get(0) + "'" + ")";
			System.out.println(sql);
			stmt.execute(sql);

		}
	}

}

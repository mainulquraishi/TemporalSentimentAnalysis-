package star_extractor;

public class test {
	public static void main(String[] args) {
		String a = "as\"df.f";
		if (a.contains("\"")) {
			a = a.replaceAll("\"", "");
		}
		System.out.println(a);

	}
}

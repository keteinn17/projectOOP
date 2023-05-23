package cgfour.bounce.util;

// def imports
import java.io.*; 

public class UtilShiz {

	public static String loadFileAsString(String path) {
		String a = "";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null)
				a = a + line + "\n";
			
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountFrequency {

	public void countWords(String word) throws FileNotFoundException, IOException {
		try {
			int wordCount = 0;
			File file = new File("src\\TxtFiles\\dictonary.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String tempString = null;
			while ((tempString = bufferReader.readLine()) != null) {
				stringBuffer.append(tempString.toLowerCase());
			}
			Pattern patternObject = Pattern.compile(word.toLowerCase());
			Matcher matcherObject = patternObject.matcher(stringBuffer);
			while (matcherObject.find()) {
				wordCount++;
			}
			bufferReader.close();
			System.out.println("Word : " + word + "\nWord Frequency Count : " + wordCount);

		} catch (IOException error) {
			error.printStackTrace();
		}
	}
}


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PageRank {
	// Get words from each file using reallAll() method
	public static String[] wordsFromFile(File f) {
		In in = new In(f);
		String text = in.readAll();

		text = text.replaceAll("[^a-zA-Z0-9\\s]", "");
		String[] words = text.split(" ");
		return words;

	}

	public static String getURL(File f) {
		In in = new In(f);
		String url = in.readLine();
		return url;
	}

	// Frequency Builder for Each file and compare it with search queries.
	public static void FrqBuilder(ArrayList<String> as, String key) throws FileNotFoundException, IOException {
		
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		for (String fileName : as) {
			String[] wordlist = wordsFromFile(new File("src\\TxtFiles\\" + fileName));
			for (String word : wordlist) {
				if (word.toLowerCase().equals(key.split("\\W+")[0])) {
					if (wordMap.containsKey(fileName)) {
						wordMap.put(fileName, wordMap.get(fileName) + 1);
					} else {
						wordMap.put(fileName, 1);
					}
				}
			}
		}
		sortValuesInverse(wordMap);
	}

	private static void sortValuesInverse(HashMap<String, Integer> map) throws FileNotFoundException, IOException {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		Collections.reverse(list);
		System.out.println("Top 10 Results using Frequency analysis:");

		int originalCounter = 0;
		int i = 0;
		while (originalCounter < 10 && (list.size() > i)) {
			String name = list.get(i).toString();
			name = name.substring(0, name.lastIndexOf('.'));
			String Occu = list.get(i).toString();

			int pos = Occu.lastIndexOf("=");

			String txtURL = name + ".txt";
			String url = getURL(new File("src\\TxtFiles\\" + txtURL));
			if (url.contains("https://www.hotelscombined.ca/Hotel")) {
				System.out.println(originalCounter + " : URL: " + url);
				System.out.println("Website name: " + name); 
				File file = new File("src\\TxtFiles\\"+name+".txt");
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				    String line;
				    while ((line = br.readLine()) != null) {
				       if (line.contains("Cheapest rate per night")) {
				    	   String[] words = line.split("\\s+");
				    	   for (int j = 0; j < words.length; j++) {
				    		   if (words[j].contains("Cheapest") && words[j+1].contains("rate")) {
				    			  System.out.println("Cheapest Price :"+ words[j-1]);
				    		   }
				    	   }
				    	   break;
				       }
				    }
				}
				System.out.println("Total number of Occurences in given file is :" + Occu.substring(pos, Occu.length()));
				System.out.println();
				originalCounter++;
			}
			i++;
		}
		if (i < 5) {
			throw new IndexOutOfBoundsException();
		}
	}

}
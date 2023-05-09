import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector{

	Trie trie = new Trie();  //create object trie
	Map<String, Integer> dict = new HashMap<>(); //create dict 
	final static List<String> invalid = Arrays.asList("abcdefghijklmnopqrstuvwxyz"); //this list helps to add word that i wants to avoid
	
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {	
		try {
			if (new File(dictionaryFileName).exists()) { //if file exists 
				System.out.println("");
			}
			FileReader fr = new FileReader(dictionaryFileName); //to read the file
			BufferedReader br = new BufferedReader(fr);	       
			String line = null; // declare line is equal to null
			while ((line = br.readLine()) != null) {       //if line is not null than  convert into lowercase    			        					
				String word = line.toLowerCase();
				if (!line.contains(" ")) { // if contain space
					dict.put(word, dict.getOrDefault(word, 0)+1); // add word in dict, and get the value or default value
					trie.add(word);
				} else {
					String[] strs= line.split("\\s");  
					for(String str: strs) {
						dict.put(str, dict.getOrDefault(str, 0)+1);
						trie.add(str);
					}
				}
			}
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) {
		if (inputWord.length()==0 || inputWord==null || invalid.contains(inputWord.toLowerCase()) ) //invalid input
			return null;
		String s = inputWord.toLowerCase(); //convert into lowercase
		String res=null; //assign result is null
		TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();		
		INode node = trie.find(s);	// find word into tree		
		if(node == null) { // if not preset
			//System.out.println("\nnot find:" +s);
			for (String w: dict.keySet()) {
				int dist = editDistance(w, s);			//perform edit distance	
				TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
				int freq = dict.get(w);
				TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
				set.add(w);			
				similarWords.put(freq, set);
				map.put(dist, similarWords);		
			}		
			res= map.firstEntry().getValue().lastEntry().getValue().first();
			//System.out.println(res+ " "+dict.get(res));
		 } else if (node !=null) {
			 //System.out.println("\nfind:" +s+" "+dict.get(s));
			 res = s;
		 }
		 return res;
	}

	private int editDistance(String word1, String word2) {
		int n = word1.length();
		int m = word2.length();
	    int dp[][] = new int[n+1][m+1];
	    for (int i = 0; i <= n; i++) {
	        for (int j = 0; j <= m; j++) {
	            if (i == 0) //if first string empty
	                dp[i][j] = j;      
	            else if (j == 0) //if second string empty
	                dp[i][j] = i; 
	            else if (word1.charAt(i-1) == word2.charAt(j-1))//if both are similar
	                dp[i][j] = dp[i-1][j-1];	            
	            else if (i>1 && j>1  && word1.charAt(i-1) == word2.charAt(j-2) 
	            		&& word1.charAt(i-2) == word2.charAt(j-1))
	            	dp[i][j] = 1+Math.min(Math.min(dp[i-2][j-2], dp[i-1][j]), Math.min(dp[i][j-1], dp[i-1][j-1]));
	            else //both are different than perform insertion, deletion and replacement
	                dp[i][j] = 1 + Math.min(dp[i][j-1], Math.min(dp[i-1][j], dp[i-1][j-1])); 
	        }
	    } 
	    return dp[n][m];
	}
}

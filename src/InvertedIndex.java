import java.io.*;
import java.util.*;

public class InvertedIndex {

	Map<String, List<Integer>> index = new HashMap<>();
	List<String> documents = new ArrayList<String>();
	int docid = 0;

	void create(File file) throws IOException {
		String name = file.getName();
		documents.add(name);
		docid++;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String words[] = line.split("[^A-Za-z]"); 
				for (String _word : words) {
					String word = _word.toLowerCase();
					List<Integer> docs = index.get(word);
					if (docs == null) {
						docs = new ArrayList<>();
						index.put(word, docs);
					}
					docs.add(docid);
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}
	public static void main(String[] args) throws IOException {
		InvertedIndex i = new InvertedIndex();
    i.start();
    System.out.println("Enter output file:1 ");
}

	// writes index back to file
	void write(File file) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			// documents mapping
			for (int i = 0; i < documents.size(); i++) {
				int id = i + 1;
				writer.write(documents.get(i));
				writer.write(" ");
				writer.write(String.valueOf(id));
				writer.newLine();
			}
			// inverted index
			for (String word : index.keySet()) {
				List<Integer> docs = index.get(word);
				writer.write(word);
				writer.write(" ");
				int s = docs.size();
				for (int i = 0; i < s - 1; i++) {
					writer.write(String.valueOf(docs.get(i)));
					writer.write(" ");
				}
				writer.write(String.valueOf(docs.get(s - 1)));
				writer.newLine();
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}


       public void start() throws IOException {
            System.out.println("Enter output file:2 ");
             Scanner myObj = new Scanner(System.in);
            System.out.println("Enter output file:3 ");
            String output = myObj.nextLine();
                File files[] = new File("src\\TxtFiles\\").listFiles();
                System.out.println("Creating index.");  
              for (File file : files) {
                   if(file.getAbsolutePath().contains("dictonary.txt") || file.getAbsolutePath().contains("urls.txt"))
                       continue;
                    System.out.println("Files are processing :->" + file);
                    create(file);
                }
                System.out.println("Files are Writing.");
                write(new File(output));
                System.out.println("Done.");
        }

}

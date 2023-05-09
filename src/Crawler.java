
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private int max_depth = 10;

	String file_path = "src//TxtFiles//urls.txt";

	private HashSet<String> urlLink;

	public Crawler() {
		urlLink = new HashSet<String>();
	}

	public void urlToTextContent(String url) {
		if (url.contains("Windsor")) {
			System.out.println("Content Conversion for URL: " + url);
			try {
				String html = Jsoup.connect(url).get().html();
				Document doc = Jsoup.parse(html.toString());
				String textContent = doc.text();
				String title = doc.title();
				createFile(title, textContent, url);
			} catch (IOException e) {
			}
		}
	}

	private void createFile(String title, String textContent, String url) {
		// TODO Auto-generated method stub
		try {
			String[] titlesplit = title.split("\\|");
			String newTitle = "";
			for (String s : titlesplit) {
				newTitle = newTitle + " " + s;
			}
			File f = new File("src//TxtFiles//" + newTitle + ".txt");
			f.createNewFile();
			PrintWriter pw = new PrintWriter(f);
			pw.println(url);
			pw.println(textContent);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
	}

	public void getPageLinks(String URL, int depth) {
		if (URL.contains("#")) {
			String substring = URL.substring(URL.indexOf("#"));
			URL = URL.replace(substring, "");
		}

		if (!urlLink.contains(URL) && (depth < max_depth)) {
			try {
				if (urlLink.add(URL)) {
					try (FileWriter f = new FileWriter(file_path, true);
							BufferedWriter b = new BufferedWriter(f);
							PrintWriter p = new PrintWriter(b);) {
							p.println(URL);
					} catch (IOException i) {
						i.printStackTrace();
					}

					System.out.println(">>Depth:" + depth + "[" + URL + "]");
					urlToTextContent(URL);
				}
				Document document = Jsoup.connect(URL).get();
				Elements resultSet = document.select("a[href]");
				depth++;
				for (Element element : resultSet) {
					getPageLinks(element.attr("abs:href"), depth);
				}
			} catch (IOException e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public static void crawl() throws IOException {
		File myObj = new File("src//TxtFiles//windsorurl.txt");
		if (myObj.createNewFile()) {
			System.out.println("List of URL File Created !");
		} else {
			System.out.println("File already exists.");
		}
		Crawler crawler = new Crawler();
		System.out.println("Hello User!---- Do you want to crawl pages? : Please type yes or no");
		try (Scanner sc = new Scanner(System.in)) {
			String userInput = sc.next();
			while (userInput.equalsIgnoreCase("yes")) {
				System.out.println("Please enter URL");
				String url = sc.next();
				crawler.getPageLinks(url, 0);
				System.out.println("Do you want to continue crawl pages? : Please type yes or no");
				userInput = sc.next();
			}
		}
		
	}
}

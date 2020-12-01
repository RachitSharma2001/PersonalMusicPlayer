import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class JSoupTest {

	public static void main(String[] args){
		Document doc;
		try {
			doc = Jsoup.connect("https://www.youtube.com/results?search_query=positions+ariana+grande+lyrics").get();
			String html = doc.html();
			System.out.println(html.substring(html.indexOf("/watch?"), html.indexOf("\"", html.indexOf("/watch?"))));
			doc = Jsoup.parse(doc.html());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

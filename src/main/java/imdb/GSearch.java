package imdb;

import java.net.*;
import java.util.regex.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class GSearch {

	public static String findId(String getupdatedfileName, int year) {
		return searchID(getupdatedfileName + " " + String.valueOf(year));

	}

	public static String findId(String getupdatedfileName) {
		return searchID(getupdatedfileName);

	}

	public static String searchID(String searchQuery) {

		// String address = "https://www.google.co.in/search?q=gladiator+imdb";
		String baseAddr = "http://www.google.com/search?q=";
		String search = searchQuery + " imdb";
		String charset = "UTF-8";

		Document doc;

		try {

			Connection connection = Jsoup.connect(
					baseAddr + URLEncoder.encode(search, charset)).userAgent(
					"Mozilla");
			// if (connection.response().statusCode() == 200) {
			doc = connection.get();
			// System.out.println("Title : " + doc.title());
			// understand the css sytle search//
			Elements links = doc.select("li.g>h3>a");
			for (Element link : links) {
				// System.out.println("link : " + link.attr("href"));
				// System.out.println("------" + link.absUrl("href"));

				String url = link.attr("href");
				url = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
				// System.out.println("==== " + url);

				// imdb id pattern//
				Pattern p = Pattern.compile("tt\\d+");
				Matcher m = p.matcher(url);
				if (m.find()) {
					System.out.println("$$$$- " + m.group());
					return m.group();
				}
			}

		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
		}
		return "";

	}
}

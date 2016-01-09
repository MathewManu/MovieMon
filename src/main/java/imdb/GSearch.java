package imdb;

import java.net.*;
import java.util.regex.*;

import org.apache.log4j.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
/*
 * This GSearch class is basically used for searching a movie name online.
 * Google search query is formed like https://www.google.co.in/search?q=gladiator+imdb
 * From the search results imdb Id is parsed. 
 * This imdb id is used for getting data from omdb.[ eg: http://www.omdbapi.com/?i=tt0332452 ]
 * "tt0332452" is obtained from google search results.
 * Jsoup is a simple java HTML parser.
 * Refer http://stackoverflow.com/questions/3727662/how-can-you-search-google-programmatically-java-api
 * for more details.
 */
public class GSearch {
	
	final static Logger log = Logger.getLogger(GSearch.class);

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
			//TODO: understand the css sytle search//
			//Elements links = doc.select("li.g>h3>a");
			
			//TODO: finding imdb id using google search.. If some changes happen in the way
			//google displays the results.. Complete application breaks.. :( :( :(
			//changed from li.g>h3>a to h3>a Worked now !
			
			Elements links = doc.select("h3>a");
			log.debug("Inside GSearch Class.. links size" + links.size());
			if(links.size() == 0) {
				log.error("\n\t**************ERROR*************\n");
				log.error("\t\tcould not find links from google search results.. \n\n");
			}
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

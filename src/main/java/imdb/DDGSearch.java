package imdb;

import java.net.*;
import java.util.regex.*;

import org.apache.log4j.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
/*
 * This DDSearch [DuckDuckGo]class is basically used for searching a movie name online.
 * 
 * DuckDuck search query is formed like http://duckduckgo.com/html/?q=gladiator+imdb
 * From the search results imdb Id is parsed. 
 * This imdb id is used for getting data from omdb.[ eg: http://www.omdbapi.com/?i=tt0332452 ]
 * "tt0332452" is obtained from google search results.
 * Jsoup is a simple java HTML parser.
 * Refer http://stackoverflow.com/questions/3727662/how-can-you-search-google-programmatically-java-api
 * for more details.
 */
public class DDGSearch {
	
	final static Logger log = Logger.getLogger(DDGSearch.class);

	public static String findId(String getupdatedfileName, int year) {
		return searchID(getupdatedfileName + " " + String.valueOf(year));

	}

	public static String findId(String getupdatedfileName) {
		return searchID(getupdatedfileName);

	}

	public static String searchID(String searchQuery) {

		String baseAddr = "http://duckduckgo.com/html/?q=";
		String search = searchQuery + " imdb";
		String charset = "UTF-8";

		Document doc;

		try {

			Connection connection = Jsoup.connect(
					baseAddr + URLEncoder.encode(search, charset)).userAgent(
					"Mozilla");
			// if (connection.response().statusCode() == 200) {
			doc = connection.get();
			
			Elements links = doc.select("a[href]");
			
			if(links.size() == 0) {
				log.error("\n\t**************ERROR*************\n");
				log.error("\t\tcould not find links from duckduck search results.. \n\n");
			}
			for (Element link : links) {
				log.debug("DuckDuckGo Search !");
				String url = link.attr("href");
		
				Pattern p = Pattern.compile("(www.imdb.com/title/)(tt\\d+)");
				Matcher m = p.matcher(url);
				if (m.find()) {
					
					log.debug("Search Query is : " +search);
					log.debug("Matched URL from DDG is : " +url);
					
					return m.group(2); //imdb id
				}
			}

		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
		}
	
		return "";

	}
}

package imdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

		String search = searchQuery + " imdb";
		try {		
			Document doc = searchForMovie(search);
			if (doc == null) {
				Thread.sleep(500);
				//searching for the second time : If this fails, the movie will be scanned later in failed movies scan
				doc = searchForMovie(search);
			}			
			if (doc == null) {
				//No more retries plz. I am a failed movie!
				return null;
			}
			
			
			Elements links = doc.select("a[href]");

			if(links.size() == 0) {
				log.error("\n\t**************ERROR*************\n");
				log.error("\t\tcould not find links from duckduck search results.. \n\n");
			}
			log.debug("DuckDuckGo Search for String :" + search);
			for (Element link : links) {

				String url = link.attr("href");

				Pattern p = Pattern.compile("(www.imdb.com/title/)(tt\\d+)");
				Matcher m = p.matcher(url);
				if (m.find()) {

					log.debug("Matched URL from DDG is : " +url);
					return m.group(2); //imdb id
				}
			}

		} catch (Exception ex) {
			log.error("ERROR : " + ex.getMessage());
			
		}
		log.error("Could not find the movie : " +search);
		return "";

	}

	/*
	 * Synchronized method which is responsible for searching for the search string 
	 * in duckduckgo.
	 */
	private static synchronized Document searchForMovie(String search) {
		String baseAddr = "http://duckduckgo.com/html/?q=";
		String charset = "UTF-8";

		Connection connection;
		try {
			connection = Jsoup.connect(
					baseAddr + URLEncoder.encode(search, charset)).userAgent(
							"Mozilla");
			return connection.get();
		} catch (IOException e) {
			log.error("UnsupportedEncodingException for String : " +search);
		}
		return null;
	}


}

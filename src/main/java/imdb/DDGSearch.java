package imdb;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

	final Lock urlConnectionLock = new ReentrantLock();

	public String findId(String getupdatedfileName, int year) {
		return searchID(getupdatedfileName + " " + String.valueOf(year));
	}

	public String findId(String getupdatedfileName) {
		return searchID(getupdatedfileName);
	}

	public String searchID(String searchQuery) {

		String search = searchQuery + " site:imdb.com";
		try {
			Document doc = searchForMovie(search);
			if (doc == null) {
				log.info("DDG Search : Going to retry for movie : " + searchQuery); 
				Thread.sleep(2000);
				//searching for the second time : If this fails, the movie will be scanned later in failed movies scan
				doc = searchForMovie(search);
			}			
			if (doc == null) {
				log.error("The movie details could not be fetched at this time. Please retry later. Movie name : " + searchQuery);
				return null;
			}


			Elements links = doc.select("a[href]");

			if(links == null || links.size() == 0) {
				log.error("\n\t**************ERROR*************\n");
				log.error("\t\tcould not find links from duckduck search results.. \n\n");
				return null;
			}
			log.debug("DuckDuckGo Search for String :" + search);
			for (Element link : links) {

				String url = link.attr("href");
			//	log.info("urls : " + url);
				Pattern p = Pattern.compile("(www.imdb.com/title/)(tt\\d+)");
				Matcher m = p.matcher(url);
				if (m.find()) {

					log.debug("Matched URL from DDG is : " +url);
					return m.group(2); //imdb id
				}
			}

		} catch (Exception ex) {
			log.error("ERROR : " + ex.getMessage() + "movie name : " + searchQuery);

		}
		log.error("Could not find the movie : " +search);
		return "";

	}

	/*
	 * Synchronized method which is responsible for searching for the search string 
	 * in duckduckgo.
	 */
	private Document searchForMovie(String search) {
		try {
			if (urlConnectionLock.tryLock(10, TimeUnit.SECONDS)) {
				String baseAddr = "http://duckduckgo.com/html/?kd=-1&q=";
				String charset = "UTF-8";
				String encodedURL = null;
				Connection connection;
				try {
					encodedURL = URLEncoder.encode(search, charset) ;
					connection = Jsoup.connect(
							baseAddr + encodedURL).userAgent("Mozilla/5.0");
					Document doc =  connection.get();
					return doc;
				} catch (Exception e) {
					log.error("Exception while searching for movie : " + encodedURL + e.getMessage() + e.getCause().getLocalizedMessage());

				}
			}
		} catch (InterruptedException e) {
			log.error("Exception while acquiring the lock!! ");
		} finally {
			urlConnectionLock.unlock();
		}
		return null;
	}


}

package Tmdb;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.concurrent.atomic.*;

import org.apache.log4j.*;
import org.apache.log4j.pattern.*;

import com.google.gson.*;

public class TmdbConnector {
	
	final static Logger log = Logger.getLogger(TmdbConnector.class);

	private static AtomicInteger limit = new AtomicInteger();
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String TMDB_PATH = "http://api.themoviedb.org/3/search/movie?api_key=";
	
	public static HttpURLConnection getConn(String iURL) throws IOException {
		URL url = new URL(iURL);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		
		return conn;
	}
	
	public static SearchResponse SearchMovie(String apiKey, String search) {
		
		String searchMovie = search.trim().replace(" ", "+");
		String iURL = String.format("%s%s%s%s", TMDB_PATH, apiKey, "&query=", searchMovie);
		try {
			
		
		HttpURLConnection conn = getConn(iURL);
		
		int responseCode = conn.getResponseCode();
		System.out.println("Sending get request to url : " + iURL);
		log.info("Sending get request to url : " + iURL);
		if (200 == responseCode) {
			conn.connect();
			Gson gson = new Gson();
			//System.out.println("Creating Results objects from json data received");
			SearchResponse response = new SearchResponse();
			response = gson.fromJson(new InputStreamReader((InputStream) conn.getContent()), SearchResponse.class);
		
			int value = conn.getHeaderFieldInt("X-RateLimit-Remaining",0);
			//limit.set(value);
			
			if(value <10) {
				log.info("Thread sleeping .. ");
				Thread.sleep(2000);
			}
			
			/*long resetTime = conn.getHeaderFieldLong("X-RateLimit-Reset", 0);
			long currentTime = Instant.now().getEpochSecond();
			
			System.out.println("Times : " +resetTime +" " + currentTime +" difference : " + (resetTime-currentTime));
			 */
			//if(S)
			log.info(" ********** X-Ratelimit-Remaining : " + value );
			//System.out.println("created response ########## : " + value );
			return response;
		}
		}
		catch (Exception e) {
			System.out.println("ERROR >>>>>>>>>  " + e.getMessage());
		}
		return null;
	}

}


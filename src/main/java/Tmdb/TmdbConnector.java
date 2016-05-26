package Tmdb;

import java.io.*;
import java.net.*;
import org.apache.log4j.*;
import com.google.gson.*;

public class TmdbConnector {
	
	final static Logger log = Logger.getLogger(TmdbConnector.class);
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String TMDB_PATH = "http://api.themoviedb.org/3/search/movie?api_key=";
	
	private HttpURLConnection getConn(String iURL) throws IOException {
		URL url = new URL(iURL);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		
		return conn;
	}
	
	public SearchResponse SearchMovie(String apiKey, String search) {
		
		String searchMovie = search.trim().replace(" ", "+");
		String iURL = String.format("%s%s%s%s", TMDB_PATH, apiKey, "&query=", searchMovie);
		try {
			
		
		HttpURLConnection conn = getConn(iURL);	
		int responseCode = conn.getResponseCode();
		log.info("Sending get request to url : " + iURL);
	
		if (200 == responseCode) {
			conn.connect();
			Gson gson = new Gson();
			
			SearchResponse response = new SearchResponse();
			response = gson.fromJson(new InputStreamReader((InputStream) conn.getContent()), SearchResponse.class);
		
			/*
			 * tmdb supports 40 requests in 10 seconds
			 * X-RateLimit-Remaining returns the remaining number of requests
			 * that can be sent in the current time frame
			 */
			int value = conn.getHeaderFieldInt("X-RateLimit-Remaining",0);
			
			if(value <10) {
				log.info("Thread sleeping .. ");
				Thread.sleep(2000);
			}
			
			log.info(" ********** X-Ratelimit-Remaining : " + value );
			
			return response;
		}
		}
		catch (Exception e) {
			System.out.println("ERROR >>>>>>>>>  " + e.getMessage());
		}
		return null;
	}

}


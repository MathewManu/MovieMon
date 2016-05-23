package imdb;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.*;

import com.google.gson.*;

public class OmdbApiConnector implements BaseApiConnector {
	
	final static Logger log = Logger.getLogger(OmdbApiConnector.class);

	//TODO: need to find out what should be the value for user_agent, as of now keeping as "Mozilla/5.0"
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String OMDB_PATH = "http://www.omdbapi.com/?";
	
	public synchronized void updateMovieObjectsWithApiData(MovieObject movieObject) {

		try {
				String iURL;
				if(movieObject.getImdbId() != null) {
					iURL = String.format("%s%s%s", OMDB_PATH, "i=", movieObject.getImdbId());
				}
				else {
					iURL = String.format("%s%s%s%s", OMDB_PATH, "t=", movieObject.getTitleFromTmdb().replace(" ", "+"),"&type=movie");
				}

				URL url = new URL(iURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.setRequestProperty("User-Agent", USER_AGENT);

				int responseCode = conn.getResponseCode();

				log.debug("\nSending GET request to url : " + url);
			
				//TODO: manu incase rCode != 200 handle 
				if (200 == responseCode) {
					conn.connect();
					Gson gson = new Gson();
					// error handling
					// no data or unknown data ..
					log.debug("\nCreating movie object from json data received");
					Movie movie = new Movie();
					movie = gson.fromJson(new InputStreamReader((InputStream) conn.getContent()), Movie.class);
					movieObject.setMovieObjFromApi(movie);
				}

		} catch (IOException e) {
			log.error(" omdbApiConnector error..  " + e.getMessage());
		}

	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

}

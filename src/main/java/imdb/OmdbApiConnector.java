package imdb;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.*;

public class OmdbApiConnector implements BaseApiConnector {

	//need to find out what should be the value for user_agent, as of now keeping as "Mozilla/5.0"
	private static final String USER_AGENT = "Mozilla/5.0";
	//this should come from properties file ????
	private static final String OMDB_PATH = "http://www.omdbapi.com/?";
	
	//later need to change to map<name, movieDummyObjwithCorrectNameAndabsPath>
	//and return type also probably
	// should get processed names here right ??
	
	public void updateMovieObjectsWithApiData(List<MovieObject> allMovieObjects) {

		try {

			for (MovieObject movie : allMovieObjects) {

				String iURL = String.format("%s%s%s", OMDB_PATH, "t=",
						movie.getCorrectMovieName());

				URL url = new URL(iURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.setRequestProperty("User-Agent", USER_AGENT);

				int responseCode = conn.getResponseCode();

				System.out.println("\nSending GET request to url : " + url);
				System.out.println("Response code : " + responseCode);

				if (200 == responseCode) {
					conn.connect();
					Gson gson = new Gson();
					// error handling
					// no data or unknown data ..
					Movie movieObj = new Movie();
					movieObj = gson.fromJson(new InputStreamReader(
							(InputStream) conn.getContent()), Movie.class);
					movie.setMovieObjFromApi(movieObj);

				}

			}

		} catch (IOException e) {
			System.out.println(" omdbApiConnector error..  " + e.getMessage());
		}
		// null or the data that is present in movieDetails ?

	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

}

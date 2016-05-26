package Tmdb;

import org.apache.log4j.*;


public class MovieDetailsTmdb {

	final static Logger log = Logger.getLogger(MovieDetailsTmdb.class);
	
	private TmdbConnector tmdbConn = new TmdbConnector();
	
	private String apiKey;
	
	public MovieDetailsTmdb(String apiKey) {
		setApiKey(apiKey);
	}

	private MovieDetailsTmdb() {
	}

	public String getIMDBid(String movieName) {

		if (apiKey == null || apiKey.isEmpty()) {
			return null;
		}

		String title = getTitleFromTmdb(movieName);

		if (title != null) {
			log.info("Title is .. " + title);
			return title;
		} else {
			return "";
		}

	}

	public String getTitleFromTmdb(String movieName) {

		log.info(" Querying tmdb for movie : " + movieName);

		String mName = movieName;
		
		while (!mName.isEmpty()) {
			
			SearchResponse response = tmdbConn.SearchMovie(getApiKey(), mName);
			
			if(response == null ) {
				log.error("Response null from tmdb for movie : " + mName);
				return null;
			}
			if (response.getTotal_results() == 0) {
				
				int lastIndexOfSpace = mName.lastIndexOf(" ");
				if (lastIndexOfSpace != -1) {
					mName = mName.substring(0, lastIndexOfSpace);
					log.info("Removed 1 word from the end : " + mName + " Length : " + mName.length());
				} else {
					log.error("Could not find the movie from tmdb .. Returing .. : " + movieName);
					return null;
				}

			} else {
				log.info("results are not null from tmdb,, Trying to find the best match from results. total results : " + response.getTotal_results());
				String title = findBestMatchTitle(response, movieName);
				log.info("Found title for movie " + movieName + " Title >>> " + title);
				return title;
			}
		}
		return null;
	}

	private String findBestMatchTitle(SearchResponse response, String movieName) {

		double match = 0.0;
		double ldMatch;
		double poplr = 0.0;
		String title = "";

		if (response.getTotal_results() == 1) {
			log.info("Found only 1 result title : " + response.getResults().get(0).getTitle());
			return response.getResults().get(0).getTitle();
		} else {
			for (SearchResult rs : response.getResults()) {
				
				ldMatch = LDistance.similarity(rs.getTitle(), movieName);
				//poplr = rs.getPopularity();
				log.info("LDisance >>>>>> Title : " + rs.getTitle() + " || name : " + movieName + " || Distance : " + ldMatch +" || pop : " + rs.getPopularity());
				if(ldMatch < .2) {
					log.info("LDistance is : " +ldMatch +" Not a close match..skipping");
					continue;
				}
				
				if (ldMatch >= match && rs.getPopularity() > poplr) {
					title = rs.getTitle();
					match = ldMatch;
					poplr = rs.getPopularity();
				}
				// popularity match if we get same similarity ?
			}
		}
		log.info("LDistace==== returing .. : " + title);
		return title;

	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}

package imdb.executer;

import java.io.*;
import java.util.concurrent.*;

import org.apache.log4j.*;

import imdb.*;
import imdb.database.dao.*;
import imdb.database.model.*;
import imdb.utils.*;

public class MovieProcessor implements Callable<Boolean> {
	
	final static Logger log = Logger.getLogger(MovieProcessor.class);
	
	private static int GUEST_USERID = 1; 
	
	private static BaseApiConnector apiConnector = new OmdbApiConnector();
	
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();

	private MovieObject movieObj;
	
	
	public MovieProcessor(MovieObject movieObj) {
		this.movieObj = movieObj;
	}
	
	@Override
	public Boolean call() {
		
		if(true == getMovieDetailsFromOmdb())
		{
			if (movieDAO.insert(getMovieDto(movieObj))) {
				log.debug("Insert success : " + movieObj.getMovieObjFromApi().getTitle() + " - Path : "
						+ movieObj.getMovieAbsPath());
				//get last inserted id ..select id from movie where imdbid = movieobj.get AND filelocation = movieobj.getfilelocation()
				// insert into user_movies table
				// insert into recenlty_added table
		
				int lastInsertId = movieDAO.getLastInsertMovieID(movieObj.getImdbId(), movieObj.getMovieAbsPath());
				if(lastInsertId != 0) {
					
					//TODO: hard coding GUEST userId 1 now...
					movieDAO.insertUserMovies(GUEST_USERID, lastInsertId);
					//TODO: recently_added_movie table insert..
				}
				
				downloadPoster();
				return true;
			
			} else {
				log.error("ERROR: insert error");
				return false;
			}
		}
		return false;
		
	}

	/*
	 * Download movie poster/Thumbnail
	 * Some movies has "NA" as poster address 
	 */
	private void downloadPoster() {
		
		String posterLoc = movieObj.getMovieObjFromApi().getPoster();
		String title = movieObj.getMovieObjFromApi().getTitle();
		
		if (posterLoc != null && posterLoc != "NA") {
			try {
				MovieMonUtils.downloadPoster(title,posterLoc);
			} catch (IOException e) {
				log.error("Exeption downloadPoster : " +e.getMessage());
			}

		}
		
	}
	/*
	 * First find the imdb id for the movie.
	 * Then query omdb to get details about the movie
	 */
	private boolean getMovieDetailsFromOmdb() {

		String imdbId = "";

		log.debug("searching Online for IMDB ID ...: \" " + movieObj.getUpdatedfileName() + "\"   Year : "
				+ movieObj.getYear());

		if (movieObj.getYear() != 9999) {
			imdbId = DDGSearch.findId(movieObj.getUpdatedfileName(), movieObj.getYear());
		} else {
			imdbId = DDGSearch.findId(movieObj.getUpdatedfileName());
		}

		if (!imdbId.isEmpty()) {
			log.info("Imdb id found for the movie : " + movieObj.getMovieName() + " is : " + imdbId);
			movieObj.setImdbId(imdbId);

			// Calling omdb to get all the movie info
			apiConnector.updateMovieObjectsWithApiData(movieObj);
			return true;
		}
		log.error("Coulnd not find IMDB Id for movie : " + movieObj.getMovieName() + " : Search Name : "
				+ movieObj.getUpdatedfileName() + " Year : " + movieObj.getYear());
		
		/*
		 * update failed movie table
		 */
		movieDAO.insertFailedMovie(movieObj.getMovieAbsPath());
		
		return false;

	}
	
	/*
	 * return movieDto which should go to DB
	 */
	//TODO : (manu) change with builder .with
	private MovieDBResult getMovieDto(MovieObject movieObj) {
		MovieDBResult movieDto = new MovieDBResult();
		
		movieDto.setFileName(movieObj.getMovieName());
		movieDto.setMovieAbsPath(movieObj.getMovieAbsPath());
		
		movieDto.setTitle(movieObj.getMovieObjFromApi().getTitle());
		movieDto.setImdbID(movieObj.getMovieObjFromApi().getImdbID());
		movieDto.setYear(movieObj.getMovieObjFromApi().getYear());
		movieDto.setGenre(movieObj.getMovieObjFromApi().getGenre());
		movieDto.setImdbRating(movieObj.getMovieObjFromApi().getImdbRating());
		
		movieDto.setPlot(movieObj.getMovieObjFromApi().getPlot());
		movieDto.setRunTime(movieObj.getMovieObjFromApi().getRunTime());
		movieDto.setLanguage(movieObj.getMovieObjFromApi().getLanguage());
		
		String posterUrl = movieObj.getMovieObjFromApi().getPoster();
		if (posterUrl != null && posterUrl != "NA") {
			movieDto.setPoster(posterUrl.substring(posterUrl.lastIndexOf("/")+1));
		}
		else {
			movieDto.setPoster("no_thumbnail.jpg");
		}
		
		
		movieDto.setDirector(movieObj.getMovieObjFromApi().getDirector());
		movieDto.setActors(movieObj.getMovieObjFromApi().getActors());
		
		return movieDto;
		
	}


}

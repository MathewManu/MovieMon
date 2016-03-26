package imdb.rest.favorites;

import java.sql.*;
import java.util.*;

import imdb.constants.MovieDaoImplSelector;
import imdb.database.dao.MovieDAOImpl;
import imdb.database.dao.MovieDAOImplForUserSpecificFeatures;
import imdb.database.dao.MovieMonDaoFactory;
import imdb.database.model.*;
import imdb.rest.*;

public class FavoriteManager {

	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl(MovieDaoImplSelector.USER_SPECIFIC_FEATURES_MOVIE_SELECTOR);
	
	public boolean addFavorite(int movieId, String userName) {

		return ((MovieDAOImplForUserSpecificFeatures) movieDAO).insertIntoFavorites(movieId, movieDAO.getUserIdForName(userName));

	}

	public boolean deleteFavorite(int movieId, String userName) {

		return ((MovieDAOImplForUserSpecificFeatures) movieDAO).deleteFromFavorites(movieId, movieDAO.getUserIdForName(userName));

	}

	public List<MovieDBResult> getAllFavorites(String userName) {
		
		int userId = movieDAO.getUserIdForName(userName);
		ResultSet rs = ((MovieDAOImplForUserSpecificFeatures) movieDAO).getAllFavs(userId);
		return RestRequestProcessor.processResultSet(rs);
		
	}
}

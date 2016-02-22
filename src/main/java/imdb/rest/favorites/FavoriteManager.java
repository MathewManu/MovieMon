package imdb.rest.favorites;

import imdb.constants.MovieDaoImplSelector;
import imdb.database.dao.MovieDAOImpl;
import imdb.database.dao.MovieDAOImplForUserSpecificFeatures;
import imdb.database.dao.MovieMonDaoFactory;

public class FavoriteManager {

	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl(MovieDaoImplSelector.USER_SPECIFIC_FEATURES_MOVIE_SELECTOR);

	public boolean addFavorite(int movieId, String userName) {

		return ((MovieDAOImplForUserSpecificFeatures) movieDAO).insertIntoFavorites(movieId, movieDAO.getUserIdForName(userName));

	}

	public boolean deleteFavorite(int movieId, String userName) {

		return ((MovieDAOImplForUserSpecificFeatures) movieDAO).deleteFromFavorites(movieId, movieDAO.getUserIdForName(userName));

	}
}

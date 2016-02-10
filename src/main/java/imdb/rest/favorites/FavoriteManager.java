package imdb.rest.favorites;

import imdb.database.dao.MovieDAOImpl;
import imdb.database.dao.MovieMonDaoFactory;

public class FavoriteManager {

	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
	
	public boolean addFavorite(int movieId, String userName) {
		
		return movieDAO.insertIntoFavorites(movieId, movieDAO.getUserIdForName(userName));
				
	}
}

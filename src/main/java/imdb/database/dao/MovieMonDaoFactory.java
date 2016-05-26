package imdb.database.dao;

import imdb.constants.MovieDaoImplSelector;

/*
 * @author mon
 * 
 * http://www.oracle.com/technetwork/java/dataaccessobject-138824.html
 * https://dzone.com/articles/database-interaction-dao-and
 * http://tutorials.jenkov.com/java-persistence/dao-design-problems.html 
 */
public class MovieMonDaoFactory {
	//singleton ?
	public static MovieDAOImpl getMovieDAOImpl(String... args) {

		if (args.length == 0) {
			return new MovieDAOImpl();
		}
		if(args[0].equals(MovieDaoImplSelector.USER_SPECIFIC_FEATURES_MOVIE_SELECTOR)) {
			return new MovieDAOImplForUserSpecificFeatures();
		} else if(args[0].equals(MovieDaoImplSelector.WATCHLIST)) {
			return new MovieDAOImplForWatchList();
		} else if(args[0].equals(MovieDaoImplSelector.USERAUTH)) {
			return new MovieDAOImplAuth();
		}
		
		
		else {
			return (new MovieDAOImpl());
		}
	}
}

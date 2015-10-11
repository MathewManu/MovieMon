package imdb.database.dao;

/*
 * @author mon
 * 
 * http://www.oracle.com/technetwork/java/dataaccessobject-138824.html
 * https://dzone.com/articles/database-interaction-dao-and
 * http://tutorials.jenkov.com/java-persistence/dao-design-problems.html 
 */
public class MovieMonDaoFactory {
	//singleton ?
	public static MovieDAOImpl getMovieDAOImpl() {
		return (new MovieDAOImpl());
	}
}

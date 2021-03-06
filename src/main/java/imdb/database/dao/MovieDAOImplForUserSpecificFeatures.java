package imdb.database.dao;

import imdb.exceptions.NoRowFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.log4j.*;

/**
 * This class was introduced specifically for DB related activities for setting/getting
 *  specific characteristics of a movie object like favorites, watchlist, 
 *  watched movies etc per user. Intended for better readability.
 *  (TODO : Anushya )If any of the methods which really belongs here has been included in MovieDAOImpl class,
 *   lets move them here !
 * @author anushya
 *
 */
public class MovieDAOImplForUserSpecificFeatures extends MovieDAOImpl {
	
	final static Logger log = Logger.getLogger(MovieDAOImplForUserSpecificFeatures.class);

	private static String IS_MOVIE_FAVORITED = "SELECT ID FROM FAVORITES WHERE MOVIE_ID = ? AND USER_ID = ?";
	private static String INSERT_INTO_FAVORITES = "INSERT INTO FAVORITES (MOVIE_ID, USER_ID) VALUES (?, ?);";
	private static String DELETE_FROM_FAVORITES = "DELETE FROM FAVORITES WHERE MOVIE_ID = ? AND USER_ID = ?";
	
	private static String SELECT_ALL_FAVS = "SELECT MOVIE.*, FAVORITES.MOVIE_ID AS ISFAVORITE, WATCHLIST.MOVIE_ID AS ISWATCHLIST"
			+ " FROM MOVIE"
			+ " INNER JOIN FAVORITES ON FAVORITES.MOVIE_ID = MOVIE.ID AND FAVORITES.USER_ID = ?"
			+ " LEFT JOIN WATCHLIST ON MOVIE.ID = WATCHLIST.MOVIE_ID";
	
	/*
	 * Checks if the movie has been favorited by the user. R
	 */
	//TODO: not using this function anymore !
	public int isMovieFavoritedByUser(String movieId, String userId) throws NoRowFoundException {
		int favId = -1;
		PreparedStatement pst = prepareStatementFromArgs(IS_MOVIE_FAVORITED, Arrays.asList(movieId, userId));
		ResultSet rs = getResultSetForPst(pst);
		try {
			if(rs.next()) {
				favId = rs.getInt("ID");
				return favId;
			}
		} catch (SQLException e) {
			log.error("Execption : " + e.getMessage());
		}
		throw new NoRowFoundException("No movie with id " + movieId + "favorited by user :" +userId);
	}

	public boolean insertIntoFavorites(int movieId, int userId) {
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_FAVORITES, Arrays.asList(movieId, userId));
		log.info("inserting into favorites table.. userid : " + userId + " movieID : " + movieId);
		return performQuery(pst);
	}

	public boolean deleteFromFavorites(int movieId, int userId) {
		PreparedStatement pst = prepareStatementFromArgs(DELETE_FROM_FAVORITES, Arrays.asList(movieId, userId));
		log.info("Deleting from favorites table.. userid : " + userId + " movieID : " + movieId);
		return performQuery(pst);
	}
	
	public ResultSet getAllFavs(int userId) {
		PreparedStatement pst = prepareStatementFromArgs(SELECT_ALL_FAVS, Arrays.asList(userId));
		log.info("Getting all favorite movies for the user: " +userId);
		return getResultSetForPst(pst);
		
	}

}

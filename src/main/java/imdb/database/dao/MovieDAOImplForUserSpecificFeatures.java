package imdb.database.dao;

import imdb.exceptions.NoRowFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

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

	private static String IS_MOVIE_FAVORITED = "SELECT ID FROM FAVORITES WHERE MOVIE_ID = ? AND USER_ID = ?";

	/*
	 * Checks if the movie has been favorited by the user. R
	 */
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


}

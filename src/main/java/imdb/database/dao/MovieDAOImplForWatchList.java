package imdb.database.dao;

import java.sql.*;
import java.util.*;

import org.apache.log4j.*;

public class MovieDAOImplForWatchList extends MovieDAOImpl {
	
	final static Logger log = Logger.getLogger(MovieDAOImplForWatchList.class);
	
	private static String SELECT_WATCHLIST = "SELECT MOVIE.* FROM MOVIE INNER JOIN WATCHLIST ON WATCHLIST.MOVIE_ID = MOVIE.ID AND WATCHLIST.USER_ID = ?;";
	private static String INSERT_INTO_WATCHLIST = "INSERT INTO WATCHLIST (USER_ID, MOVIE_ID) VALUES (?, ?);";
	private static String DELETE_FROM_WATCHLIST = "DELETE FROM WATCHLIST WHERE USER_ID = ? AND MOVIE_ID = ?";

	/*
	 * iswatchlist function needs to be added.
	 */
	
	public boolean insertIntoWatchList(int userId, int movieId) {
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_WATCHLIST, Arrays.asList(userId, movieId));
		log.info("inserting into watchlist table.. userid : " + userId + " movieID : " + movieId);
		return performQuery(pst);
	}

	public boolean deleteFromWatchList(int userId, int movieId) {
		PreparedStatement pst = prepareStatementFromArgs(DELETE_FROM_WATCHLIST, Arrays.asList(userId, movieId));
		log.info("Deleting from watchlist table.. userid : " + userId + " movieID : " + movieId);
		return performQuery(pst);
	}
	
	public ResultSet getWatchList(int userId) {
		PreparedStatement pst = prepareStatementFromArgs(SELECT_WATCHLIST, Arrays.asList(userId));
		log.info("Getting all watchlist for the user: " +userId);
		return getResultSetForPst(pst);
		
	}
	
}

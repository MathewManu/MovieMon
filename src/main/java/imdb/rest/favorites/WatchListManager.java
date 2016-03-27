package imdb.rest.favorites;

import java.sql.*;
import java.util.*;

import imdb.constants.*;
import imdb.database.dao.*;
import imdb.database.model.*;
import imdb.rest.*;

public class WatchListManager {
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl(MovieDaoImplSelector.WATCHLIST);

	public boolean addToWatchList(int movieId, String userName) {

		return ((MovieDAOImplForWatchList) movieDAO).insertIntoWatchList(movieDAO.getUserIdForName(userName), movieId);

	}

	public boolean deleteFromWatchList(int movieId, String userName) {

		return ((MovieDAOImplForWatchList) movieDAO).deleteFromWatchList(movieDAO.getUserIdForName(userName), movieId);

	}

	public List<MovieDBResult> getWatchList(String userName) {

		int userId = movieDAO.getUserIdForName(userName);
		ResultSet rs = ((MovieDAOImplForWatchList) movieDAO).getWatchList(userId);
		return RestRequestProcessor.processResultSet(rs);

	}

}

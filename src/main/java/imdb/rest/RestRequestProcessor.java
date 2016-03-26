package imdb.rest;

import java.sql.*;
import java.util.*;

import imdb.auth.AuthenticationUtils;
import imdb.constants.MovieDaoImplSelector;
import imdb.database.dao.*;
import imdb.database.model.*;
import imdb.exceptions.NoRowFoundException;

public class RestRequestProcessor {

	//I need this specific dao here , as I am querying for user favorites.
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl(MovieDaoImplSelector.USER_SPECIFIC_FEATURES_MOVIE_SELECTOR);

	private static String SELECT_ALL = "SELECT * FROM MOVIE";

	/*
	 * this fun processes the query params & form sql query. call impl &
	 * processes the resultSet. can process rating, year query params now.
	 * rating eg: 5.6-8.8, 5.6-, 5.6 
	 * year eg : 2000-2010, 2000-, 2000, -2000
	 */
	public static List<MovieDBResult> getMovies(String rating, String year) {

		ArrayList<String> queryParamList = new ArrayList<String>();
		StringBuilder querySb = new StringBuilder(SELECT_ALL);

		if (!rating.isEmpty()) {
			queryParamList.add(getQueryConditionWithRating(rating));
		}
		if (!year.isEmpty()) {
			queryParamList.add(getQueryConditionWithYear(year));
		}

		String query = updateSelectStmntWithConditions(querySb, queryParamList).toString();

		return processResultSet(movieDAO.getResultSetForQuery(query));

	}

	public static List<MovieDBResult> processResultSet(ResultSet rs) {

		List<MovieDBResult> movieList = new ArrayList<MovieDBResult>();
		try {
			while (rs.next()) {
				movieList.add(processRow(rs));
			}
			rs.close();
		} catch (Exception e) {

			System.out.println("Exception while processing resultSet : " + e.getMessage());
		}
		movieDAO.closeConnection();
		return movieList;
	}

	private static StringBuilder updateSelectStmntWithConditions(StringBuilder querySb,
			ArrayList<String> queryParamList) {

		for (int i = 0; i < queryParamList.size(); i++) {
			if (i == 0) {
				querySb.append(" WHERE");
			} else {
				querySb.append(" AND");
			}
			querySb.append(queryParamList.get(i));
		}
		return querySb;
	}

	/*
	 * This function searches db for a particular movie.
	 * eg: http://localhost:8080/MovieMon/Movies/gladiator
	 */
	// TODO: remove dup entries, should return only one resource
	// TODO: title is case sensitive now. Need to fix
	public static List<MovieDBResult> searchMovie(String searchQuery) {

		String query = String.format("%s%s%s", "SELECT * FROM MOVIE WHERE TITLE = '", searchQuery, "';");

		return processResultSet(movieDAO.getResultSetForQuery(query));

	}

	private static String getQueryConditionWithYear(String year) {
		String query = "";
		int index = year.indexOf("-");

		if (index == -1) {
			query = String.format("%s%s%s", " YEAR = '", year, "'");
		} else {
			String minYear = year.substring(0, index);
			String maxYear = year.substring(index + 1);

			if (maxYear.isEmpty()) {
				query = String.format("%s%s%s", " YEAR >= '", minYear, "'");
			} else {
				query = String.format("%s%s%s%s%s", " YEAR BETWEEN '", minYear, "' AND '", maxYear, "'");
			}
		}

		return query;
	}

	private static String getQueryConditionWithRating(String rating) {
		String query = "";

		int index = rating.indexOf("-");

		if (index == -1) {
			query = String.format("%s%s%s", " IMDBRATING <= '", rating, "'");
		} else {
			String minRating = rating.substring(0, index);
			String maxRating = rating.substring(index + 1);

			if (maxRating.isEmpty()) {
				query = String.format("%s%s%s", " IMDBRATING >= '", minRating, "'");
			} else {
				query = String.format("%s%s%s%s%s", " IMDBRATING BETWEEN '", minRating, "' AND '", maxRating, "'");
			}
		}

		return query;
	}

	/*
	 * processes a single row of ResultSet and generate MovieDBResult object.
	 */
	private static MovieDBResult processRow(ResultSet rs) throws SQLException {
		MovieDBResult movie = new MovieDBResult();

		movie.setId(rs.getInt("ID"));
		movie.setFileName(rs.getString("FILENAME"));
		movie.setMovieAbsPath(rs.getString("FILELOCATION"));
		movie.setTitle(rs.getString("TITLE"));
		movie.setImdbID(rs.getString("IMDBID"));
		movie.setYear(rs.getInt("YEAR"));
		movie.setGenre(rs.getString("GENRE"));
		movie.setImdbRating(rs.getString("IMDBRATING"));
		movie.setPoster(rs.getString("POSTER"));
		movie.setPlot(rs.getString("PLOT"));
		movie.setDirector(rs.getString("DIRECTOR"));
		movie.setActors(rs.getString("ACTORS"));
		movie.setRunTime(rs.getString("RUNTIME"));
		movie.setLanguage(rs.getString("LANGUAGE"));
		String loggedInUserName;
		if ((loggedInUserName = AuthenticationUtils.getCurrentlyLoggedinUser()) != null) {
			try {
				int favId = ((MovieDAOImplForUserSpecificFeatures) movieDAO).isMovieFavoritedByUser(String.valueOf(movie.getId()), String.valueOf(movieDAO.getUserIdForName(loggedInUserName)));
				if (favId != -1) {
					movie.setFavorite(true);
				}
			} catch (NoRowFoundException e) {
				movie.setFavorite(false);
			}
		}

		return movie;
	}

}

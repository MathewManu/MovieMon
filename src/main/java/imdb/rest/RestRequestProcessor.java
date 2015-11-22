package imdb.rest;

import java.sql.*;
import java.util.*;

import imdb.database.dao.*;
import imdb.database.model.*;

public class RestRequestProcessor {

	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
	
	/*
	 * this fun processes the query params & form sql query. call impl &
	 * processes the resultSet. can process rating, year query params now.
	 * rating eg: 5.6-8.8, 5.6-, 5.6 
	 * year eg : 2000-2010, 2000-, 2000, -2000
	 */
	public static List<MovieDBResult> getMovies(String rating, String year) {

		List<MovieDBResult> movieList = new ArrayList<MovieDBResult>();
		ArrayList<String> queryParamList = new ArrayList<String>();

		String query_default = "SELECT * FROM MOVIE";
		StringBuilder querySb = new StringBuilder();
		querySb.append(query_default);

		if (!rating.isEmpty()) {
			queryParamList.add(getQueryConditionWithRating(rating));
		}
		if (!year.isEmpty()) {
			queryParamList.add(getQueryConditionWithYear(year));
		}
		for (int i = 0; i < queryParamList.size(); i++) {
			if (i == 0) {
				querySb.append(" WHERE");
			} else {
				querySb.append(" AND");
			}
			querySb.append(queryParamList.get(i));

		}
		String query = querySb.toString();

		ResultSet rs = movieDAO.getResultSetForQuery(query);

		try {
			while (rs.next()) {
				movieList.add(processRow(rs));
			}
			rs.close();
		} catch (SQLException e) {

			System.out.println("Exception while processing resultSet : " + e.getMessage());
		}

		return movieList;
	}

	/*
	 * This function searches db for a particular movie.
	 * eg: http://localhost:8080/MovieMon/Movies/gladiator
	 */
	// TODO: remove dup entries, should return only one resource
	// TODO: title is case sensitive now. Need to fix
	public static List<MovieDBResult> searchMovie(String searchQuery) {

		List<MovieDBResult> movieList = new ArrayList<MovieDBResult>();
		String query = String.format("%s%s%s", "SELECT * FROM MOVIE WHERE TITLE = '", searchQuery, "';");

		ResultSet rs = movieDAO.getResultSetForQuery(query);

		try {
			while (rs.next()) {
				movieList.add(processRow(rs));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(
					"Exception while processing resultSet, query = " + searchQuery + " exception : " + e.getMessage());
		}
		return movieList;
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
		movie.setFileName(rs.getString("FILENAME"));
		movie.setMovieAbsPath(rs.getString("FILELOCATION"));
		movie.setTitle(rs.getString("TITLE"));
		movie.setImdbID(rs.getString("IMDBID"));
		movie.setYear(rs.getInt("YEAR"));
		movie.setGenre(rs.getString("GENRE"));
		movie.setImdbRating(rs.getString("IMDBRATING"));
		return movie;
	}

}

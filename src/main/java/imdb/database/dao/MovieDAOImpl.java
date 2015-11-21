package imdb.database.dao;

import imdb.database.model.MovieDBResult;
import imdb.utils.PropertyFileParser;

import java.sql.*;
import java.util.*;

public class MovieDAOImpl implements MovieMonDAO {

	@Override
	public List<MovieDBResult> getMovieWithName(String name) {
		createConnection();
		return null;
	}
	
	@Override
	public boolean insert(MovieDBResult movieDto) {
		//need to refactor this fn//

		String INSERT_INTO = "INSERT INTO MOVIE "
				+ "(FILENAME, FILELOCATION, TITLE, YEAR, "
				+ "IMDBRATING, IMDBID, GENRE)" + " VALUES ( ";

		String insertStmt = String.format(
				"%s '%s','%s','%s','%s','%s','%s','%s' %s",
				INSERT_INTO, movieDto.getFileName(),
				movieDto.getMovieAbsPath(), movieDto.getTitle(),
				movieDto.getYear(), movieDto.getImdbRating(),
				movieDto.getImdbID(), movieDto.getGenre(), ");");
		
		if(update(insertStmt)) {
			return true;
		}
		
		return false;
	}
	

	@Override
	public boolean insert(List<MovieDBResult> movieList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(List<MovieDBResult> movieList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MovieDBResult getDistinctMovie(MovieDBResult movie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection createConnection() {
		
		/*
		 * The db files will be stored in the HSQL_DB_FILE_LOCATION.
		 *  username ..Should this be read from properties ?
		 *  move this connection as an instance variable which is created only once ?
		 *  somethig like getConnection() ?
		 *  Now, for every movie object conn is opened & closed !!! 
		 */
		try {
			Class.forName(PropertyFileParser.HSQL_DB_DRIVER);
			return DriverManager.getConnection(PropertyFileParser.HSQL_DB_URL
					+ PropertyFileParser.HSQL_DB_FILE_LOCATION
					+ ";shutdown=true",	"SA", ""); // default password
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception while registering DB driver : "
					+ e.getMessage());

		}
		System.out.println("Couldn't return connection. Missed closing connection ? ! Returning Null");
		return null;
	}


	@Override
	public boolean update(String query) {
		if(performQuery(createConnection() , query)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean createTable(String tableName,
			HashMap<String, String> columnNames) {
		return false;
	}	

	public synchronized boolean performQuery(Connection conn, String query) {
		int result = -1;	

		try (Connection connction = conn) {
			result = conn.createStatement().executeUpdate(query);

			if (result == -1) {
				System.out.println("db error : " + query);
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("Exception while running query" + query +" Exception : " +e.getMessage());
			return false;
		}
		return  true;
	}

	public boolean updateDupMovies() {
		
		System.out.println();
		System.out.println("---------------Begin updating dup movie table-----------------------");
		/*
		 * Try to find entries with same imdbid from table MOVIE. Those entries
		 * will be dup movies. It's id & location are inserted to dup_movie
		 * table. Later user can pick the one he wants
		 */
		String query = "SELECT ID,IMDBID, FILELOCATION FROM MOVIE "
				+ "WHERE IMDBID IN "
				+ "( SELECT IMDBID FROM MOVIE "
				+ "GROUP BY IMDBID"
				+ " HAVING (COUNT(*) > 1))";
		Connection conn = createConnection();
		try {

			ResultSet rs = conn.createStatement().executeQuery(query);
			conn.close();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String imdbId = rs.getString("IMDBID");
				String fileLoc = rs.getString("FILELOCATION");

				if (false == insertDupMovie(id, imdbId, fileLoc)) {
					System.out.println("Error while inserting duplicate movie into the table : " + fileLoc);
				}
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("Exception while running query" + query
					+ " Exception : " + e.getMessage());
			return false;
		}
		System.out.println("---------------End updating dup movie table-----------------------");
		return true;
		
	}

	private boolean insertDupMovie(int id, String imdbId, String fileLoc) {

		String INSERT_INTO = "INSERT INTO DUP_MOVIES "
				+ "(MOVIE_ID, FILELOCATION, IMDBID)" + " VALUES ( ";

		String insertStmt = String.format("%s '%s', '%s','%s' %s", INSERT_INTO,
				id, fileLoc, imdbId, ");");
		System.out.println("Trying to update .. " +insertStmt);
		return update(insertStmt);

	}
	
	public boolean insertFailedMovie(String fileLoc) {
		
		String INSERT_INTO = "INSERT INTO FAILED_MOVIES "
				+ "(FILELOCATION)" + " VALUES ( ";

		String insertStmt = String.format("%s '%s' %s", INSERT_INTO, fileLoc, ");");
		System.out.println("Trying to insert failed movie : " +insertStmt);
		return update(insertStmt);
		
	}
	public List<MovieDBResult> getAll() {
		List<MovieDBResult> movieList = new ArrayList<MovieDBResult>();
		String query = "SELECT * FROM MOVIE;";
		Connection conn = createConnection();
		try {

			ResultSet rs = conn.createStatement().executeQuery(query);
			conn.close();
			while (rs.next()) {
				movieList.add(processRow(rs));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception while running query" + query
					+ " Exception : " + e.getMessage());
		}
		return movieList;		
	}

	public List<MovieDBResult> search(String searchQuery) {
		List<MovieDBResult> movieList = new ArrayList<MovieDBResult>();
		String query = String.format("%s%s%s","SELECT * FROM MOVIE WHERE TITLE LIKE '%",searchQuery,"%';");
		Connection conn = createConnection();
		try {

			ResultSet rs = conn.createStatement().executeQuery(query);
			conn.close();
			while (rs.next()) {
				movieList.add(processRow(rs));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("Exception while running query" + query
					+ " Exception : " + e.getMessage());
		}
		return movieList;	
	}
	
	private MovieDBResult processRow(ResultSet rs) throws SQLException {
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

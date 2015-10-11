package imdb.database.dao;

import imdb.database.model.MovieDBResult;
import imdb.utils.PropertyFileParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

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
	
}

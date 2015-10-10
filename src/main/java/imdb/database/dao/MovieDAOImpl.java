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

		try {
			Class.forName(PropertyFileParser.HSQL_DB_DRIVER);
			return DriverManager.getConnection(PropertyFileParser.HSQL_DB_URL
					+ PropertyFileParser.HSQL_DB_FILE_LOCATION,    // The db files will be stored in the HSQL_DB_FILE_LOCATION.
					"sa",                     // username ..Should this be read from properties file? 
					"");   					 // default password
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception while registering DB driver : " + e.getMessage());
		}

		return null;
	}


	@Override
	public boolean update(String query) {
		performQuery(createConnection() , query);
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
			System.out.println("Exception while running query" + query);
			return false;
		}
		return  true;
	}

}

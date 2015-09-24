package imdb.database.dao;

import imdb.database.model.MovieDBResult;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public interface MovieDAOImpl {

	public Connection createConnection();
	
	public List<MovieDBResult> getMovieWithName(String name);
	
	public boolean insert(List<MovieDBResult> movieList);
	
	public boolean delete(List<MovieDBResult> movieList);
	
	public MovieDBResult getDistinctMovie(MovieDBResult movie);
	
	public boolean update(String query);

	boolean createTable(String tableName, HashMap<String, String> columnNames);
}

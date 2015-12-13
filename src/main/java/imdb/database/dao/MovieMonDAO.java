package imdb.database.dao;

import imdb.database.model.MovieDBResult;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public interface MovieMonDAO {

	public Connection createConnection();
	
	public boolean closeConnection();
	
	public List<MovieDBResult> getMovieWithName(String name);
	
	public boolean insert(List<MovieDBResult> movieList);
	
	public boolean insert(MovieDBResult movieList);
	
	public boolean delete(List<MovieDBResult> movieList);
	
	public MovieDBResult getDistinctMovie(MovieDBResult movie);
	
	public boolean update(PreparedStatement pst);

	boolean createTable(String tableName, HashMap<String, String> columnNames);
}

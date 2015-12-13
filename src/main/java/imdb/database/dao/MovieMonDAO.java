package imdb.database.dao;

import imdb.database.model.MovieDBResult;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public interface MovieMonDAO {

	public Connection createConnection();
	
	public boolean closeConnection();
	
	public boolean insert(List<MovieDBResult> movieList);
	
	public boolean insert(MovieDBResult movieList);

	boolean createTable(String tableName, HashMap<String, String> columnNames);
}

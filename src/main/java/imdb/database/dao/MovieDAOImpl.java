package imdb.database.dao;

import java.sql.*;
import java.util.*;

import org.apache.log4j.*;

import imdb.*;
import imdb.database.model.*;
import imdb.utils.*;

public class MovieDAOImpl implements MovieMonDAO {
	
	final static Logger log = Logger.getLogger(OmdbApiConnector.class);
	//@Inject
	//private Connection conn;
	private static Connection conn;
	
	
	private static String INSERT_STMNT = "INSERT INTO MOVIE " + "(FILENAME, FILELOCATION, TITLE, YEAR, "
			+ "IMDBRATING, IMDBID, GENRE, POSTER, LANGUAGE, PLOT, RUNTIME, DIRECTOR, ACTORS)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private static String INSERT_INTO_DUPMOVIE_DETAILS = "INSERT INTO DUP_MOVIES " + "(MOVIE_ID, IMDBID, FILELOCATION)"
			+ " VALUES (?, ?, ?);";
	
	private static String INSERT_INTO_FAILED_DETAILS = "INSERT INTO FAILED_MOVIES " + "(FILELOCATION)" 	+ " VALUES ( ? );";
	
	private static String SELECT_STMNT_DUP_MOVIE_ENTRIES = "SELECT ID,IMDBID, FILELOCATION FROM MOVIE " + "WHERE IMDBID IN "
			+ "( SELECT IMDBID FROM MOVIE " + "GROUP BY IMDBID" + " HAVING (COUNT(*) > 1));";

	private static String LAST_INSERT_MOVIE_ID = "SELECT ID FROM MOVIE WHERE IMDBID = ? AND FILELOCATION = ?";
	
	private static String INSERT_INTO_USER_MOVIES = "INSERT INTO USER_MOVIES (USERID, MOVIE_ID) VALUES (?, ?);";
	
	private static String INSERT_INTO_FAVORITES = "INSERT INTO FAVORITES (MOVIE_ID, USER_ID) VALUES (?, ?);";
	
	public static String SELECT_SCANNED_FILES = "SELECT FILELOCATION FROM MOVIE";
	
	public static String SELECT_USER_ID = "SELECT ID FROM USERS WHERE USERNAME = ?";
	

	@Override
	public boolean insert(MovieDBResult movieDto) {

		List<? extends Object> hsql_args = Arrays.asList(movieDto.getFileName(), movieDto.getMovieAbsPath(),
				movieDto.getTitle(), movieDto.getYear(), movieDto.getImdbRating(), movieDto.getImdbID(),
				movieDto.getGenre(), movieDto.getPoster(), movieDto.getLanguage(), movieDto.getPlot(),
				movieDto.getRunTime(), movieDto.getDirector(), movieDto.getActors());

		PreparedStatement pst = prepareStatementFromArgs(INSERT_STMNT, hsql_args);

		if (performQuery(pst)) {
			return true;
		}

		return false;
	}
	
	public int getLastInsertMovieID(String imdbID, String fileLoc) {
		
		int movie_id = 0;
		PreparedStatement pst = prepareStatementFromArgs(LAST_INSERT_MOVIE_ID, Arrays.asList(imdbID, fileLoc));
		ResultSet rs = getResultSetForPst(pst);
		try {
			if(rs.next()) {
				movie_id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			log.error("Execption : " + e.getMessage());
		}
		return movie_id;
	
	}

	private PreparedStatement prepareStatementFromArgs(String statement, List<? extends Object> args) {

		Connection conn = createConnection();
		PreparedStatement pstmt = null;
		try {

			pstmt = conn.prepareStatement(statement);

			for (int i = 0; i < args.size(); i++) {
				if (args.get(i) instanceof String) {
					pstmt.setString(i+1, (String) args.get(i));
				} else if (args.get(i) instanceof Integer) {
					pstmt.setInt(i+1, (Integer) args.get(i));
				}
			}
		} catch (Exception e) {
			log.error("Error while preparing prepared statement.. " + args.toString() + e.getMessage());
		}
		return pstmt;

	}

	@Override
	public boolean insert(List<MovieDBResult> movieList) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Connection createConnection() {
		/*
		 * The db files will be stored in the HSQL_DB_FILE_LOCATION.
		 */
		if (conn == null) {

			try {
				Class.forName(PropertyFileParser.HSQL_DB_DRIVER);
				Connection localConn = DriverManager.getConnection(
						PropertyFileParser.HSQL_DB_URL + PropertyFileParser.HSQL_DB_FILE_LOCATION + ";shutdown=true",
						"SA", "");

				conn = localConn;

			} catch (Exception e) {
				log.error("Exception while getting the DB connection : " + e.getMessage());
				closeConnection();
				return null;

			}
		}
		return conn;

	}
	 
	@Override
	public boolean closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				log.error("Exception while closing the connection : " + e.getMessage());
				return false;
			}

		}

		return true;
	}

	@Override
	public boolean createTable(String tableName,
			HashMap<String, String> columnNames) {
		return false;
	}	

	public synchronized boolean performQuery(PreparedStatement pstmt) {
		int result = -1;

		try {
			result = pstmt.executeUpdate();

			if (result == -1) {
				log.error("db error : " + pstmt.toString());
				return false;
			}

		} catch (SQLException e) {
			log.error("Exception while running query" + pstmt.toString() + " Exception : " + e.getMessage());
			return false;
		}
		
		return  true;
	}

	public boolean updateDupMovies() {
		
		System.out.println();
		log.info("---------------Begin updating dup movie table-----------------------");
		/*
		 * Try to find entries with same imdbid from table MOVIE. Those entries
		 * will be dup movies. It's id & location are inserted to dup_movie
		 * table. Later user can pick the one he wants
		 */
		
		Connection conn = createConnection();
		try {

			ResultSet rs = conn.createStatement().executeQuery(SELECT_STMNT_DUP_MOVIE_ENTRIES);
			//conn.close();
			while (rs.next()) {
				Integer id = rs.getInt("ID");
				String imdbId = rs.getString("IMDBID");
				String fileLoc = rs.getString("FILELOCATION");

				if (false == insertDupMovie(id, imdbId, fileLoc)) {
					log.error("Error while inserting duplicate movie into the table : " + fileLoc);
				}
			}
			rs.close();
		} catch (Exception e) {
			log.error("Exception while running query updateDupMovies() Exception : " + e.getMessage());
			return false;
		}
		log.info("---------------End updating dup movie table-----------------------");
		return true;
		
	}

	private boolean insertDupMovie(int id, String imdbId, String fileLoc) {
	
		List<? extends Object> hsqlArgs = Arrays.asList(id, imdbId, fileLoc);
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_DUPMOVIE_DETAILS, hsqlArgs);
		
		//System.out.println("Trying to update .. " +insertStmt);
		return performQuery(pst);

	}
	
	public boolean insertFailedMovie(String fileLoc) {
		
		List<? extends Object> hsqlArgs = Arrays.asList(fileLoc);
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_FAILED_DETAILS, hsqlArgs);
		
		log.info("Trying to insert failed movie : " +fileLoc);
		return performQuery(pst);
		
	}


	public ResultSet getResultSetForQuery(String query) {
		Connection conn = createConnection();
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(query);

		} catch (Exception e) {
			log.error("Exception while running query" + query + " Exception : " + e.getMessage());
		}
		return rs;

	}
	public ResultSet getResultSetForPst(PreparedStatement pst) {
	
		ResultSet rs = null;
		try {
			rs = pst.executeQuery();

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return rs;

	}

	public boolean insertUserMovies(int userID, int movieID) {
		
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_USER_MOVIES, Arrays.asList(userID, movieID));
		log.info("inserting into user_movies table.. userid : " +userID + " movieID : " +movieID);
		//System.out.println("Trying to update .. " +insertStmt);
		return performQuery(pst);
		
	}
	
	public boolean insertIntoFavorites(int movieId, int userId) {
		
		PreparedStatement pst = prepareStatementFromArgs(INSERT_INTO_FAVORITES, Arrays.asList(movieId, userId));
		log.info("inserting into user_movies table.. userid : " + userId + " movieID : " + movieId);
		return performQuery(pst);
		
	}
	
	public int getUserIdForName(String userName) {
		PreparedStatement pst = prepareStatementFromArgs(SELECT_USER_ID, Arrays.asList(userName));
		ResultSet rs = getResultSetForPst(pst);
		int user_id = 0;
		try {
			if(rs.next()) {
				user_id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			log.error("Execption : " + e.getMessage());
		}
		return user_id;
	}
	
	//TODO: user input
	public List<String> getScannedFileList() {
		
		List<String> scannedFileList = new ArrayList<String>();
		ResultSet rs = getResultSetForQuery(SELECT_SCANNED_FILES);
		
		try {
			while(rs.next()) {
				scannedFileList.add(rs.getString("FILELOCATION"));
			}
			rs.close();
		} catch (SQLException e) {
			log.error("ResultSet processing ERROR. " + e.getMessage());
		}
		
		return scannedFileList;
		
	}
	
}

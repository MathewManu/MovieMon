package imdb.database.dao;

import java.sql.*;
import java.util.*;

import org.apache.log4j.*;

public class MovieDAOImplAuth extends MovieDAOImpl {

	final static Logger log = Logger.getLogger(MovieDAOImplAuth.class);

	private static String SELECT_PASSWD = "SELECT USERS.PASSWORD FROM USERS WHERE USERS.USERNAME = ?";
	private static String SELECT_UNAME = "SELECT USERS.USERNAME FROM USERS WHERE USERS.TOKEN = ?";
	private static String UPDATE_TOKEN = "UPDATE USERS SET TOKEN=? WHERE USERNAME=?";

	public boolean validate(String username, String password) {
		PreparedStatement pst = prepareStatementFromArgs(SELECT_PASSWD, Arrays.asList(username));
		return validatePassword(getResultSetForPst(pst), password);
	}

	/*
	 * insert token into user table
	 */
	public boolean insertToken(String token, String username) {
		PreparedStatement pst = prepareStatementFromArgs(UPDATE_TOKEN, Arrays.asList(token, username));
		log.info("inserting token into users table " + token);
		return performQuery(pst);

	}

	/*
	 * get username for a particular token. In case of server restart, details can
	 * be fetched using token coming from ui
	 */
	public String getUserNameFromDb(String token) {
		PreparedStatement pst = prepareStatementFromArgs(SELECT_UNAME, Arrays.asList(token));
		return getUserNameFromRs(getResultSetForPst(pst));
	}

	private String getUserNameFromRs(ResultSet rs) {
		log.info("---- Trying to get token from result set ----");
		try {
			if (rs.next()) {
				return rs.getString("USERNAME");
			}
		} catch (SQLException e) {
			log.error("Exception : " + e.getMessage());
		}
		return null;
	}
	/*
	 * validate password against db password
	 * TODO: should do md5 for passwords
	 */
	private boolean validatePassword(ResultSet rs, String password) {
		log.info("---- validating password from db ----");
		try {
			if (rs.next()) {
				return rs.getString("PASSWORD").equals(password);
			}
		} catch (SQLException e) {
			log.error("Exception : " + e.getMessage());
		}
		return false;
	}
}

package imdb.rest.favorites;

import java.math.*;
import java.security.*;
import java.util.*;

import org.apache.log4j.*;

import imdb.auth.*;
import imdb.constants.*;
import imdb.database.dao.*;

public class UserAuthManager {

	final static Logger log = Logger.getLogger(UserAuthManager.class);
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl(MovieDaoImplSelector.USERAUTH);

	/*
	 * validate username & passwd. Generate & insert token into db
	 */
	public String authenticateUser(String username, String password) {

		boolean validUser = ((MovieDAOImplAuth) movieDAO).validate(username, password);
		if (validUser) {
			log.info("user : " + username + " has been validated!");
			String token = generateToken();

			if (((MovieDAOImplAuth) movieDAO).insertToken(token,username)) {
				AuthenticationUtils.setToken(token);
				AuthenticationUtils.setCurrentlyLoggedInUser(username);
			} else {
				log.error("token insert failed !!");
				return null;
			}
			return token;
		}
		return null;
	}

	private String generateToken() {
		Random random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		return token;
	}

	public String getUserInfoFromDb(String token) {

		String userName = ((MovieDAOImplAuth) movieDAO).getUserNameFromDb(token);
		if (null == userName) {
			log.error("could not find username for token");
			return userName;
		}
		AuthenticationUtils.setToken(token);
		AuthenticationUtils.setCurrentlyLoggedInUser(userName);

		return token;
	}
}

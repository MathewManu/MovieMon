package imdb.rest;

import java.math.*;
import java.security.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;

import imdb.auth.*;

@Path("/authentication")
public class AuthenticationController {
	
	final static Logger logger = Logger.getLogger(AuthenticationController.class);

	@POST
	@Path("signin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Credentials credentials) {

		try {
			String username = credentials.getUsername();
			String password = credentials.getPassword();

			authenticateUser(username, password);
			String token = generateToken(username);

			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private String generateToken(String username) {
		
		Random random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		return token;
		//TODO: insert this token in the db with the user.
	}

	private void authenticateUser(String username, String password) throws Exception {
		//TODO: validate from DB
		if (username.equals("guest") && password.equals("guest")) {
			logger.debug("<<<<<  Authenticated  >>>>");
		} else {
			logger.debug("<<<<<  Not Authenticated  >>>>" + username + " " +password);
			//TODO: custom exception needs to be added
			throw new Exception();
		}

	}
}

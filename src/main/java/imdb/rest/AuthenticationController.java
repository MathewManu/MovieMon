package imdb.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.log4j.*;

import imdb.auth.*;
import imdb.rest.favorites.*;

@Path("/authentication")
public class AuthenticationController {
	
	final static Logger logger = Logger.getLogger(AuthenticationController.class);
	private UserAuthManager userAuthManager = new UserAuthManager();

	@POST
	@Path("signin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Credentials credentials) {

		try {
			String username = credentials.getUsername();
			String password = credentials.getPassword();

			String token = userAuthManager.authenticateUser(username, password);
			if(null == token) {
				logger.error("Authentication failed for user : " + username);
				//TODO: throw correct exception
				throw new Exception(); 
			}
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}

package imdb.auth;

import java.io.*;
import java.security.*;
import javax.annotation.Priority;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.*;

import imdb.exceptions.*;
import imdb.rest.favorites.*;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {

	final static Logger logger = Logger.getLogger(AuthenticationFilter.class);
	
    private static final Response ACCESS_DENIED = Response.status(Response.Status.FORBIDDEN).build();
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
			//TODO: exception ?
			// throw new NotAuthorizedException("failed");
			requestContext.abortWith(ACCESS_DENIED);
			return;
		}
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {
			validateToken(token);
			
		} catch (Exception e) {
			logger.debug("exception : token validation failed");
			requestContext.abortWith(ACCESS_DENIED);
			return;

		}

		final String principalUsername = AuthenticationUtils.getCurrentlyLoggedinUser();
		
		requestContext.setSecurityContext(new SecurityContext() {
					
			@Override
			public Principal getUserPrincipal() {
				return new Principal() {

					@Override
					public String getName() {
						return principalUsername;
					}
					
				};
			}
			
			@Override
			public boolean isUserInRole(String role) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getAuthenticationScheme() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		AuthenticationUtils.setCurrentlyLoggedInUser(principalUsername);
	}
	
	/*
	 * validate token against the token coming from ui
	 * if saved token is null, try to get data from db using the incoming token from ui
	 */
	private void validateToken(String token) throws InvalidTokenException {

		logger.debug("validating token...");
		if (token == null) {
			throw new InvalidTokenException("invalid token");
		}
		String currentToken = AuthenticationUtils.getToken();

		if (currentToken == null) {
			logger.info("Saved token is null, Trying to fetch from db");

			UserAuthManager userAuthManager = new UserAuthManager();
			currentToken = userAuthManager.getUserInfoFromDb(token);
		}
		if (null == currentToken || !currentToken.equals(token)) {
			throw new InvalidTokenException("invalid token");
		}
		logger.info("token has been validated");
	}

}

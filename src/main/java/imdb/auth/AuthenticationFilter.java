package imdb.auth;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.*;
import org.glassfish.jersey.internal.util.Base64;

@Provider 
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {

	final static Logger logger = Logger.getLogger(AuthenticationFilter.class);
	
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    
    private static final Response ACCESS_DENIED = Response.status(Response.Status.FORBIDDEN).build();
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// get request headers.
		final MultivaluedMap<String, String> headers = requestContext.getHeaders();

		// fetch authorization header
		final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

		if (authorization == null || authorization.isEmpty()) {
			requestContext.abortWith(ACCESS_DENIED);
			return;

		}
		// Get encoded username and password
		final String encodedUserPass = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		// decode username & password

		String usernameAndPassword = new String(Base64.decode(encodedUserPass.getBytes()));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		logger.debug("Username & password : " + username + " " + password);

		if (!isUserAllowed(username, password)) {
			requestContext.abortWith(ACCESS_DENIED);
			return;
		}
		
		requestContext.setSecurityContext(new SecurityContext() {
					
			@Override
			public Principal getUserPrincipal() {
				return new Principal() {

					@Override
					public String getName() {
						return username;
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
		
		AuthenticationUtils.setCurrentlyLoggedInUser(username);
	}

	private boolean isUserAllowed(String username, String password) {

		logger.debug("isUserAllowed method.. " +username +" " +password);
		//TODO:should check from db for user
		if (username.equals("guest") && password.equals("guest")) {
			return true;
		}

		return false;
	}

}

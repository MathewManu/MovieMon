package imdb.auth;


public class AuthenticationUtils {

	
	private static String userName = "";
	private static String token;

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		AuthenticationUtils.token = token;
	}

	public static  String getCurrentlyLoggedinUser() {
		return userName;
	}

	public static void setCurrentlyLoggedInUser(String username) {
		userName = username;
		
	}

}

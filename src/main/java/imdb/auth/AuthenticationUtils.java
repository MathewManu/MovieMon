package imdb.auth;


public class AuthenticationUtils {

	
	private static String userName = "";

	public static  String getCurrentlyLoggedinUser() {
		return userName;
	}

	protected static void setCurrentlyLoggedInUser(String username) {
		userName = username;
		
	}

}

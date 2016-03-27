package imdb.constants;

/*
 * The class was implemented keeping in mind the multiple DAO impl classes that can
 * come in the future. This class is a list of constant strings that can help the developer
 * to select which DAO impl to choose from. 
 * Added for extensibilty
 */
public class MovieDaoImplSelector {

	//TODO : make these enums.
	public static final String DEFAULT_MOVIE_SELECTOR = "DEF";
	public static final String USER_SPECIFIC_FEATURES_MOVIE_SELECTOR = "USERSPECIFICFEATURES";
	public static final String WATCHLIST = "WATCHLIST";
	
}

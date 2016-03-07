package imdb.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.*;

import imdb.*;

public class MovieNameutils {
	
	final static Logger log = Logger.getLogger(MovieNameutils.class);

	public static void updateGlobalGibberishMap(String fileName, String movieTitle) {
		log.debug(">>>>>inside GlobalGiberishMap update function " + fileName + " title : " + movieTitle);
		if (!fileName.equals(movieTitle)) {
			String[] fileNameTokens = fileName.split(" ");
			List<String> movieTitleTokens = Arrays.asList(movieTitle.split(" "));

			for(String token : fileNameTokens) {
				if (!MovieNameResolver.globalGibberishSet.contains(token) && !movieTitleTokens.contains(token)) {
					
					int tokenCount = MovieNameResolver.gibberishMap.containsKey(token)
							? MovieNameResolver.gibberishMap.get(token) : 0;
					
					MovieNameResolver.gibberishMap.put(token, tokenCount +1);
					log.debug("Gibberish set insert.. : " +token +" count : " + tokenCount+1);
					
				}
			}
		}
	}
}

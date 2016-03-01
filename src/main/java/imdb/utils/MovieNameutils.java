package imdb.utils;

import java.util.Arrays;
import java.util.List;

import imdb.DDGSearch;
import imdb.MovieNameResolver;

public class MovieNameutils {

	public static void updateGlobalGibberishMap(String fileName, String movieTitle) {
		if (!fileName.equals(movieTitle)) {
			String[] fileNameTokens = fileName.split(" ");
			List<String> movieTitleTokens = Arrays.asList(movieTitle.split(" "));

			for(String token : fileNameTokens) {
				if (!MovieNameResolver.globalGibberishSet.contains(token) && !movieTitleTokens.contains(token)) {

					int tokenCount = MovieNameResolver.gibberishMap.get(token);
					MovieNameResolver.gibberishMap.put(token, tokenCount +1);
				}
			}
		}
	}
}

package imdb;

import java.util.*;

public class MovieNameProcessor {

	public void updateMovieObjectsWithCorrectNames(List<MovieObject> allMovieObjects) {
			
			for(MovieObject movie : allMovieObjects) {
				//as of now just split & pass the first part//
				movie.setCorrectMovieName(TextProcessingUtils.getFileNameWithOutExt(movie.getMovieName()));
			}
			
	}

}

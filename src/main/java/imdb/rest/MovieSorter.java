package imdb.rest;

import imdb.constants.SortType;
import imdb.database.model.MovieDBResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

public class MovieSorter {

	final static Logger log = Logger.getLogger(MovieSorter.class);

	private Comparator<MovieDBResult> ascSortMovieByNameComparator = (MovieDBResult res1, MovieDBResult res2) -> { return res1.getTitle().compareTo(res2.getTitle());};
	private Comparator<MovieDBResult> descSortMovieByNameComparator = (MovieDBResult res1, MovieDBResult res2) -> { return res2.getTitle().compareTo(res1.getTitle());};
	
	private Comparator<MovieDBResult> acsSortMovieByRatings = (MovieDBResult res1, MovieDBResult res2) -> { 
		if (Double.parseDouble(res1.getImdbRating()) > Double.parseDouble(res2.getImdbRating())) return 1; 
		else if (Double.parseDouble(res1.getImdbRating()) < Double.parseDouble(res2.getImdbRating())) return -1;
		else return 0;};

	private Comparator<MovieDBResult> descSortMovieByRatings = (MovieDBResult res1, MovieDBResult res2) -> {
		if (Double.parseDouble(res1.getImdbRating()) < Double.parseDouble(res2.getImdbRating())) return 1; 
		else if (Double.parseDouble(res1.getImdbRating()) > Double.parseDouble(res2.getImdbRating())) return -1;
		else return 0;};


	public List<MovieDBResult> sortMovieByName(String sortingType) {

		List<MovieDBResult> results = new ArrayList<MovieDBResult>();
		results = getUnsortedMovies();

		log.info(String.format("Sorting movies by name in %s order", sortingType));
		if (sortingType.equals(SortType.ASCENDING.getStringValue())) {
			Collections.sort(results, ascSortMovieByNameComparator);
		} else {
			Collections.sort(results, descSortMovieByNameComparator);
		}

		return results;
	}

	public List<MovieDBResult> sortMovieByRating(String sortingType) {
		List<MovieDBResult> results = new ArrayList<MovieDBResult>();
		results = getUnsortedMovies();
		log.info(String.format("Sorting movies by imdb rating in %s order", sortingType));
		if (sortingType.equals(SortType.ASCENDING.getStringValue())) {
			Collections.sort(results, acsSortMovieByRatings);
		} else {
			Collections.sort(results, descSortMovieByRatings);
		}
		return results;
	}

	private List<MovieDBResult> getUnsortedMovies() {		
		return RestRequestProcessor.getMovies("", "");
		
	}
}

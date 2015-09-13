package imdb;

import java.util.*;

public interface BaseApiConnector {
	void connect();

	void updateMovieObjectsWithApiData(List<MovieObject> allMovieObjects);
}

package imdb;

//This class represents a movie object completely
//it includes the fileName that we are reading from a directory 
//And that movie's abs Path
//Then the movie object that is from Omdb api
//This will be the obj that we ll serialize and save in db.
//Add required attributes in this class.

public class MovieObject {
	
	private String movieName;
	private String correctMovieName;
	private String movieAbsPath;
	private Movie movieObjFromApi;

	public MovieObject(String fileName, String absPath) {
		this.movieName = fileName;
		this.movieAbsPath = absPath;
	}

	public String getMovieName() {
		return movieName;
	}

	public String getMovieAbsPath() {
		return movieAbsPath;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public void setMovieAbsPath(String movieAbsPath) {
		this.movieAbsPath = movieAbsPath;
	}

	public Movie getMovieObjFromApi() {
		return movieObjFromApi;
	}

	public void setMovieObjFromApi(Movie movieObjFromApi) {
		this.movieObjFromApi = movieObjFromApi;
	}

	public String getCorrectMovieName() {
		return correctMovieName;
	}

	public void setCorrectMovieName(String correctMovieName) {
		this.correctMovieName = correctMovieName;
	}

}

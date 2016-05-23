package imdb;

/*
 *This class represents a movie object completely
 *it includes the fileName & movie's abs path that we are reading from a directory 
 *Then the movie object that is from Omdb api [ eg: http://www.omdbapi.com/?i=tt0332452 ]
 *imdb id will be the unique key
 *This will be the obj that we ll serialize and save in db.
 *Add required attributes in this class.
 */

public class MovieObject {
	
	private String movieName;
	private String updatedfileName;
	private String titleFromTmdb;

	private String movieAbsPath;
	private Movie movieObjFromApi;

	private int year;	
	private String imdbId;



	public String getUpdatedfileName() {
		return updatedfileName;
	}

	public int getYear() {
		return year;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setUpdatedfileName(String updatedfileName) {
		this.updatedfileName = updatedfileName;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public MovieObject(String fileName, String absPath) {
		this.movieName = fileName;
		this.movieAbsPath = absPath;
		this.year = 9999;
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
	public String getTitleFromTmdb() {
		return titleFromTmdb;
	}

	public void setTitleFromTmdb(String titleFromTmdb) {
		this.titleFromTmdb = titleFromTmdb;
	}


}

package imdb.database.model;

public class MovieDBResult {
	
	private String fileName;
	private String movieAbsPath;
	
	private String title;
	private int year;
	private String imdbRating;
	private String imdbID;	
	private String genre;
		
	//private String updatedfileName;
	//private String runTime;
	//private String director;
	//private String plot;	
	//private String language;
	
	public String getFileName() {
		return fileName;
	}
	public String getMovieAbsPath() {
		return movieAbsPath;
	}
	public String getTitle() {
		return title;
	}
	public int getYear() {
		return year;
	}
	public String getImdbRating() {
		return imdbRating;
	}
	public String getImdbID() {
		return imdbID;
	}
	public String getGenre() {
		return genre;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setMovieAbsPath(String movieAbsPath) {
		this.movieAbsPath = movieAbsPath;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}


}

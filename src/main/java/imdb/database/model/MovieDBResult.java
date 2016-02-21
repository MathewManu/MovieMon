package imdb.database.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class MovieDBResult {
	
	private String fileName;
	private String movieAbsPath;
	private int id ;
	private String title;
	private int year;
	private String imdbRating;
	private String imdbID;	
	private String genre;
	private String runTime;
	private String director;
	private String actors;
	private String plot;	
	private String language;
	private String poster;
	private boolean isFavorite;
	private boolean isInWatchList;
	private boolean isAlreadyWatched;
	
	
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
	public String getRunTime() {
		return runTime;
	}
	public String getPlot() {
		return plot;
	}
	public String getLanguage() {
		return language;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getDirector() {
		return director;
	}
	public String getActors() {
		return actors;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFavorite() {
		return isFavorite;
	}
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public boolean isInWatchList() {
		return isInWatchList;
	}
	public void setInWatchList(boolean isInWatchList) {
		this.isInWatchList = isInWatchList;
	}
	public boolean isAlreadyWatched() {
		return isAlreadyWatched;
	}
	public void setAlreadyWatched(boolean isAlreadyWatched) {
		this.isAlreadyWatched = isAlreadyWatched;
	}


}

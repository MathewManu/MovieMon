package imdb;

import com.google.gson.annotations.*;

public class Movie {

	@SerializedName("Title")
	private String title;

	@SerializedName("Year")
	private int year;

	@SerializedName("Rated")
	private String rated;

	@SerializedName("Released")
	private String released; // should be date ?

	@SerializedName("Runtime")
	private String runTime;

	@SerializedName("Genre")
	private String genre; // multiple values

	@SerializedName("Director")
	private String director; // multiple

	@SerializedName("Actors")
	private String actors; // multiple

	@SerializedName("Plot")
	private String plot;
	
	@SerializedName("Language")
	private String language;
	
	@SerializedName("Country")
	private String country;
	
	@SerializedName("Awards")
	private String awards;
	
	@SerializedName("Poster")
	private String poster;
	
	@SerializedName("Metascore")
	private String metaScore;
	
	private String imdbRating;
	
	private String imdbVotes;
	
	private String imdbID;
	
	@SerializedName("Type")
	private String type;
	
	@SerializedName("Response")
	private String response;

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public String getRated() {
		return rated;
	}

	public String getReleased() {
		return released;
	}

	public String getRunTime() {
		return runTime;
	}

	public String getGenre() {
		return genre;
	}

	public String getDirector() {
		return director;
	}

	public String getActors() {
		return actors;
	}

	public String getPlot() {
		return plot;
	}

	public String getLanguage() {
		return language;
	}

	public String getCountry() {
		return country;
	}

	public String getAwards() {
		return awards;
	}

	public String getPoster() {
		return poster;
	}

	public String getMetaScore() {
		return metaScore;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public String getImdbVotes() {
		return imdbVotes;
	}

	public String getImdbID() {
		return imdbID;
	}

	public String getType() {
		return type;
	}

	public String getResponse() {
		return response;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public void setMetaScore(String metaScore) {
		this.metaScore = metaScore;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public void setImdbVotes(String imdbVotes) {
		this.imdbVotes = imdbVotes;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}

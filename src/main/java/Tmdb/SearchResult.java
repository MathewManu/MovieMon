package Tmdb;

public class SearchResult {

	private String id;
	private String title;
	//private String original_title;
	private Double popularity;
	
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public Double getPopularity() {
		return popularity;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}
	
	
}

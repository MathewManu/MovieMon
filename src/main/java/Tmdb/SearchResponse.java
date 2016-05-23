package Tmdb;

import java.util.*;

public class SearchResponse {

	private int page;
	private List<SearchResult> results = new ArrayList<SearchResult>();
	private int total_results;
	private int total_pages;
	
	public int getPage() {
		return page;
	}
	public List<SearchResult> getResults() {
		return results;
	}
	public int getTotal_results() {
		return total_results;
	}
	public int getTotal_pages() {
		return total_pages;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}
	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}
	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}
	
}

package imdb.constants;

public enum MovieSortParams {
	
	NAME ("name"),
	RATINGS ("ratings"),
	RECENTLYADDED ("recentlyadded");
	
	private String stringValue;
	
	private MovieSortParams(String stringv) {
		this.stringValue = stringv;
	}
	
	public String getStringValue() {
		return stringValue;
	}

}

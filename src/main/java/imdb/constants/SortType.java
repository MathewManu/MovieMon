package imdb.constants;

public enum SortType {

	ASCENDING("asc"),
	DESCENDING("desc");
	
    private String stringValue;
	
	private SortType(String stringv) {
		this.stringValue = stringv;
	}
	
	public String getStringValue() {
		return stringValue;
	}
}

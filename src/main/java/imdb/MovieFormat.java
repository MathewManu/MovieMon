package imdb;

import imdb.utils.*;

public enum MovieFormat {
	MP4("mp4"),
	AVI("avi"),
	MKV("mkv");
	
	private String format;
	
	private MovieFormat(String format) {
		this.format = format;
	}
	
	public String getMovieFormat() {
		return format;
	}
	public static boolean isValidFormat(String format) {
		return MovieMonUtils.checkIfValuePresent(MovieFormat.class, format);
	}
}

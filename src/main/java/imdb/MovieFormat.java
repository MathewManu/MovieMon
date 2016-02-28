package imdb;

import imdb.utils.*;

public enum MovieFormat {
	MP4("mp4"),
	AVI("avi"),
	MKV("mkv"),
	M4V("m4v"),
	DIVX("divx"),
	RMVB("rmvb"),
	VOB("vob"),
	DAT("dat"),
	WMV("wmv"),
	FLV("flv");
	//3GPP("3gpp");
	//TODO: (manu) should change to a list ?????
	
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

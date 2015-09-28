package imdb;

import java.util.*;
import java.util.regex.*;

public class MovieNameResolver {
	
	public MovieNameResolver() {
		init();
	}
	//static ?
	private static List<Pattern> rxs = new ArrayList<Pattern>();
	
	
	public static List<Pattern> getRxs() {
		return rxs;
	}
	
	private void init() {

		String arr[] = new String[] { "DvdRip", "aXXo", "xVid", "YIFY",
				"torrent", "BRRip", "AAC", "HQ", "720p", "x264", "bluray" };

		for (String word : arr) {
			rxs.add(Pattern.compile(word, Pattern.CASE_INSENSITIVE));
		}
	}
	
	public boolean process(MovieObject movieObj) {
	
		String orgFileName = movieObj.getMovieName();
		movieObj.setUpdatedfileName(getFileNameWithOutExt(orgFileName));
		
		//update movie year instance variable.
		//and replace the movie year from the fileName.
		processMovieYear(movieObj);
		replaceNonWordChars(movieObj);
		replaceJunkWords(movieObj);
		
		System.out.println("searching ... " +movieObj.getUpdatedfileName() +" "+movieObj.getYear());
		
		String imdbId ="";
		if (movieObj.getYear() != 9999) {
			imdbId = GSearch.findId(movieObj.getUpdatedfileName(), movieObj.getYear());
		} else {
			imdbId = GSearch.findId(movieObj.getUpdatedfileName());
		}
		//System.out.println("Imdb id found for the movie : "+movieObj.getMovieName() +" is : "+imdbId);
		
		if(!imdbId.isEmpty()) {
			movieObj.setImdbId(imdbId);
			return true;
		}
			
		//check for error cases later
		System.out.println("Coulnd not find imdb id for movie : " +movieObj.getMovieName());
		return false;
		
	}

	private void replaceJunkWords(MovieObject movieObj) {

		String movieName = movieObj.getUpdatedfileName();
		
		for (Pattern rx : getRxs()) {
			movieName = rx.matcher(movieName).replaceAll(" ");
		}

		// replace mulitple occurances of spaces with single one
		movieName = movieName.replaceAll("\\s+", " ");
		movieObj.setUpdatedfileName(movieName);
	//	System.out.println("$$$$$$$$$$$$$$ : " + movieName);
	}

	/*
	 * update movie year in movie object
	 */
	private void processMovieYear(MovieObject movieObj) {
		// multiple years ?
		// numbers like 12222, movie names like 2012 2012DvDrip how to handle ?
		String yearPattern = "\\d{4}";

		Pattern p = Pattern.compile(yearPattern);
		Matcher m = p.matcher(movieObj.getUpdatedfileName());

		if (m.find()) {
			int possibleYear = Integer.parseInt(m.group());

			if (1900 < possibleYear) {
				movieObj.setYear(possibleYear);
				movieObj.setUpdatedfileName(m.replaceFirst(" "));
			}

		}
	}

	/*
	 * Replace all the non-word characters with space [^a-zA-Z0-9]. ( all extra
	 * characters like _,{,( etc )
	 */
	private void replaceNonWordChars(MovieObject movieObj) {

		String nonWordCharPattern = "[^a-zA-Z0-9]";
		Pattern p = Pattern.compile(nonWordCharPattern);

		Matcher m = p.matcher(movieObj.getUpdatedfileName());
		String processedFileName = m.replaceAll(" ");
		
		movieObj.setUpdatedfileName(processedFileName);

	}

	// take movObj as parameter & update ext in the obj
	public static String getFileNameWithOutExt(String fileName) {

		String ext = "";
		String fileNameWithoutExt = "";

		int indexOfPeriod = fileName.lastIndexOf(".");
		
		if (-1 != indexOfPeriod) {
			fileNameWithoutExt = fileName.substring(0, indexOfPeriod);
			ext = fileName.substring(indexOfPeriod + 1);
			
		} else {
			// handle this case//
		}

		return fileNameWithoutExt;

	}

}

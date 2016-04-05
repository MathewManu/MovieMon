package imdb;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;

import javax.print.attribute.HashAttributeSet;

import org.apache.log4j.*;

public class MovieNameResolver {
	
	final static Logger log = Logger.getLogger(MovieNameResolver.class);
	
	public MovieNameResolver() {
		init();
	}
	//static ?
	private static List<Pattern> rxs = new ArrayList<Pattern>();
	
	
	public static List<Pattern> getRxs() {
		return rxs;
	}
	
	private void init() {
		
		//TODO: should get updated dynamically 
		String arr[] = new String[] { "DvdRip", "aXXo", "xVid", "YIFY",
				"torrent", "BRRip", "HDRIP", "ViP3R", "AAC", "HQ", "720p", "h264", "bluray", "x264", "1080p","480p", "torentz", "www.torentz.3xforum.ro.avi"  };
		
		for (String word : arr) {
			rxs.add(Pattern.compile(word, Pattern.CASE_INSENSITIVE));
		}
		
	}
	
	public boolean process(MovieObject movieObj) {
	
		String orgFileName = movieObj.getMovieName();
		movieObj.setUpdatedfileName(getFileNameWithOutExt(orgFileName));
		
		/*
		 * Replace non word characters from movie name, Possible junk words [ need to add logic to update this list ]
		 * Try to extract movie year.
		 * set the processed movie name to the movieObj
		 */
		
		replaceNonWordChars(movieObj);
		replaceJunkWords(movieObj);
		processMovieYear(movieObj);
		
		return true;
		
	}

	private void replaceJunkWords(MovieObject movieObj) {

		String movieName = movieObj.getUpdatedfileName();
		
		for (Pattern rx : getRxs()) {
			movieName = rx.matcher(movieName).replaceAll(" ");
		}

		// replace mulitple occurances of spaces with single one
		movieName = movieName.replaceAll("\\s+", " ");
		movieObj.setUpdatedfileName(movieName);

	}

	/*
	 * update movie year in movie object
	 */
	private void processMovieYear(MovieObject movieObj) {
		
		//considering movies from 1900 to 2999
		String yearPattern = "[12][09][0-9][0-9]";

		Pattern p = Pattern.compile(yearPattern);
		Matcher m = p.matcher(movieObj.getUpdatedfileName());

		if (m.find()) {
			int possibleYear = Integer.parseInt(m.group());

			movieObj.setYear(possibleYear);
			
			if(m.replaceFirst("").isEmpty()) {
				//handling movienames like 2012DvDrip.avi
				log.error("------ Not replacing year in movie : "+movieObj.getUpdatedfileName());
			}
			else {
				//remove year from the filename
				// replace mulitple occurances of spaces with single one
				movieObj.setUpdatedfileName(m.replaceFirst("").replaceAll("\\s+", " "));
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
			
		} 

		return fileNameWithoutExt;

	}

}

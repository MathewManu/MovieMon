package imdb.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.*;

import imdb.*;

public class GibberishWordsUtils {

	final static Logger log = Logger.getLogger(GibberishWordsUtils.class);

	private static Map<String, Integer> gibberishMap= new ConcurrentHashMap<String, Integer>();
	public static Set<String> globalGibberishSet =  Collections.synchronizedSet(new HashSet<String>());

	private static String arr[] = new String[] { "DvdRip", "aXXo", "xVid", "YIFY",
		"torrent", "BRRip", "HDRIP", "ViP3R", "AAC", "HQ", "720p", "h264", "bluray", "x264", "1080p","480p", "torentz", "www.torentz.3xforum.ro.avi"  };
	static {
		Collections.addAll(globalGibberishSet, arr);
	}

	private GibberishWordsUtils () {
		//private constructor
	}

	//all tokens in the gibberish map will be compared with the movie title strings in a case insensitive way 
	public static void updateGlobalGibberishMap(String fileName, String movieTitle) {
		log.debug(">>>>>inside GlobalGiberishMap update function " + fileName + " title : " + movieTitle);

		if (!fileName.equals(movieTitle)) {
			String[] fileNameTokens = fileName.split(" ");
			List<String> movieTitleTokens = Arrays.asList(movieTitle.split(" "));

			for(String token : fileNameTokens) {				
				if (!globalGibberishSet.contains(token.toLowerCase()) && !movieTitleTokens.contains(token)) {
					putToGibberishMap(token);	
				}
			}
		}
	}

	private static void putToGibberishMap(String str) {
		// check if string is a number :
		if (str.matches("-?\\d+(\\.\\d+)?")) {
			int count = gibberishMap.containsKey(str)
					? gibberishMap.get(str) : 0;
					gibberishMap.put(str.toLowerCase(), count + 1);
					log.debug("Gibberish Map update >>> key : " + str + "vale : " + count);
		}
	}

	public static void updateGlobalSet(String arr[]) {
		
	}

}

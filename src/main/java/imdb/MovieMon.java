package imdb;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MovieMon {

	public static final String SRC_DIR = "C:\\manu\\testDir\\movie_test";
	
	private static List<MovieObject> allMovieObjects;
	
	private static BaseApiConnector apiConnector = new OmdbApiConnector();
	
	public static void process() {
		
		if(false == updateMovieNamesFromRootDir(SRC_DIR)) {
			System.out.println("Could not find any movies at path : "+SRC_DIR);
		}
		
		updateMovieObjectsWithCorrectNames(); 
		//method name ? 
		// gladiatorDVDrip.avi & gladiator.avi
		//do a search for "angry" in omdb 

		 apiConnector.updateMovieObjectsWithApiData(allMovieObjects);
		 System.out.println(allMovieObjects.size());
		
	}
	private static void updateMovieObjectsWithCorrectNames() {
		(new MovieNameProcessor()).updateMovieObjectsWithCorrectNames(allMovieObjects);
		
	}
	
	
	public static boolean updateMovieNamesFromRootDir(String srcDirectory) {

		Path srcDir = Paths.get(srcDirectory);
		SimpleFileWalk dirWalk = new SimpleFileWalk();

		System.out.println("Scanning the mentioned directory : "+srcDirectory + "for Movies, Please wait...");
		
		try {
			Files.walkFileTree(srcDir, dirWalk);
		} catch (IOException e) {
			System.out.println("walkFileTree exception : " + e.getMessage());
		}
		
		allMovieObjects = dirWalk.getAllMovieObjs();
		return allMovieObjects.isEmpty() ? false : true; //Boolean class required ?
	}

}

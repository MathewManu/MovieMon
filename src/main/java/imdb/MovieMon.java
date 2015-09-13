package imdb;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MovieMon {

	//should be from properties file or userinput 
	public static final String SRC_DIR = "C:\\manu\\testDir\\movie_test";
	
	public static Set<String> fileNames = null;

	private static List<MovieObject> allMovieObjects;

	
	
	public static void process() {
		
		//check with anushya, where should we do the new of this one
		//this config has to come from the user
		BaseApiConnector apiConnector = new OmdbApiConnector();
		
		updateMovieNamesFromRootDir();
		updateMovieObjectsWithCorrectNames(); 
		//method name ? should return correct movie names only !!!
		 //while generating correct name, 2 names can become same
		// gladiatorDVDrip.avi & gladiator.avi
		//need to handle this case as well

		 apiConnector.updateMovieObjectsWithApiData(allMovieObjects);
		 System.out.println(allMovieObjects.size());
		
	}
	private static void updateMovieObjectsWithCorrectNames() {
		(new MovieNameProcessor()).updateMovieObjectsWithCorrectNames(allMovieObjects);
		
	}
	
	
	private static void updateMovieNamesFromRootDir() {

		Path startingDir = Paths.get(SRC_DIR);
		SimpleFileWalk pf = new SimpleFileWalk();

		try {
			Files.walkFileTree(startingDir, pf);
		} catch (IOException e) {
			System.out.println("walkFileTree exception : " + e.getMessage());
		}
		fileNames = pf.getUniqueFiles();
		allMovieObjects = pf.getAllMovieObjs();

	}

}

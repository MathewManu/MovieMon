package imdb;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MovieMon {

	//public static final String SRC_DIR = "C:\\manu\\testDir\\movie_test";
	public static final String SRC_DIR = "F:\\mnu\\Films\\Misc\\fromsree\\4 u Asha";
	
	private static List<MovieObject> allMovieObjects;
	
	//private static BaseApiConnector apiConnector = new OmdbApiConnector();
	
	public static void process() {
		
		if(false == updateMovieNamesFromRootDir(SRC_DIR)) {
			System.out.println("Could not find any movies at path : "+SRC_DIR);
		}
		
		System.out.println("--------Finished Processing----------");
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
		
		//need this ??? no 
		allMovieObjects = dirWalk.getAllMovieObjs();
		return allMovieObjects.isEmpty() ? false : true;
	}

}

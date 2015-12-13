package imdb;

import imdb.database.dao.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MovieMon {

	private static String srcDirectory;
	private static List<MovieObject> allMovieObjects;
	
/*	@Inject
	private MovieMonDaoFactory fact; */
	
	public static void process() {
		
		if (false == updateMovieNamesFromRootDir(srcDirectory)) {
			System.out.println("Could not find any movies at path : "+srcDirectory);
			return;
		}
		//process dup movies here ?
		MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
		movieDAO.updateDupMovies();
		movieDAO.closeConnection();
		
		System.out.println("--------Finished Processing----------");
	}
	
	public static boolean updateMovieNamesFromRootDir(String srcDirectory) {

		Path srcDir = Paths.get(srcDirectory);
		SimpleFileWalk dirWalk = new SimpleFileWalk();

		System.out.println("Scanning the mentioned directory : "+srcDirectory + " for Movies, Please wait...");
		
		try {
			Files.walkFileTree(srcDir, dirWalk);
		} catch (IOException e) {
			System.out.println("walkFileTree exception : " + e.getMessage());
		}
		
		//need this ??? no 
		allMovieObjects = dirWalk.getAllMovieObjs();
		return allMovieObjects.isEmpty() ? false : true;
	}

	public static String getSrcDirectory() {
		return srcDirectory;
	}

	public static void setSrcDirectory(String srcDirectory) {
		MovieMon.srcDirectory = srcDirectory;
	}



}

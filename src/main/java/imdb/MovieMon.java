package imdb;

import imdb.database.dao.*;
import imdb.utils.MovieMonUtils;
import imdb.utils.ScanStatusEnum;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MovieMon {

	private static String srcDirectory;
	private static List<MovieObject> allMovieObjects;
	
	public static synchronized void process() {
		MovieMonUtils.setScanStatus(ScanStatusEnum.INPROGRES);
		if (false == updateMovieNamesFromRootDir(srcDirectory)) {
			System.out.println("Could not find any movies at path : "+srcDirectory);
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return;
		}
		//process dup movies here ?
		MovieMonDaoFactory.getMovieDAOImpl().updateDupMovies();
		MovieMonUtils.setScanStatus(ScanStatusEnum.SUCCESS);
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

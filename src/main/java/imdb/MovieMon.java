package imdb;

import imdb.database.dao.*;
import imdb.utils.MovieMonUtils;
import imdb.utils.ScanStatusEnum;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.log4j.*;

public class MovieMon {

	private static String srcDirectory;
	private static List<MovieObject> allMovieObjects;
	
	final static Logger log = Logger.getLogger(MovieMon.class);
	
/*	@Inject
	private MovieMonDaoFactory fact; */
	
	public synchronized static void process() {
		MovieMonUtils.setScanStatus(ScanStatusEnum.INPROGRES);

		if (false == updateMovieNamesFromRootDir(srcDirectory)) {
			log.error("Could not find any movies at path : "+srcDirectory);
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return;
		}
		//process dup movies here ?
		MovieMonUtils.setScanStatus(ScanStatusEnum.SUCCESS);
		MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
		movieDAO.updateDupMovies();
		movieDAO.closeConnection();
		
		log.debug("--------Finished Processing----------");
	}
	
	public static boolean updateMovieNamesFromRootDir(String srcDirectory) {

		Path srcDir = Paths.get(srcDirectory);
		SimpleFileWalk dirWalk = new SimpleFileWalk();

		log.debug("Scanning the mentioned directory : "+srcDirectory + " for Movies, Please wait...");
		
		try {
			Files.walkFileTree(srcDir, dirWalk);
		} catch (IOException e) {
			log.error("walkFileTree exception : " + e.getMessage());
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

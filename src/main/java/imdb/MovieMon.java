package imdb;

import imdb.database.dao.*;
import imdb.executer.*;
import imdb.utils.MovieMonUtils;
import imdb.utils.ScanStatusEnum;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.*;

public class MovieMon {

	private static String srcDirectory;
	private static List<MovieObject> allMovieObjects;
	
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
	
	final static Logger log = Logger.getLogger(MovieMon.class);
	
	public static int THREAD_COUNT = 2;
		
	public synchronized static void process() {
		
		MovieMonUtils.setScanStatus(ScanStatusEnum.INPROGRES);
		long startTime = System.currentTimeMillis();

		if (false == updateMovieNamesFromRootDir(srcDirectory)) {
			log.error("Could not find any movies at path : "+srcDirectory);
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return;
		}
		/*
		 * New change in call flow. After Filewalk we'll only have movieObjects with resolved names. Following things should happen afterwards
		 * Iterate over the objects. Each Thread should do
		 * -> Gsearch for Imdb Id [ Currently this is been done during name resolving. Need to change this ]
		 * -> omdb query for movie details. 
		 * -> insertion into movie table, user_movies, recently_added_movies
		 * -> thumbnail download
		 * 
		 * Other items to do.. 
		 * ---> to avoid processing the same movie in case of multiple runs, query db for filelocation.. SELECT FileLocation FROM MOVIE
		 * ---> If the movie that we are going to process is present in this list, Skip.. By this only newly added movies will be
		 * ---> processed.
		 */
		
		ExecutorService eService = Executors.newFixedThreadPool(THREAD_COUNT);
		
		//TODO: getscannedfilelist should take currently logged in user as input.. 
		//need to change this function.
		List<String> scannedFileList = movieDAO.getScannedFileList();
		List<MovieProcessor> movieProcessors = new ArrayList<MovieProcessor>();
		
		for (MovieObject movieObj : allMovieObjects) {
			
			
			if (scannedFileList.contains(movieObj.getMovieAbsPath())) {
				log.info("Skipping file as it is already present in DB" + movieObj.getMovieAbsPath());
				// remove from scannedFileLIst this item ?
				continue;
			}
			
			movieProcessors.add(new MovieProcessor(movieObj));

		}
		try {
			
			List<Future<Boolean>> futureResultList = eService.invokeAll(movieProcessors);
			
			for(Future<Boolean> fut : futureResultList) {
				if(fut.get() == false) {
					//can have some count or something//
					//log.error("ERROR while processing the movie : " + eCount);
				}
				else {
					//log.error("ERROR while processing the movie : " + Count);
				}
					
			}
				
		} catch (InterruptedException | ExecutionException e) {
			log.error("Exception : " +e.getMessage());
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
		} 
		
			
		MovieMonUtils.setScanStatus(ScanStatusEnum.SUCCESS);
		
		//TODO : Adding a new thread to process failed movies :
		
		Runnable failedmovieProcessorThread = () -> {
		};
		
		//TODO:updateDupMoviesForTheUser() 
		movieDAO.updateDupMovies();
		movieDAO.closeConnection();
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		/*for(Map.Entry<String, Integer> entry : MovieNameResolver.gibberishMap.entrySet()) {
			log.debug(">>>>> key : " + entry.getKey() +" : " + entry.getValue());
		}*/
		
		
		log.debug("--------Finished Processing----------");
		log.debug("Elapsed time ... >>>>>>> : " + totalTime  +" >>>> sec : " +totalTime/1000);
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

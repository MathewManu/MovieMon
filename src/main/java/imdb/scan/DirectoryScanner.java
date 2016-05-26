package imdb.scan;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.log4j.*;

import imdb.*;


public class DirectoryScanner {

	final static Logger log = Logger.getLogger(DirectoryScanner.class);

	public List<MovieObject> getMovieObjList(String srcDirectory) {

		SimpleFileWalk dirWalk = new SimpleFileWalk();
		Path srcDir = Paths.get(srcDirectory);

		log.debug("Scanning the mentioned directory : " + srcDirectory + " for Movies, Please wait...");

		try {
			Files.walkFileTree(srcDir, dirWalk);
		} catch (IOException e) {
			log.error("walkFileTree exception : " + e.getMessage());
		}

		return dirWalk.getAllMovieObjs();

	}

}

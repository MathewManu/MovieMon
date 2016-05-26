package imdb.scan;

import imdb.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

import org.apache.log4j.*;

public class SimpleFileWalk extends SimpleFileVisitor<Path>{
	
	final static Logger log = Logger.getLogger(SimpleFileWalk.class);
	
	private List<MovieObject> allMovieObjs = new ArrayList<MovieObject>();
	private MovieNameResolver nameResolver = new MovieNameResolver();
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {

		String fileName = file.getFileName().toString();
		
		if (attr.isRegularFile()) {

			long fsize = attr.size()/(1024*1024); // size in MB
			int lastIndexOfDot = fileName.lastIndexOf("."); // filename must have extension
			String fileExt = fileName.substring(lastIndexOfDot + 1);

			// assumes min of 200MB size for a movie. Helps to skip sample files.
			
			if (fsize <= 200 || lastIndexOfDot == -1 || !MovieFormat.isValidFormat(fileExt)) {
				log.debug("====Skiping the file : " + fileName);
				return FileVisitResult.CONTINUE;
			}
		
			MovieObject movieObj = new MovieObject(fileName, file.toString());

			if (nameResolver.process(movieObj)) {
				
				allMovieObjs.add(movieObj);
				log.info("Added movie to after name process : " + fileName );
			} 

		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		return FileVisitResult.CONTINUE;
	}

	public List<MovieObject> getAllMovieObjs() {
		return allMovieObjs;
	}


}

package imdb;

import imdb.database.dao.*;
import imdb.database.model.*;
import imdb.utils.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

import org.apache.log4j.*;

public class SimpleFileWalk extends SimpleFileVisitor<Path>{
	
	final static Logger log = Logger.getLogger(SimpleFileWalk.class);
	
	private Set<String> uniqueFilesTest = new HashSet<String>();
	
	private List<MovieObject> allMovieObjs = new ArrayList<MovieObject>();
	private List<MovieObject> failedMovieObjs = new ArrayList<MovieObject>();
	
	private MovieNameResolver nameResolver = new MovieNameResolver();
	private static BaseApiConnector apiConnector = new OmdbApiConnector();
	
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();

	public Set<String> getUniqueFiles() {
		return uniqueFilesTest;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
			throws IOException {

		String fileName = file.getFileName().toString();

		if (attr.isRegularFile()) {

			int lastIndexOfDot = fileName.lastIndexOf(".");
			if (lastIndexOfDot == -1
					|| !MovieFormat.isValidFormat(fileName.substring(lastIndexOfDot + 1))) {
				log.debug("====Skiping the file : " + fileName);
				return FileVisitResult.CONTINUE;

			}
		
			MovieObject movieObj = new MovieObject(fileName, file.toString());

			if (nameResolver.process(movieObj)) {
				
				// now we can query omdb for the movie object.
				apiConnector.updateMovieObjectsWithApiData(movieObj);

				// download thumbnail for the movie
				// Found that for some movies "NA" is present as thumbnail
				// address
				String posterLoc = movieObj.getMovieObjFromApi().getPoster();

				if (posterLoc != null && posterLoc != "NA") {
					MovieMonUtils.downloadPoster(movieObj.getMovieObjFromApi().getTitle(),
							movieObj.getMovieObjFromApi().getPoster());

				}

				// update to hsql db --> shold change to proper place !
				if (movieDAO.insert(getMovieDto(movieObj))) {
					log.debug("Insert success for movie : " + movieObj.getMovieName() +" -- Path : "+movieObj.getMovieAbsPath());
					
				} else {
					log.error("ERROR: insert error");
				}

				// TODO:should remove this add
				allMovieObjs.add(movieObj);

				System.out.println("===End===");
				log.debug("===End===\n\n");

			} else {
				// If we failed to get details of a movie online
				// add to the failed_movie table
				// Need to process later / display to the user
				movieDAO.insertFailedMovie(file.toString());
				failedMovieObjs.add(movieObj); // need to remove this line !
				log.debug("===End==\n\n");
			}

		}

		return FileVisitResult.CONTINUE;
	}

	private MovieDBResult getMovieDto(MovieObject movieObj) {
		MovieDBResult movieDto = new MovieDBResult();
		
		movieDto.setFileName(movieObj.getMovieName());
		movieDto.setMovieAbsPath(movieObj.getMovieAbsPath());
		
		movieDto.setTitle(movieObj.getMovieObjFromApi().getTitle());
		movieDto.setImdbID(movieObj.getMovieObjFromApi().getImdbID());
		movieDto.setYear(movieObj.getMovieObjFromApi().getYear());
		movieDto.setGenre(movieObj.getMovieObjFromApi().getGenre());
		movieDto.setImdbRating(movieObj.getMovieObjFromApi().getImdbRating());
		
		return movieDto;
		
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		// System.out.format("preVisit directory ---- %s%n", dir);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		// System.out.format("Directory: %s%n", dir);
		return FileVisitResult.CONTINUE;
	}

	public List<MovieObject> getAllMovieObjs() {
		return allMovieObjs;
	}


}

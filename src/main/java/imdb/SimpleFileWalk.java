package imdb;

import imdb.database.dao.*;
import imdb.database.model.*;
import imdb.utils.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class SimpleFileWalk extends SimpleFileVisitor<Path>{
	
	private Set<String> uniqueFilesTest = new HashSet<String>();
	private Set<String> possibleDuplicates = new HashSet<String>();
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
				System.out.println("====Skiping the file : " + fileName);
				return FileVisitResult.CONTINUE;

			}
			//TODO: this duplicate thing should be changed !
			if (false == uniqueFilesTest.add(fileName)) {
				possibleDuplicates.add(fileName);
				System.out.println("duplicate file ..." + fileName);
			} else {

				MovieObject movieObj = new MovieObject(fileName, file.toString());

				if (nameResolver.process(movieObj)) {
					//System.out.println("------true-----");

					// now we can query omdb for the movie object.
					apiConnector.updateMovieObjectsWithApiData(movieObj);
					
					// download thumbnail for the movie
					MovieMonUtils.downloadPoster(movieObj.getMovieObjFromApi().getTitle(), 
							movieObj.getMovieObjFromApi().getPoster());
					
					//update to hsql db --> shold change to proper place !
					if(movieDAO.insert(getMovieDto(movieObj))) {
						System.out.println("Insert success for movie : "+movieObj.getMovieName());
					}
					else {
						System.out.println("ERROR: insert error");
					}
					
					// TODO:should remove this add
					allMovieObjs.add(movieObj);
				
					System.out.println("===End===");

				}
				else {
					failedMovieObjs.add(movieObj);
					System.out.println("===End===");
				}

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

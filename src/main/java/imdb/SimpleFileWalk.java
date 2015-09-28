package imdb;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class SimpleFileWalk extends SimpleFileVisitor<Path>{
	
	private Set<String> uniqueFilesTest = new HashSet<String>();
	private Set<String> possibleDuplicates = new HashSet<String>();
	private TextProcessingUtils textProcessor = new TextProcessingUtils();
	private List<MovieObject> allMovieObjs = new ArrayList<MovieObject>();
	
	//private DirectoryOperations dirUtils = new DirectoryOperations();
	private MovieNameResolver nameResolver = new MovieNameResolver();
	private static BaseApiConnector apiConnector = new OmdbApiConnector();

	public Set<String> getUniqueFiles() {
		return uniqueFilesTest;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr)
			throws IOException {

		String fileName = file.getFileName().toString();
		
		if (attr.isRegularFile()) {
			//this has to be separated out, 
			//need to verify that file is a movie file 
			//minSize ..
			
			// else case does the logic, refactor later//
			
			if( false == uniqueFilesTest.add(fileName)) {
				possibleDuplicates.add(fileName);
				System.out.println("duplicate file ..." +fileName);
			} else {
				
				MovieObject movieObj = new MovieObject(fileName, file.toString());
				
				if (nameResolver.process(movieObj)) {
					System.out.println("------true-----");
					
					// now we can query omdb for the movie object.
					apiConnector.updateMovieObjectsWithApiData(movieObj);
					System.out.println("===End===");

				}
				
				//TODO:should remove this add
				allMovieObjs.add(movieObj);
			}
			
			//add a fileProcessor class which does all these directory creation link creation/copy etc.
			Path newDirPath = textProcessor.getNewDirectoryPath(fileName);
	
			//		dirUtils.createDirectory(newDirPath);
			//dirUtils.copyFile(file, newDirPath);
			//Files.copy(file, newDirPath.resolve(file.getFileName()));

		}
		return FileVisitResult.CONTINUE;
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

	public void setAllMovieObjs(List<MovieObject> allMovieObjs) {
		this.allMovieObjs = allMovieObjs;
	}

}

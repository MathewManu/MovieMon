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
	
	private DirectoryOperations dirUtils = new DirectoryOperations();

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
			//Directory creation should happen after 
			//removing all unnecessary keywords, ie, after name processing
			
			if( false == uniqueFilesTest.add(fileName)) {
				possibleDuplicates.add(fileName);
				System.out.println("duplicate file ..." +fileName);
			} else {
				//System.out.println(fileName); // what to do incase of duplicate files ?
				MovieObject movieObj = new MovieObject(fileName, file.toString());
				allMovieObjs.add(movieObj);
			}
			
			//add a fileProcessor class which does all these directory creation link creation/copy etc.
			Path newDirPath = textProcessor.getNewDirectoryPath(fileName);
	
			//		dirUtils.createDirectory(newDirPath);
			//dirUtils.copyFile(file, newDirPath);
		//	dirUtils.createLinks(newDirPath ,file);
			//dirUtils.createLink(file, newDirPath);
			//Files.createDirectory(newDirPath);
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

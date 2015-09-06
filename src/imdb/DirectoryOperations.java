package imdb;

import java.io.*;
import java.nio.file.*;

public class DirectoryOperations {

	public void createDirectory(Path newDirPath) {
		try {
			Files.createDirectory(newDirPath);
		} catch (IOException e) {
			System.out.println("CreateDirectory failed :  " + e.getMessage());
		}

	}

	public void copyFile(Path file, Path newDirPath) {
		try {
			Files.copy(file, newDirPath.resolve(file.getFileName()));
		} catch (IOException e) {

			System.out.println("Copy file has failed :  " + e.getMessage());
		}

	}

	public void createLink(Path file, Path newDirPath) {

	}

	public void createLinks(Path newDirPath, Path target) {
		Path link = Paths.get("C://imdb_output//testfile.jpg");
		try {
			Files.createLink(link, target);
		} catch (IOException e) {
			System.out.println("Link creation has failed :  " + e.getMessage());
		}

	}

}

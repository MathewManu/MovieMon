package imdb.utils;

import java.io.*;
import java.nio.file.*;

public class MovieMonUtils {

	public static <E extends Enum<E>> boolean checkIfValuePresent(Class<E> enumClass, String format) {
		
		for(E e : enumClass.getEnumConstants()) {
			if(e.name().equalsIgnoreCase(format)) { return true; }
		}
		return false;
	}
	public static void createDirectory(Path newDirPath) {
		try {
			Files.createDirectory(newDirPath);
		} catch (IOException e) {
			System.out.println("CreateDirectory failed :  " + e.getMessage());
		}

	}

	public static void copyFile(Path file, Path newDirPath) {
		try {
			Files.copy(file, newDirPath.resolve(file.getFileName()));
		} catch (IOException e) {

			System.out.println("Copy file has failed :  " + e.getMessage());
		}

	}
	

}

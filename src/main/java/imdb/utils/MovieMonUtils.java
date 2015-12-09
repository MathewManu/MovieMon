package imdb.utils;

import java.io.*;
import java.net.*;
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
	public static void downloadPoster(String movieTitle, String posterUrl) throws IOException {
		
		/*
		 * append posterUrl extention (jpg) with movieTitle to form the thumbnail name
		 * gladiator+"."+"jpg"
		 */
		try {
			Files.createDirectories(Paths.get(PropertyFileParser.IMAGES_FOLDER)) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String outputFileName = PropertyFileParser.IMAGES_FOLDER + "/" + movieTitle+ "." + posterUrl.substring(posterUrl.lastIndexOf(".")+1);
		
		URL url = new URL(posterUrl);
		
		try (InputStream is = url.openStream();
				OutputStream os = new FileOutputStream(outputFileName);) {
			
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			System.out.println("Downloaded Thumbnail for movie : "+movieTitle );
		}
		catch(IOException e) {
			System.out.println("Exception while downloading Thumbnail for movie : "+movieTitle +" Exception : "+e.getMessage());
		}
	}
	

}

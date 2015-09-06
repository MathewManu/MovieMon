package imdb;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TextProcessingUtils {

	public Path getNewDirectoryPath(String fileName) {
		
		String sampleDirPath = "C:\\manu\\imdb_output\\";
		
		String fileNameWithOutExt = getFileNameWithOutExt(fileName);
		Path newDirPath = Paths.get(sampleDirPath.concat(fileNameWithOutExt));
		return newDirPath;
		
	}
	
	// testfun to get filename
	private String getFileNameWithOutExt(String fileName) {
		String arr[] = fileName.split("\\.");
		return arr[0];

	}
	
}

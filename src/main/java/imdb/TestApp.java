package imdb;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestApp {

	public static void main(String[] args) throws IOException {
	//	DirectoryUtils utilObj = new DirectoryUtils("C:\\manu");
		//List<String> fileList = utilObj.getFileList();
		try {
			Path startingDir = Paths.get("C:\\manu\\testDir");
			SimpleFileWalk pf = new SimpleFileWalk();
			
			Files.walkFileTree(startingDir, pf);
			Set<String> filesA = pf.getUniqueFiles();
			
			for (String string : filesA) {
				System.out.println(string);
			}
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

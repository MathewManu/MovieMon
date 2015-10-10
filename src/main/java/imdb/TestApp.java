package imdb;

import imdb.install.InstallMovieMon;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestApp {

	public static void main(String[] args) throws IOException {
		
		if (args.length != 0 && args[0].equals("--install")) {
			InstallMovieMon.install();
			System.exit(0);
		}
		try {
			
				//init() // does all properties file reading & startup work
				// reading the directory src & destination (if user selects). ShortCut
				// which site to use for getting the data
				// etc..
				// if i call init here and set the data, can i get it in some other class ?
				// call movimon.process() from here,, and hard code the value as of now.

				MovieMon.process();
			/*	Path startingDir = Paths.get("C:\\manu\\testDir");
				SimpleFileWalk pf = new SimpleFileWalk();
				
				Files.walkFileTree(startingDir, pf);
				Set<String> filesA = pf.getUniqueFiles();
				
				for (String string : filesA) {
					System.out.println(string);
				}*/
				
				//json from omdb testing..
				//OmdbApiConnector omdb = new OmdbApiConnector();
				//omdb.testConn();
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

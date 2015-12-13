package imdb;

import imdb.install.InstallMovieMon;
import imdb.utils.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestApp {
	
	
	public static void main(String[] args) throws IOException {

		if(args.length == 0) {
			System.out.println("==========use the following options to run================");
			System.out.println("--install              -      Install movieMon");
			System.out.println("\"<MovieLocation>\"    -      to scan the specified loc");
			//need to make sure that install is run first !
			//currently script doesn't check if directories exists or not. need to correct this.
			//while testing from eclipse use arguments along with run 
			System.exit(0);
		}
		if (args[0].equals("--install")) {
			InstallMovieMon.install();
			System.out.println("Installation finished");
			System.exit(0);
		}
		else {		
			
			try {
				new PropertyFileParser().load();
				MovieMon.setSrcDirectory(args[0]);
				MovieMon.process();

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
	
		}
	}
}

package imdb;

import imdb.install.InstallMovieMon;
import imdb.utils.*;

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
			new PropertyFileParser().load();	
			MovieMon.process();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}

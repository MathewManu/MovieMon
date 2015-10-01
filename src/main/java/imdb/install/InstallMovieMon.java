package imdb.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import imdb.database.dao.MovieMonDAO;
import imdb.utils.PropertyFileParser;

/**
 * Installing our application into the folder specified in config.properties
 * @author anushya
 *
 */
public class InstallMovieMon {

	public static void install() {
		System.out.println("Loading properties.....");
		//initialize config.properties.
		new PropertyFileParser().load();		

		//TODO : copy the files into bin and lib folders respectively 
		//-----------------------FileUtils.copyDirectory(new File(Prop), destDir);
		//create db tables;
		//TODO : need to add more tables in tables.sql file.
		System.out.println("Creating db tables....");
		ScriptRunner runner = new ScriptRunner(new MovieMonDAO().createConnection());
		try (InputStreamReader reader = new InputStreamReader(InstallMovieMon.class.getResourceAsStream("/tables.sql"))) {
			runner.runScript(reader);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			runner.closeConnection();
		}


	}
}
package imdb.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import imdb.database.dao.*;
import imdb.utils.PropertyFileParser;

/**
 * Installing our application into the folder specified in config.properties
 * @author anushya
 *
 */
public class InstallMovieMon {

	public static void install() {
		
		System.out.println("Loading properties.....");
		new PropertyFileParser().load();		

		System.out.println("Creating db tables....");
		MovieDAOImpl DaoImpl = new MovieDAOImpl();
		ScriptRunner runner = new ScriptRunner(DaoImpl.createConnection());
		try (InputStreamReader reader = new InputStreamReader(InstallMovieMon.class.getResourceAsStream("/tables.sql"))) {
			runner.runScript(reader);
		} catch (Exception e) {
			System.out.println("Error while running the script : " +e.getMessage());
		} finally {
			DaoImpl.closeConnection();
			runner.closeConnection();
			
		}


	}
}

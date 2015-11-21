package imdb.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.*;
import java.util.Properties;

/**
 * The configuration file config.properties is parsed during server/application startup.
 * The values are populated as static variables.
 * @author anushya
 *
 */
public class PropertyFileParser {

	public static String INSTALL_LOCATION_WINDOWS = "C://Program Files//MovieMon"; // In case properties file is corrupted, this will be the default location
	public static String THUMB_NAIL_DIR = "C://Program Files//MovieMon//Thumbnails//";
	public static String HSQL_DB_DRIVER = "org.hsqldb.jdbcDriver";
	public static String HSQL_DB_URL = "jdbc:hsqldb:file:";
	public static String HSQL_DB_FILE_LOCATION = "C://Program Files//MovieMon//db//MovieMonDb";

	private static String CONFIG_FILE = "/config.properties";

	Properties defaultProps = null;

	public  static void main(String args[]) {
		PropertyFileParser par = new PropertyFileParser();
		par.load();
		par.parseConfigFile();
	}

	/**
	 * Load method is the entry point for initializing static variables with
	 * values in properties file. 
	 * This method needs to be called during install/start of the service or application
	 * @param propertyName
	 */
	public void load() {
		
		if (defaultProps == null) {			
			parseConfigFile();
			//get the properties and load it into the static variables.
			INSTALL_LOCATION_WINDOWS = defaultProps.getProperty("install.directory");
			HSQL_DB_DRIVER = defaultProps.getProperty("hsql.db.driver");
			HSQL_DB_URL = defaultProps.getProperty("hsql.db.url");
			HSQL_DB_FILE_LOCATION = defaultProps.getProperty("hsql.db.file");
			THUMB_NAIL_DIR = defaultProps.getProperty("thumbnail.directory");
		}		
		
		//print the variables --> log.debug msgs
		System.out.println("install.directory : " + INSTALL_LOCATION_WINDOWS);
		System.out.println("hsql.db.driver : " + HSQL_DB_DRIVER);
		System.out.println("hsql.db.url : " + HSQL_DB_URL);
		System.out.println("hsql.db.file : " + HSQL_DB_FILE_LOCATION);

	}

	/**
	 * Method responsible for loading the properties file.
	 * @return
	 */
	public boolean parseConfigFile() {		
		
		try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE)) {
			defaultProps = new Properties();
			defaultProps.load(in);			
		} catch (Exception e) {
			System.out.println("Exception while reading properties file : " + e.getLocalizedMessage());
			return false;
		}
		return true;
	}
}

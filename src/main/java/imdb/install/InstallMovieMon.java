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
import imdb.utils.DBUtils;
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
		DBUtils.executeMysqlScript("tables.sql");


	}
}

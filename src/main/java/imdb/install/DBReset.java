package imdb.install;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import imdb.utils.DBUtils;
import imdb.utils.PropertyFileParser;

public class DBReset {

	public static void main(String args[]) {
		deleteAndConstructDb();		
	}

	public static void deleteAndConstructDb() {
		DBUtils.executeMysqlScript("Dbreset.sql");
		try {
			FileUtils.cleanDirectory(new File(PropertyFileParser.THUMB_NAIL_DIR));
		} catch (IOException e) {
			System.out.println("Exception while deleting thumbnails : " + e.getMessage());
		}
		DBUtils.executeMysqlScript("tables.sql");
	}
	
	
}

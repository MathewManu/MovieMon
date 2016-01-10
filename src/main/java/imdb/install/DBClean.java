package imdb.install;

import imdb.utils.DBUtils;
import imdb.utils.PropertyFileParser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DBClean {

	public static void main(String args[]) {
		cleanDBAndMetaData();		
	}

	public static void cleanDBAndMetaData() {
		DBUtils.executeMysqlScript("DbClean.sql");
		try {
			FileUtils.cleanDirectory(new File(PropertyFileParser.THUMB_NAIL_DIR));
		} catch (IOException e) {
			System.out.println("Exception while deleting thumbnails : " + e.getMessage());
		}
		
	}
}

package imdb.install;

import imdb.utils.DBUtils;

public class DBReset {

	public static void main(String args[]) {
		DBUtils.executeMysqlScript("Dbreset.sql");
	}
}

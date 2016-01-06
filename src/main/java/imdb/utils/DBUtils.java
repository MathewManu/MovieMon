package imdb.utils;

import imdb.database.dao.MovieDAOImpl;
import imdb.install.InstallMovieMon;

import java.io.InputStreamReader;

import org.apache.ibatis.jdbc.ScriptRunner;

public class DBUtils {

	public static void executeMysqlScript(String sqlScriptName) {
		MovieDAOImpl DaoImpl = new MovieDAOImpl();
		ScriptRunner runner = new ScriptRunner(DaoImpl.createConnection());
		try (InputStreamReader reader = new InputStreamReader(InstallMovieMon.class.getResourceAsStream("/" + sqlScriptName))) {
			runner.runScript(reader);
		} catch (Exception e) {
			System.out.println("Error while running the script : " +e.getMessage());
		} finally {
			DaoImpl.closeConnection();
			runner.closeConnection();
			
		}
	}
}

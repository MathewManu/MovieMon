package imdb.rest;

import imdb.MovieMon;
import imdb.database.model.MovieDBResult;
import imdb.utils.MovieMonUtils;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Path("/movies")
public class RestController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> getMovies(
			@DefaultValue("") @QueryParam("rating") String rating, 
			@DefaultValue("") @QueryParam("year") String year ) {
		return RestRequestProcessor.getMovies(rating, year);
	}

	@GET
	@Path("{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> searchMovie(@PathParam("searchQuery") String searchQuery) {
		return RestRequestProcessor.searchMovie(searchQuery);
	}

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public  String doScan(final String requString) {		

		if (requString == null ||  requString.isEmpty()) {
			return MovieMonUtils.GetLocalizedString("noinputdirectoryname");
		}
		
		File file = new File(requString);
		if (! file.exists() || ! file.isDirectory()) {
			return MovieMonUtils.GetLocalizedString("invaiddirectory");
		}
		
		if (file.list().length == 0){			
			return MovieMonUtils.GetLocalizedString("emptydirectory");				
		}
		
			new Thread( () -> {
				MovieMon.setSrcDirectory(requString);
				MovieMon.process();
			}).start(); 

		return MovieMonUtils.GetLocalizedString("scaninprogress");

	}
}

package imdb.rest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import imdb.database.dao.*;
import imdb.database.model.*;


@Path("/movies")
public class RestController {
	/*	@GET
	@Produces("text/plain")
	public String getMessage() {
		return "Rest Never Sleeps";
	}*/
	
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
	

}

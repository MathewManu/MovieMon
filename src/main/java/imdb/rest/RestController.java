package imdb.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import imdb.database.dao.*;
import imdb.database.model.*;


@Path("/Movies")
public class RestController {
	private static MovieDAOImpl movieDAO = MovieMonDaoFactory.getMovieDAOImpl();
	
/*	@GET
	@Produces("text/plain")
	public String getMessage() {
		return "Rest Never Sleeps";
	}*/
	
	@GET
	//@Produces( {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML} )
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> getAll() {
		return movieDAO.getAll();
	}
	
	@GET @Path("{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> search(@PathParam("searchQuery") String searchQuery) {
		return movieDAO.search(searchQuery);
	}
	

}

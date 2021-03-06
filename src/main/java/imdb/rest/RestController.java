package imdb.rest;

import imdb.constants.MovieSortParams;
import imdb.MovieMon;
import imdb.auth.*;
import imdb.database.model.MovieDBResult;
import imdb.install.DBReset;
import imdb.rest.favorites.*;
import imdb.utils.MovieMonUtils;
import imdb.utils.ScanStatusEnum;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;

import org.glassfish.jersey.media.sse.*;


@Path("/movies")
public class RestController {

	final static Logger logger = Logger.getLogger(RestController.class);
	private MovieSorter movieSorter = new MovieSorter();
	private FavoriteManager FavoriteManager = new FavoriteManager();
	private WatchListManager watchListManager = new WatchListManager();
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> getMovies(@DefaultValue("") @QueryParam("rating") String rating,
			@DefaultValue("") @QueryParam("year") String year, @DefaultValue("") @QueryParam("genre") String genre,
			@Context SecurityContext securityContext) {

		Principal principal = securityContext.getUserPrincipal();
		String username = principal.getName();
		logger.debug(">>>>>>>> Username is ... " +username);
		
		return RestRequestProcessor.getMovies(rating, year, genre);
	}

	@GET
	@Path("{searchQuery}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MovieDBResult> searchMovie(@PathParam("searchQuery") String searchQuery) {
		return RestRequestProcessor.searchMovie(searchQuery);
	}

	@Secured
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public  String doScan(final String requString) {		

		if (requString == null ||  requString.isEmpty()) {
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return MovieMonUtils.GetLocalizedString("noinputdirectoryname");
		}

		File file = new File(requString);
		if (! file.exists() || ! file.isDirectory()) {
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return MovieMonUtils.GetLocalizedString("invaiddirectory");
		}

		if (file.list().length == 0) {
			MovieMonUtils.setScanStatus(ScanStatusEnum.FAILED);
			return MovieMonUtils.GetLocalizedString("emptydirectory");				
		}
		
		MovieMonUtils.setScanStatus(ScanStatusEnum.INPROGRES);
		new Thread( () -> {
			MovieMon.setSrcDirectory(requString);
			MovieMon.process();
		}).start(); 
		
		return MovieMonUtils.GetLocalizedString("scaninprogress");


	}


	/**
	 * This method was added to support SSE events from UI. Somehow , this doesnt seem to work once I enabled Async-supported tag in web.xml.
	 * refer to : http://stackoverflow.com/questions/34248928/implementing-sse-using-jersey-not-working-after-setting-async-supported-to-true
	 * @return
	 */
	@GET
	@Path("scanstatus")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput getScanStatus() {

		final EventOutput output = new EventOutput();

		//TODO :Anushya Better to associate the message as part of enum only.
		String message = MovieMonUtils.getScanStatus() == ScanStatusEnum.SUCCESS ? ScanStatusEnum.SUCCESS.name() : MovieMonUtils.GetLocalizedString("scaninprogress");
		try {
			output.write(new OutboundEvent.Builder().name("custom-message").data(String.class, message).build());
		} catch (IOException e) {
			try {
				output.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}		
		return output;
	}
	
	
	@GET
	@Path("scan/status")
	@Produces(MediaType.TEXT_PLAIN)
	public String getScanStatusForPolling() {
		return MovieMonUtils.getScanStatus() == null ? ScanStatusEnum.INPROGRES.name() : MovieMonUtils.getScanStatus().name();
		
	}
	
	@Secured
	@DELETE
	public void deleteAllMovies() {
		DBReset.deleteAndConstructDb();
	}
	
	//sorting
		@GET
		@Path("sortedmovies")
		@Produces(MediaType.APPLICATION_JSON)
		public List<MovieDBResult> sortMoviesByName(@QueryParam("action") String action, @QueryParam("sortParam") String sortParam, @QueryParam("sortType") String ascOrDesc ) {
			
			logger.info(String.format("Query Parameters : action = %s, sortParam = %s, sortType = %s ", action, sortParam, ascOrDesc));
			List<MovieDBResult> results = new ArrayList<MovieDBResult>();
			
			// cannot think of any other actions other than sort! Will move sort into an enum if and when required!
			if (action.equals("sort")) {
				if (sortParam.equals(MovieSortParams.NAME.getStringValue())) {
					results =  movieSorter.sortMovieByName(ascOrDesc);
				} else if (sortParam.equals(MovieSortParams.RATINGS.getStringValue())) {		
					results =  movieSorter.sortMovieByRating(ascOrDesc);
				} 
			}
			return results;
		}
		/*
		 * Favorites section 
		 */
		@Secured
		@POST
		@Path("favorites/{id}")
		public Response addFavorites(@PathParam("id") String id, @Context SecurityContext securityContext) {
			if (FavoriteManager.addFavorite(Integer.parseInt(id), securityContext.getUserPrincipal().getName())) {
				return  Response.status(Response.Status.OK).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		@Secured
		@DELETE
		@Path("favorites/{id}")
		public Response deleteFavorites(@PathParam("id") String id, @Context SecurityContext securityContext) {
			if (FavoriteManager.deleteFavorite(Integer.parseInt(id), securityContext.getUserPrincipal().getName())) {
				return  Response.status(Response.Status.OK).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		@Secured
		@GET
		@Path("favorites")
		@Produces(MediaType.APPLICATION_JSON)
		public List<MovieDBResult> getAllFavorites(@Context SecurityContext securityContext) {
			return FavoriteManager.getAllFavorites(securityContext.getUserPrincipal().getName());
		}
		
		/*
		 * watchlist section
		 */
		@Secured
		@POST
		@Path("watchlist/{id}")
		public Response addToWatchList(@PathParam("id") String id, @Context SecurityContext securityContext) {
			if (watchListManager.addToWatchList(Integer.parseInt(id), securityContext.getUserPrincipal().getName())) {
				return  Response.status(Response.Status.OK).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		@Secured
		@DELETE
		@Path("watchlist/{id}")
		public Response deleteFromWatchList(@PathParam("id") String id, @Context SecurityContext securityContext) {
			if (watchListManager.deleteFromWatchList(Integer.parseInt(id), securityContext.getUserPrincipal().getName())) {
				return  Response.status(Response.Status.OK).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		@Secured
		@GET
		@Path("watchlist")
		@Produces(MediaType.APPLICATION_JSON)
		public List<MovieDBResult> getWatchListForTheUser(@Context SecurityContext securityContext) {
			return watchListManager.getWatchList(securityContext.getUserPrincipal().getName());
		}
		
}
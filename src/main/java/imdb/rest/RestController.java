package imdb.rest;

import imdb.MovieMon;
import imdb.database.model.MovieDBResult;
import imdb.install.DBReset;
import imdb.utils.DBUtils;
import imdb.utils.MovieMonUtils;
import imdb.utils.ScanStatusEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.annotations.Delete;
import org.glassfish.jersey.media.sse.*;


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
	
	@DELETE
	public void deleteAllMovies() {
		DBReset.cleanDBAndMetaData();
	}
	

}


   

	
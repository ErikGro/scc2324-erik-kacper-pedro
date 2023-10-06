package scc.srv;

import scc.utils.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.MediaType;

/**
 * Resource for managing media files, such as images.
 */ 
@Path("/house")
public class HouseResource
{
	Map<String,byte[]> map = new HashMap<String,byte[]>();

	/**
	 * Post a new image.The id of the image is its hash.
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response upload(RegisterHouse data) {
    	throw new ServiceUnavailableException();
	}

	/**
	 * Return the contents of an image. Throw an appropriate error message if
	 * id does not exist.
	 */
	@POST
	@Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, RegisterHouse house) {
		
		throw new ServiceUnavailableException();
	}
    @DELETE
	@Path("/{id}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHouse(@PathParam("id") String id,) {
		throw new ServiceUnavailableException();
	}
	/**
	 * Lists the ids of images stored.
	 */
	@POST
	@Path("/{id}/rental/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRental(@PathParam("id") String id,RentalInformation rental) {
		throw new ServiceUnavailableException();
	}
    @POST
	@Path("/{id}/rental/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRental(@PathParam("id") String id,RentalInformation rental) {
		throw new ServiceUnavailableException();
	}
   
    @POST
	@Path("/{id}/question/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listRental(@PathParam("id") String id) {
		throw new ServiceUnavailableException();
	}
    //might have to do something different for questions since they might have to be sorted through id
    @POST
	@Path("/{id}/question/reply")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listRental(@PathParam("id") String id) {
		throw new ServiceUnavailableException();
	}



    @GET
	@Path("/{id}/rental/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listRental(@PathParam("id") String id) {
		throw new ServiceUnavailableException();
	}

   


    
}

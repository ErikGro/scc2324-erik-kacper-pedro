package scc.srv;

import com.azure.core.http.rest.Response;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import scc.data.Question;

/**
 * Class with control endpoints.
 * create question by id
 */

@Path("/rest/house/{houseId}/question")
public class QuestionResource {
    // @Path("/{id}/create")
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    
    // public Response createQuestion(@PathParam("houseId") String houseId, @PathParam("id") String id, Question data) {
    //     throw new ServiceUnavailableException();
    // }
}

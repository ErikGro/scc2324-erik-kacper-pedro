package scc.srv;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/*
 * Class to control endpoints.
 * create answer to the question with questionId 
 * only owner of the house can answer the question
 * 
 */

@Path("/rest/house/{houseId}/question/{questionId}/answer")
public class AnswerResource {
    /*
     * @Path("/{id}/create")
     * @POST
     * @Consumes(MediaType.APPLICATION_JSON)
     * @Produces(MediaType.APPLICATION_JSON)
     * 
     * public Response createAnswer(@PathParam("id") String id, Answer data) {
     *     throw new ServiceUnavailableException();
     * }
     */

}

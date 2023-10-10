package scc.srv;

import javax.ws.rs.core.Response;
import com.azure.cosmos.models.CosmosItemResponse;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import scc.data.Question;
import scc.data.QuestionDAO;
import scc.db.CosmosDBLayer;
import scc.db.QuestionDB;

/**
 * Class with control endpoints.
 * create question by id
 */

@Path("/rest/house/{houseId}/question")
public class QuestionResource {

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuestion(@PathParam("houseId") String houseId, Question q) {

        CosmosDBLayer dbLayer = CosmosDBLayer.getInstance();
        QuestionDB db = dbLayer.questionDB;
        
        String id = "q:" + System.currentTimeMillis();

        QuestionDAO qDAO = new QuestionDAO(id, houseId, q.getUserId(), q.getText(), q.getTimestamp());


        CosmosItemResponse<QuestionDAO> res = db.putQuestion(qDAO);
        q.setId(id);
        
        return Response.status(res.getStatusCode()).build();
    }

    @Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions(@PathParam("houseId") String houseId) {

        CosmosDBLayer dbLayer = CosmosDBLayer.getInstance();
        QuestionDB db = dbLayer.questionDB;

        // Get all questions from a house using getQuestions from QuestionDB
        return Response.ok(db.getQuestions(houseId).stream().map(QuestionDAO::toString)).build();
    }
    
}

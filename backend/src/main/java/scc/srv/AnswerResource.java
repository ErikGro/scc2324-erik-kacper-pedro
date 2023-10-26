package scc.srv;

import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.data.Answer;
import scc.data.AnswerDAO;
import scc.data.QuestionDAO;
import scc.data.house.HouseDAO;
import scc.db.AnswerDB;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;
import scc.db.QuestionDB;

import java.text.SimpleDateFormat;
import java.util.Optional;

import com.azure.cosmos.models.CosmosItemResponse;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/*
 * Class to control endpoints.
 * create answer to the question with questionId 
 * only owner of the house can answer the question
 * 
 */
@Path("/house/{houseId}/question/{questionId}/answer")
public class AnswerResource {
    private final HouseService houseService = new HouseService();

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAnswer(@PathParam("houseId") String houseId, @PathParam("questionId") String questionId, Answer ans) {


        CosmosDBLayer dbLayer = CosmosDBLayer.getInstance();

        QuestionDB qdb = dbLayer.questionDB;
        if (qdb.getByID(questionId).getItem() == null) {
            return Response.status(404, "Question doesn't exist.").build();
        }
        AnswerDB db = dbLayer.answerDB;


        // TODO: Add checking if user replying the question is the owner of the house
        // If not, return 403 Forbidden
        

        // Get house from db
        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }

        // Check if user is the owner of the house
        if (!house.get().getOwnerID().equals(ans.getUserId())) {
            return Response.status(403, "Only the onwer of the house can respond to the question.").build();
        }

        
        if (db.answerExists(questionId)) {
            return Response.status(409, "Answer already exists.").build();
        }

    
        String id = "a:" + System.currentTimeMillis();
        String ts = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date());
        
        AnswerDAO aDAO = new AnswerDAO(id, questionId, ans.getUserId(), ans.getText(), ts);

        CosmosItemResponse<AnswerDAO> res = db.upsert(aDAO);
        ans.setId(id);
        
        return Response.status(res.getStatusCode()).build();
    }

    // TODO: Add listing all answers with questions text for a given house

}

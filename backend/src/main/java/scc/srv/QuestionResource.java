package scc.srv;

import jakarta.ws.rs.core.Response;
import com.azure.cosmos.models.CosmosItemResponse;
import java.util.Iterator;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import scc.data.Question;
import scc.data.QuestionDAO;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;
import scc.db.QuestionDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Class with control endpoints.
 * create question by id
 */

@Path("/house/{houseId}/question")
public class QuestionResource {

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuestion(@PathParam("houseId") String houseId,  Question q) {

        CosmosDBLayer dbLayer = CosmosDBLayer.getInstance();
        HouseDB dbHouse = dbLayer.houseDB;
        
        // Get house from db
        if (!dbHouse.hasHouse(houseId)) {
            return Response.status(404, "House doesn't exist.").build();
        }
    
        QuestionDB db = dbLayer.questionDB;
        
        String id = "q:" + System.currentTimeMillis();
        String ts = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date());

        QuestionDAO qDAO = new QuestionDAO(id, houseId, q.getUserId(), q.getText(), ts);

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
        
        HouseDB dbHouse = dbLayer.houseDB;
        // Get house from db
        if (!dbHouse.hasHouse(houseId)) {
            return Response.status(404, "House doesn't exist.").build();
        }

        Iterator<QuestionDAO> res = db.getQuestions(houseId).iterator();
        // Get all questions from a house using getQuestions from QuestionDB
        List<String> questions = new ArrayList<String>();
        while (res.hasNext()) {
            QuestionDAO q = res.next();
            questions.add(q.toString());
        }
        
        return Response.ok(questions).build();
    }

    @Path("/{id}/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@PathParam("id") String id) {

        CosmosDBLayer dbLayer = CosmosDBLayer.getInstance();
        QuestionDB db = dbLayer.questionDB;
        
        Iterator<QuestionDAO> res = db.getQuestion(id).iterator();
        // Get all questions from a house using getQuestions from QuestionDB
        List<String> questions = new ArrayList<String>();
        while (res.hasNext()) {
            QuestionDAO q = res.next();
            questions.add(q.toString());
        }
        
        return Response.ok(questions).build();
    }
    
}

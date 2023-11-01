package scc.srv;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import scc.cache.HouseService;
import scc.cache.QuestionsService;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.Questions;
import scc.data.QuestionsDAO;
import scc.data.house.HouseDAO;



import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.UUID;

/**
 * Class with control endpoints.
 * create question by id
 */

@Path("/house/{houseId}/question")
public class QuestionResource {
    private final HouseService houseService = new HouseService();
    private final QuestionsService questionsService = new QuestionsService();
    private final UserService userService = new UserService();

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuestion(@PathParam("houseId") String houseId,  Questions q) {

        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }
        
        String id = UUID.randomUUID().toString();
        String ts = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date());

        QuestionsDAO qDAO = new QuestionsDAO(id, houseId, q.getUserId(), q.getText(), ts, "", "", "");

        ServiceResponse<QuestionsDAO> res = questionsService.upsert(qDAO);
        
        return Response.status(res.getStatusCode()).build();
    }

    @Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions(@PathParam("houseId") String houseId) {

        
        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }

        Iterator<QuestionsDAO> res = questionsService.getQuestions(houseId).getItem().get().iterator();
        // Get all questions from a house using getQuestions from QuestionDB
        List<String> questions = new ArrayList<>();
        while (res.hasNext()) {
            QuestionsDAO q = res.next();
            questions.add(q.toString());
        }
        
        return Response.ok(questions).build();
    }

    @Path("/{id}/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@PathParam("id") String id) {

        
        ServiceResponse<QuestionsDAO> res = questionsService.getByID(id);
        if (res.getStatusCode() < 300) {
            return Response.ok(res.getItem().get().toString()).build();
        } else {
            return Response.noContent().build();
        }
    }

    @Path("/{id}/")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(@CookieParam("scc:session") Cookie session,
                                   @PathParam("id") String id) {
        ServiceResponse<QuestionsDAO> questionResponse = questionsService.getByID(id);

        if (questionResponse.getItem().isEmpty())
            return Response.status(404).build();

        if (session == null || session.getValue() == null ||
                userService.userSessionInvalid(session.getValue(), questionResponse.getItem().get().getUserId()))
            return Response.status(401).build();

        ServiceResponse<QuestionsDAO> deleteResponse = questionsService.deleteByID(id);

        if (deleteResponse.getStatusCode() < 300) {
            return Response.ok().build();
        } else {
            return Response.noContent().build();
        }
    }
}

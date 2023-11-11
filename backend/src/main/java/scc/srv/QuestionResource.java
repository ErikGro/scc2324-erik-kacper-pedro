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

    /**
     * Crete a new question for a house
     * @param houseId of the house which is related to the question
     * @param questions question
     * @return 200 if successfully created
     */
    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQuestion(@PathParam("houseId") String houseId, Questions questions) {
        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }
        
        String id = UUID.randomUUID().toString();
        String ts = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date());

        QuestionsDAO qDAO = new QuestionsDAO(id, houseId, questions.getUserId(), questions.getText(), ts, "", "", "");

        ServiceResponse<QuestionsDAO> res = questionsService.upsert(qDAO);
        
        return Response.status(res.getStatusCode()).build();
    }

    /**
     * Get all questions for a house
     * @param houseId related to the questions
     * @return an array of all questions
     */
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

    /**
     * Get a specific question for a house
     * @param id of the questiom
     * @return the content of the question as json
     */
    @Path("/{id}/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@PathParam("id") String id) {

        
        ServiceResponse<QuestionsDAO> res = questionsService.getByID(id);
        if (res.getItem().isPresent()) {
            return Response.ok(res.getItem().get().toString()).build();
        } else {
            throw new NotFoundException("No question for the given ID found.");
        }
    }

    /**
     * Delete a question by id
     * @param session of the user
     * @param id of the question
     * @return 200 if successfully deleted
     */
    @Path("/{id}/")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(@CookieParam("scc:session") Cookie session,
                                   @PathParam("id") String id) {
        ServiceResponse<QuestionsDAO> questionResponse = questionsService.getByID(id);

        if (questionResponse.getItem().isEmpty())
            throw new NotFoundException("There exists no question for the given id.");

        if (session == null || session.getValue() == null ||
                userService.userSessionInvalid(session.getValue(), questionResponse.getItem().get().getUserId()))
            throw new NotAuthorizedException(id);

        ServiceResponse<QuestionsDAO> deleteResponse = questionsService.deleteByID(id);

        if (deleteResponse.getStatusCode() < 300) {
            return Response.ok().build();
        } else {
            return Response.status(deleteResponse.getStatusCode()).build();
        }
    }
}

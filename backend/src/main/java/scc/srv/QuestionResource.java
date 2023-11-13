package scc.srv;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.*;

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
    public Response createQuestion(@CookieParam("scc:session") Cookie session,
                                   @PathParam("houseId") String houseId,
                                   Questions questions) {
        if (session == null || session.getValue() == null)
            return Response.status(401).build();

        Optional<String> userID = userService.getUserIDBySession(session.getValue());

        if (userID.isEmpty())
            return Response.status(401).build();

        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }
        
        String id = UUID.randomUUID().toString();
        String ts = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date());
        QuestionsDAO questionDAO = new QuestionsDAO(id, houseId, userID.get(), questions.getText(), ts, "", "", "");

        ServiceResponse<QuestionsDAO> response = questionsService.upsert(questionDAO);
        if (response.getStatusCode() > 300)
            return Response.status(response.getStatusCode()).build();
        
        return Response.created(URI.create("/house/" + houseId + "/question/" + id)).build();
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

        Optional<Set<QuestionsDAO>> optionalQuestion = questionsService.getQuestions(houseId).getItem();

        if (optionalQuestion.isEmpty()) {
            return Response.status(404).build();
        }
        
        return Response.ok(optionalQuestion.get()).build();
    }

    /**
     * Get a specific question for a house
     * @param id of the question
     * @return the content of the question as json
     */
    @Path("/{id}")
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
    @Path("/{id}")
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

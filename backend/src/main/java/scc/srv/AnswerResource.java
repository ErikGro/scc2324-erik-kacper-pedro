package scc.srv;

import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.cache.QuestionsService;
import scc.cache.ServiceResponse;
import scc.data.Questions;
import scc.data.QuestionsDAO;
import scc.data.house.HouseDAO;

import java.text.SimpleDateFormat;
import java.util.Optional;


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
    private final QuestionsService questionsService = new QuestionsService();
    
    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAnswer(@PathParam("houseId") String houseId, @PathParam("questionId") String questionId, Questions ans) {


        Optional<QuestionsDAO> question = questionsService.getByID(questionId).getItem();
        
        if (question.isEmpty()) {
            return Response.status(404, "Question doesn't exist.").build();
        }

        Optional<HouseDAO> house = houseService.getByID(houseId).getItem();
        if (house.isEmpty()) {
            return Response.status(404, "House doesn't exist.").build();
        }

        // Check if user is the owner of the house
        if (!house.get().getOwnerID().equals(ans.getUserId())) {
            return Response.status(403, "Only the onwer of the house can respond to the question.").build();
        }

        if (!question.get().getAnswerText().isEmpty()) {
            return Response.status(409, "Answer already exists.").build();
        }


        question.get().setAnswerTimestamp(new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new java.util.Date()));
        question.get().setAnswerText(ans.getText());
        question.get().setAnswerUserId(ans.getUserId());

        ServiceResponse<QuestionsDAO> res = questionsService.upsert(question.get());
        
        return Response.status(res.getStatusCode()).build();
    }
}

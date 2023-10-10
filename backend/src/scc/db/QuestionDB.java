package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionDAO;

public class QuestionDB extends DBContainer {
    QuestionDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<QuestionDAO> putQuestion(QuestionDAO q) {
        return container.createItem(q);
    }

    public CosmosPagedIterable<QuestionDAO> getQuestion(String id) {
        return container.queryItems("SELECT * FROM questions WHERE questions.id=\"" + id + "\"", new CosmosQueryRequestOptions(), QuestionDAO.class);
    }
    // Get all questions from a house
    public CosmosPagedIterable<QuestionDAO> getQuestions(String houseId) {
        return container.queryItems("SELECT * FROM questions WHERE questions.houseId=\"" + houseId + "\"", new CosmosQueryRequestOptions(), QuestionDAO.class);
    }
}


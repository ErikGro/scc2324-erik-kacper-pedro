package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionDAO;

public class QuestionDB extends AbstractDB<QuestionDAO> {
    public QuestionDB(CosmosContainer container) {
        super(container, QuestionDAO.class);
    }

    // Get all questions from a house
    public CosmosPagedIterable<QuestionDAO> getQuestions(String houseId) {
        return container.queryItems("SELECT * FROM questions WHERE questions.houseId=\"" + houseId + "\"", new CosmosQueryRequestOptions(), QuestionDAO.class);
    }
}


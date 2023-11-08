package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionsDAO;

public class QuestionsDB extends AbstractDB<QuestionsDAO> {
    public QuestionsDB(CosmosContainer container) {
        super(container, QuestionsDAO.class);
    }

    // Get all questions from a house
    public synchronized CosmosPagedIterable<QuestionsDAO> getQuestions(String houseId) {
        return container.queryItems("SELECT * FROM questions WHERE questions.houseId=\"" + houseId + "\"", new CosmosQueryRequestOptions(), QuestionsDAO.class);
    }
}


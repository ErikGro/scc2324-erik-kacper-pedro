package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import java.util.List;

import scc.cache.ServiceResponse;
import scc.data.QuestionsDAO;
import scc.persistence.db.QuestionsDB;

public class CosmosQuestionsDB extends CosmosAbstractDB<QuestionsDAO> implements QuestionsDB<QuestionsDAO> {
    public CosmosQuestionsDB(CosmosContainer container) {
        super(container, QuestionsDAO.class);
    }

    // Get all questions from a house
    public synchronized ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId) {
        CosmosPagedIterable<QuestionsDAO> response = container.queryItems("SELECT * FROM questions WHERE questions.houseId=\"" + houseId + "\"", new CosmosQueryRequestOptions(), QuestionsDAO.class);

        return new ServiceResponse<>(200, response.stream().toList());
    }
}


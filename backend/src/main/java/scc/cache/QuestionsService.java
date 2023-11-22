package scc.cache;


import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionsDAO;
import scc.persistence.db.cosmos.CosmosDBLayer;
import scc.persistence.db.cosmos.CosmosQuestionsDB;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestionsService extends AbstractService<QuestionsDAO, CosmosQuestionsDB> {
    public QuestionsService() {
        super(QuestionsDAO.class, "question:", CosmosDBLayer.getInstance().getQuestionsDB());
    }

    // Get all questions from a house
    public ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId) {
        return db.getQuestions(houseId);
    }
}

package scc.cache;


import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionsDAO;
import scc.db.CosmosDBLayer;
import scc.db.QuestionsDB;

import java.util.Set;
import java.util.stream.Collectors;

public class QuestionsService extends AbstractService<QuestionsDAO, QuestionsDB> {
    public QuestionsService() {
        super(QuestionsDAO.class, "question:", CosmosDBLayer.getInstance().getQuestionsDB());
    }

    // Get all questions from a house
    public ServiceResponse<Set<QuestionsDAO>> getQuestions(String houseId) {
        CosmosPagedIterable<QuestionsDAO> questions = db.getQuestions(houseId);

        return new ServiceResponse<>(200, questions.stream().collect(Collectors.toSet()));
    }
}

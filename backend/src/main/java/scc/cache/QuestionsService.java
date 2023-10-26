package scc.cache;


import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.QuestionDAO;
import scc.data.QuestionsDAO;
import scc.db.CosmosDBLayer;
import scc.db.QuestionsDB;

public class QuestionsService extends AbstractService<QuestionsDAO, QuestionsDB> {
    public QuestionsService() {
        super(QuestionsDAO.class, "question:", CosmosDBLayer.getInstance().questionsDB);
    }

    // Get all questions from a house
    public ServiceResponse<CosmosPagedIterable<QuestionsDAO>> getQuestions(String houseId) {
        CosmosPagedIterable<QuestionsDAO> questions = db.getQuestions(houseId);
        return new ServiceResponse<>(200, questions);
    }
}

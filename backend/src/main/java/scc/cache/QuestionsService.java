package scc.cache;


import scc.data.QuestionsDAO;
import scc.persistence.db.QuestionsContainer;
import scc.persistence.db.mongo.MongoDBLayer;

import java.util.List;

public class QuestionsService extends AbstractService<QuestionsDAO, QuestionsContainer> {
    public QuestionsService() {
        super(QuestionsDAO.class, "question:", MongoDBLayer.getInstance().getQuestionsContainer());
    }

    // Get all questions from a house
    public ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId) {
        return container.getQuestions(houseId);
    }
}

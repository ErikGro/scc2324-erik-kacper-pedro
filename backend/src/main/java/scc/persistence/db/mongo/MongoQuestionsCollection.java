package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import scc.cache.ServiceResponse;
import scc.data.QuestionsDAO;
import scc.persistence.db.QuestionsContainer;

import java.util.List;

public class MongoQuestionsCollection extends MongoAbstractCollection<QuestionsDAO> implements QuestionsContainer {
    MongoQuestionsCollection(MongoCollection<QuestionsDAO> collection) {
        super(collection);
    }

    @Override
    synchronized public ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId) {
        return null;
    }
}

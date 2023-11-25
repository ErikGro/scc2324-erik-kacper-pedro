package scc.persistence.db.mongo;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import scc.cache.ServiceResponse;
import scc.data.QuestionsDAO;
import scc.data.house.HouseDAO;
import scc.persistence.db.QuestionsContainer;

import java.util.List;
import java.util.stream.Collectors;

import static dev.morphia.query.filters.Filters.eq;

public class MongoQuestionsCollection extends MongoAbstractCollection<QuestionsDAO> implements QuestionsContainer {
    MongoQuestionsCollection(Datastore datastore) {
        super(QuestionsDAO.class, datastore);
    }

    @Override
    synchronized public ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId) {
        List<QuestionsDAO> list = datastore.find(QuestionsDAO.class)
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, list);
    }
}

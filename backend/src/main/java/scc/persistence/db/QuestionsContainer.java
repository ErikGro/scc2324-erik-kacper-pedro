package scc.persistence.db;

import scc.cache.ServiceResponse;
import scc.data.QuestionsDAO;

import java.util.List;

public interface QuestionsContainer extends Container<QuestionsDAO> {
    ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId);
}

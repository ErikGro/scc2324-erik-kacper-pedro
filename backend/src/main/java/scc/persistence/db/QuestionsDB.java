package scc.persistence.db;

import scc.cache.ServiceResponse;
import java.util.List;

public interface QuestionsDB<QuestionsDAO> extends DB<QuestionsDAO> {
    ServiceResponse<List<QuestionsDAO>> getQuestions(String houseId);
}

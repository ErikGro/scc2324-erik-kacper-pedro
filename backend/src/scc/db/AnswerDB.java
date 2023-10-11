package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.AnswerDAO;

public class AnswerDB extends DBContainer {
    public AnswerDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<AnswerDAO> putAnswer(AnswerDAO a) {
        return container.createItem(a);
    }

    public boolean answerExists(String questionId) {
        CosmosPagedIterable<AnswerDAO> res = container.queryItems("SELECT * FROM answers WHERE answers.questionId=\"" + questionId + "\"", new CosmosQueryRequestOptions(), AnswerDAO.class);
        return res.iterator().hasNext();
    }
}

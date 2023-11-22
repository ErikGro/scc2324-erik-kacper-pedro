package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.cache.ServiceResponse;
import scc.data.UserDAO;
import scc.persistence.db.UserDB;

import java.util.List;
import java.util.stream.Collectors;

public class CosmosUserDB extends CosmosAbstractDB<UserDAO> implements UserDB<UserDAO> {
    public CosmosUserDB(CosmosContainer container) {
        super(container, UserDAO.class);
    }
    public synchronized ServiceResponse<List<UserDAO>> getByUsername(String username) {
        String query = "SELECT * FROM users WHERE users.username=\"" + username + "\"";
        CosmosPagedIterable<UserDAO> response = container.queryItems(query, new CosmosQueryRequestOptions(), UserDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }
}

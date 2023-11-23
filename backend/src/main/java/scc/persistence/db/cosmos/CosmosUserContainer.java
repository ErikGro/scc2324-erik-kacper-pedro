package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.cache.ServiceResponse;
import scc.data.UserDAO;
import scc.persistence.db.UserContainer;

import java.util.List;

public class CosmosUserContainer extends CosmosAbstractContainer<UserDAO> implements UserContainer {
    public CosmosUserContainer(CosmosContainer container) {
        super(container, UserDAO.class);
    }
    public synchronized ServiceResponse<UserDAO> getByUsername(String username) {
        String query = "SELECT * FROM users WHERE users.username=\"" + username + "\"";
        CosmosPagedIterable<UserDAO> response = container.queryItems(query, new CosmosQueryRequestOptions(), UserDAO.class);
        List<UserDAO> list = response.stream().toList();
        if (list.isEmpty()) {
            return new ServiceResponse<>(404);
        } else {
            return new ServiceResponse<>(200, list.get(0));
        }
    }
}

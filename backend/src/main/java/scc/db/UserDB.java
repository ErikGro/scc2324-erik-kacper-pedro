package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.UserDAO;
import scc.data.house.HouseDAO;

public class UserDB extends AbstractDB<UserDAO> {
    public UserDB(CosmosContainer container) {
        super(container, UserDAO.class);
    }
    public synchronized CosmosPagedIterable<UserDAO> getByUsername(String username) {
        String query = "SELECT * FROM users WHERE users.username=\"" + username + "\"";
        return container.queryItems(query, new CosmosQueryRequestOptions(), UserDAO.class);
    }
}

package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.UserDAO;

public class UserDB extends AbstractDB<UserDAO> {
    public UserDB(CosmosContainer container) {
        super(container, UserDAO.class);
    }

    public CosmosItemResponse<Object> delUser(UserDAO user) {
        return container.deleteItem(user, new CosmosItemRequestOptions());
    }

    public CosmosPagedIterable<UserDAO> getUsers() {
        return container.queryItems("SELECT * FROM users ", new CosmosQueryRequestOptions(), UserDAO.class);
    }

    public void deleteAllUsers() {
        CosmosPagedIterable<UserDAO> users = getUsers();
        
        for (UserDAO user : users) {
            delUser(user);
        }
    }
}

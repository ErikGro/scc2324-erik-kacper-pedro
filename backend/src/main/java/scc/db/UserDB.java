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
}

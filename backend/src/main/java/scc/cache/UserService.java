package scc.cache;

import scc.data.UserDAO;
import scc.db.CosmosDBLayer;
import scc.db.UserDB;

public class UserService extends AbstractService<UserDAO, UserDB> {
    public UserService() {
        super(UserDAO.class, "user:", CosmosDBLayer.getInstance().userDB);
    }
}
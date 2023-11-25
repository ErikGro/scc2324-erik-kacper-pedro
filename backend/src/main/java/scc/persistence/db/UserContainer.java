package scc.persistence.db;

import scc.cache.ServiceResponse;
import scc.data.UserDAO;

public interface UserContainer extends Container<UserDAO> {
    ServiceResponse<UserDAO> getByUsername(String username);
}

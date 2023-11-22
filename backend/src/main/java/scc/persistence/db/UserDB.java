package scc.persistence.db;

import scc.cache.ServiceResponse;

import java.util.List;

public interface UserDB<UserDAO> extends DB<UserDAO> {
    ServiceResponse<List<UserDAO>> getByUsername(String username);
}

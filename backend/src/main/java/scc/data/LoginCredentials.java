package scc.data;

import scc.utils.Hash;

/**
 * Uses as POST body for creating user and authenticating user
 */
public class LoginCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDAO toUserDAO() {
        UserDAO user = new UserDAO();
        user.setUsername(username);
        user.setPasswordHash(Hash.of(password));

        return user;
    }
}

package scc.data;

/**
 * Represents a User, as returned to the clients
 */

public class User {
    private String name;
    private String fullName;

    public User(UserDAO dao) {
        this.name = dao.getUsername();
        this.fullName = dao.getFullName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

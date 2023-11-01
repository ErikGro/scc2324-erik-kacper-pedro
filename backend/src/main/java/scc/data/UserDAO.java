package scc.data;

import scc.cache.Identifiable;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a User, as stored in the database
 */
public class UserDAO implements Identifiable {
    private String id;
    private String name;
    private String pwd;
    private Set<String> houseIds = Collections.emptySet();

    public UserDAO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Set<String> getHouseIds() {
        return houseIds;
    }

    public void setHouseIds(Set<String> houseIds) {
        this.houseIds = houseIds;
    }

    public User toUser() {
        return new User(id, name, pwd, houseIds);
    }

}

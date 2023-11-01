package scc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import scc.cache.Cachable;

/**
 * Represents a User, as stored in the database
 */
public class UserDAO implements Cachable {
	private String id;
	private String name;
	private String pwd;
	private Set<String> houseIds = Collections.emptySet();

	public UserDAO() {}

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
		return new User( id, name, pwd, houseIds);
	}
	public String getCachingKey() {
        return id;
    }
}

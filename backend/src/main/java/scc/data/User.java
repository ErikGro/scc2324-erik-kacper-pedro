package scc.data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import scc.utils.Hash;
/**
 * Represents a User, as returned to the clients
 * 
 * NOTE: array of house ids is shown as an example of how to store a list of elements and 
 * handle the empty list.
 */

public class User {
	private String id;
	private String name;
	private String pwd;
	private Set<String> houseIds = Collections.emptySet();
	public User() {}

	public User(String id, String name, String pwd, Set<String> houseIds) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.houseIds = houseIds;
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
		this.pwd = Hash.of(pwd);//password is now hashed to compare paswwords just hash the given before comparing
	}
	
	public Set<String> getHouseIds() {
		return houseIds;
	}
	public void setHouseIds(Set<String> houseIds) {
		this.houseIds = houseIds;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + ", houseIds="
				+ houseIds + "]";
	}
	public boolean isValid() {
		return !(id.isEmpty()&&pwd.isEmpty()&&name.isEmpty());
		
	}
}

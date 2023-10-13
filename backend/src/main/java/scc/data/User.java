package scc.data;
import java.util.Arrays;
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
	private String photoId;
	private String[] houseIds;
	public User() {
	}

	
	public User(String id, String name, String pwd, String photoId, String[] houseIds) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.photoId = photoId;
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
	

	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String[] getHouseIds() {
		return houseIds == null ? new String[0] : houseIds ;
	}
	public void setHouseIds(String[] houseIds) {
		this.houseIds = houseIds;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + ", photoId=" + photoId + ", houseIds="
				+ Arrays.toString(houseIds) + "]";
	}
	public boolean isValid() {
		return !(id.isEmpty()&&pwd.isEmpty()&&name.isEmpty());
		
	}
}

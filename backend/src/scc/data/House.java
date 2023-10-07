package scc.data;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Arrays;

/**
 * Represents a Giyse, as returned to the clients
 * 
 * NOTE: array of house ids is shown as an example of how to store a list of elements and 
 * handle the empty list.
 */
public class House {
	private String id;
	private String name;
	private String description;
	private String[] photoIds;
	public House(String id, String name, String description,  String[] photoIds) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.photoIds = photoIds;
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
	public String getdescription() {
		return description;
	}
	public void setdescription(String description) {
		this.pwd =description;
	}
	public String[] getphotoIds() {
		return photoIds == null ? new String[0] : photoIds ;
	}
	public void setphotoIds(String[] photoIds) {
		this.photoIds = photoIds;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", Description=" + description +  ", photoIds="
				+ Arrays.toString(photoIds) + "]";
	}
}


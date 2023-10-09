package scc.data;

/**
 * Represents a Giyse, as returned to the clients
 * 
 * NOTE: array of house ids is shown as an example of how to store a list of elements and 
 * handle the empty list.
 */
public class House {
	private String id;
	private String name;
	public House(String id, String name) {
		this.id = id;
		this.name = name;
	}
}


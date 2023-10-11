package scc.data;

public class House {
	private String id;
	private String name;

	public House() {}
	public House(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public House(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}


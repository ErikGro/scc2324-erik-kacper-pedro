package scc.data;

import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class House {
	private String id;
	private String name;

	private Address address;

	private String description;

	private String[] photoIDs;

	private Set<Month> availableMonths = new HashSet<>();
	private Map<Month, Float> normalPricePerMonth = new HashMap<>();
	private Map<Month, Float> promotionPricePerMonth = new HashMap<>();

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


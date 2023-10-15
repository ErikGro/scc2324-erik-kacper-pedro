package scc.data.house;

import java.util.*;
import java.util.stream.Collectors;

public class House {
	private String id;
	private String ownerID;
	private String name;
	private Address address;
	private String description;
	private String[] photoIDs;
	private Set<AvailablePeriod> availablePeriods;

	public House () {};

	public House(HouseDAO houseDAO, Set<AvailablePeriodDAO> availableMonths) {
		this.id = houseDAO.getId();
		this.ownerID = houseDAO.getOwnerID();
		this.name = houseDAO.getName();
		this.address = houseDAO.getAddress();
		this.description = houseDAO.getDescription();
		this.photoIDs = houseDAO.getPhotoIDs();

		this.availablePeriods = availableMonths.stream().map(AvailablePeriodDAO::toAvailablePeriod).collect(Collectors.toSet());
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getPhotoIDs() {
		return photoIDs;
	}

	public void setPhotoIDs(String[] photoIDs) {
		this.photoIDs = photoIDs;
	}

	public Set<AvailablePeriod> getAvailablePeriods() {
		return availablePeriods;
	}

	public void setAvailablePeriods(Set<AvailablePeriod> availablePeriods) {
		this.availablePeriods = availablePeriods;
	}
}


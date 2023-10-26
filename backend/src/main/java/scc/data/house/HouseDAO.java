package scc.data.house;

import scc.cache.Cachable;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HouseDAO implements Cachable {
    private String id;
    private String ownerID;
    private String name;
    private Address address;
    private String description;
    private List<String> photoIDs;

    private Set<AvailablePeriod> availablePeriods;

    public HouseDAO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhotoIDs() {
        return photoIDs;
    }

    public void setPhotoIDs(List<String> photoIDs) {
        this.photoIDs = photoIDs;
    }

    public Set<AvailablePeriod> getAvailablePeriods() {
        return availablePeriods;
    }

    public void setAvailablePeriods(Set<AvailablePeriod> availablePeriods) {
        this.availablePeriods = availablePeriods;
    }

    public String getCachingKey() {
        return id;
    }
}

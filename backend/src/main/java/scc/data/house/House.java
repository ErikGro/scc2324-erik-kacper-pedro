package scc.data.house;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class House {
    private String id;
    private String ownerID;
    private String name;
    private Address address;
    private String description;
    private List<String> photoIDs = Collections.emptyList();
    private Set<AvailablePeriod> availablePeriods = Collections.emptySet();

    public House(HouseDAO dao) {
        this.id = dao.getId();
        this.ownerID = dao.getOwnerID();
        this.name = dao.getName();
        this.address = dao.getAddress();
        this.description = dao.getDescription();
        this.photoIDs = dao.getPhotoIDs();
        this.availablePeriods = dao.getAvailablePeriods();
    }

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
        return Collections.unmodifiableList(photoIDs);
    }

    public void setPhotoIDs(List<String> photoIDs) {
        this.photoIDs = photoIDs;
    }

    public Set<AvailablePeriod> getAvailablePeriods() {
        return Collections.unmodifiableSet(availablePeriods);
    }

    public void setAvailablePeriods(Set<AvailablePeriod> availablePeriods) {
        this.availablePeriods = availablePeriods;
    }
}

package scc.data.house;

import java.time.Month;
import java.util.Map;
import java.util.Set;

public class HouseDAO {
    private String id;
    private String ownerID;
    private String name;
    private Address address;
    private String description;
    private String[] photoIDs;

    public HouseDAO() {}

    public HouseDAO(House house) {
        this.ownerID = house.getOwnerID();
        this.name = house.getName();
        this.address = house.getAddress();
        this.description = house.getDescription();
        this.photoIDs = house.getPhotoIDs();
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

    public String[] getPhotoIDs() {
        return photoIDs;
    }

    public void setPhotoIDs(String[] photoIDs) {
        this.photoIDs = photoIDs;
    }
}

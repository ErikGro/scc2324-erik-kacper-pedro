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
    private Set<Month> availableMonths;
    private Map<Month, Float> normalPricePerMonth;
    private Map<Month, Float> promotionPricePerMonth;

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

    public Set<Month> getAvailableMonths() {
        return availableMonths;
    }

    public void setAvailableMonths(Set<Month> availableMonths) {
        this.availableMonths = availableMonths;
    }

    public Map<Month, Float> getNormalPricePerMonth() {
        return normalPricePerMonth;
    }

    public void setNormalPricePerMonth(Map<Month, Float> normalPricePerMonth) {
        this.normalPricePerMonth = normalPricePerMonth;
    }

    public Map<Month, Float> getPromotionPricePerMonth() {
        return promotionPricePerMonth;
    }

    public void setPromotionPricePerMonth(Map<Month, Float> promotionPricePerMonth) {
        this.promotionPricePerMonth = promotionPricePerMonth;
    }

    public String[] getPhotoIDs() {
        return photoIDs;
    }

    public void setPhotoIDs(String[] photoIDs) {
        this.photoIDs = photoIDs;
    }
}

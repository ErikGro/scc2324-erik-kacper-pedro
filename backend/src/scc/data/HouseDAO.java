package scc.data;

import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HouseDAO {
    private String _rid;
    private String _ts;
    private String id;
    private String name;

    private Address address;

    private String description;

    private String[] photoIDs;

    private Set<Month> availableMonths;
    private Map<Month, Float> normalPricePerMonth;
    private Map<Month, Float> promotionPricePerMonth;

    public HouseDAO() {}

    public HouseDAO(String name, Address address, String description, Set<Month> availableMonths, Map<Month, Float> normalPricePerMonth, Map<Month, Float> promotionPricePerMonth) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.availableMonths = availableMonths;
        this.normalPricePerMonth = normalPricePerMonth;
        this.promotionPricePerMonth = promotionPricePerMonth;
    }

    public String get_rid() {
        return _rid;
    }

    public void set_rid(String _rid) {
        this._rid = _rid;
    }

    public String get_ts() {
        return _ts;
    }

    public void set_ts(String _ts) {
        this._ts = _ts;
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

package scc.data;

import java.time.Month;

public class RentalDAO {
    private String id;
    private String houseID;
    private String tenantID;
    private String startDate;
    private String endDate;
    private Float price;

    public RentalDAO() {}
    public RentalDAO(Rental rental) {
        this.houseID = rental.getHouseID();
        this.tenantID = rental.getTenantID();
        this.startDate = rental.getStartDate();
        this.endDate = rental.getEndDate();
        this.price = rental.getPrice();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

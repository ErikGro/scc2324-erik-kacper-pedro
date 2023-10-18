package scc.data;

import java.time.Month;

public class Rental {
    private String houseID;
    private String tenantID;
    private String startDate;
    private String endDate;
    private Float price;

    public Rental() {}
    public Rental(String houseID, String tenantID, String startDate, String endDate, Float price) {
        this.houseID = houseID;
        this.tenantID = tenantID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
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

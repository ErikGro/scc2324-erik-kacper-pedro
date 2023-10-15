package scc.data;

import java.time.Month;

public class Rental {
    private String houseID;
    private String tenantID;
    private Month startMonth;
    private Integer startYear;
    private Month endMonth;
    private Integer endYear;
    private Float price;

    public Rental() {}
    public Rental(String houseID, String tenantID, Month startMonth, Integer startYear, Month endMonth, Integer endYear, Float price) {
        this.houseID = houseID;
        this.tenantID = tenantID;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
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

    public Month getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Month startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Month getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Month endMonth) {
        this.endMonth = endMonth;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

package scc.data;

import java.time.Month;

public class RentalDAO {
    private String id;
    private String houseID;
    private String tenantID;
    private Month startMonth;
    private Integer startYear;
    private Month endMonth;
    private Integer endYear;
    private Float price;

    public RentalDAO() {}
    public RentalDAO(Rental rental) {
        this.houseID = rental.getHouseID();
        this.tenantID = rental.getTenantID();
        this.startMonth = rental.getStartMonth();
        this.startYear = rental.getStartYear();
        this.endMonth = rental.getEndMonth();
        this.endYear = rental.getEndYear();
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

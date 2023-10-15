package scc.data.house;

import java.time.Month;

public class AvailablePeriodDAO {
    private String id;
    private String houseID;
    private Month startMonth;
    private Integer startYear;
    private Month endMonth;
    private Integer endYear;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailablePeriodDAO() {};

    public AvailablePeriodDAO(AvailablePeriod period) {
        this.startMonth = period.getStartMonth();
        this.startYear = period.getStartYear();
        this.endMonth = period.getEndMonth();
        this.endYear = period.getEndYear();
        this.normalPrice = period.getNormalPrice();
        this.promotionPrice = period.getPromotionPrice();
    }

    public AvailablePeriod toAvailablePeriod() {
        return new AvailablePeriod(startMonth, startYear, endMonth, endYear, normalPrice, promotionPrice);
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

    public Float getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Float normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Float getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Float promotionPrice) {
        this.promotionPrice = promotionPrice;
    }
}

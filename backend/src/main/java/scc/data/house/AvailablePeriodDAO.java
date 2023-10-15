package scc.data.house;

import java.time.Month;

public class AvailablePeriodDAO {
    private String id;
    private String houseID;
    private String startDate;
    private String endDate;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailablePeriodDAO() {};

    public AvailablePeriodDAO(AvailablePeriod period) {
        this.startDate = period.getStartDate();
        this.endDate = period.getEndDate();
        this.normalPrice = period.getNormalPrice();
        this.promotionPrice = period.getPromotionPrice();
    }

    public AvailablePeriod toAvailablePeriod() {
        return new AvailablePeriod(startDate, endDate, normalPrice, promotionPrice);
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

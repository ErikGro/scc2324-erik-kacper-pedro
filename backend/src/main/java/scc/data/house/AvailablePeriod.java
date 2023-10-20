package scc.data.house;

import java.time.Month;

public class AvailablePeriod {
    private String startDate;
    private String endDate;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailablePeriod() {}

    public AvailablePeriod(String startDate, String endDate, Float normalPrice, Float promotionPrice) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.normalPrice = normalPrice;
        this.promotionPrice = promotionPrice;
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

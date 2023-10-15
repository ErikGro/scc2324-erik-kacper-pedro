package scc.data.house;

import java.time.Month;

public class AvailablePeriod {
    private Month startMonth;
    private Integer startYear;
    private Month endMonth;
    private Integer endYear;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailablePeriod() {}

    public AvailablePeriod(Month startMonth, Integer startYear, Month endMonth, Integer endYear, Float normalPrice, Float promotionPrice) {
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.normalPrice = normalPrice;
        this.promotionPrice = promotionPrice;
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

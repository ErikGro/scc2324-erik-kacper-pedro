package pt.unl.fct.di.scc;


public class AvailablePeriod {
    private String startDate;
    private String endDate;
    private Float normalPricePerDay;
    private Float promotionPricePerDay;

    public AvailablePeriod() {
    }

    public AvailablePeriod(String startDate, String endDate, Float normalPricePerDay, Float promotionPricePerDay) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.normalPricePerDay = normalPricePerDay;
        this.promotionPricePerDay = promotionPricePerDay;
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

    public Float getNormalPricePerDay() {
        return normalPricePerDay;
    }

    public void setNormalPricePerDay(Float normalPricePerDay) {
        this.normalPricePerDay = normalPricePerDay;
    }

    public Float getPromotionPricePerDay() {
        return promotionPricePerDay;
    }

    public void setPromotionPricePerDay(Float promotionPricePerDay) {
        this.promotionPricePerDay = promotionPricePerDay;
    }
}

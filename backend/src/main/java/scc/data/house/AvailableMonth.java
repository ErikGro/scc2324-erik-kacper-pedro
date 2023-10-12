package scc.data.house;

import java.time.Month;

public class AvailableMonth {
    private Month month;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailableMonth() {};

    public AvailableMonth(AvailableMonthDAO availableMonthDAO) {
        this.month = availableMonthDAO.getMonth();
        this.normalPrice = availableMonthDAO.getNormalPrice();
        this.promotionPrice = availableMonthDAO.getPromotionPrice();
    }

    public Month getMonth() {
        return month;
    }

    public Float getNormalPrice() {
        return normalPrice;
    }

    public Float getPromotionPrice() {
        return promotionPrice;
    }
}

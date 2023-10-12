package scc.data.house;

import java.time.Month;

public class AvailableMonthDAO {
    private String id;
    private String houseID;
    private Month month;
    private Float normalPrice;
    private Float promotionPrice;

    public AvailableMonthDAO() {};

    public AvailableMonthDAO(AvailableMonth availableMonth) {
        this.month = availableMonth.getMonth();
        this.normalPrice = availableMonth.getNormalPrice();
        this.promotionPrice = availableMonth.getPromotionPrice();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getHouseID() {
        return houseID;
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

package scc.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.Document;
import org.bson.types.ObjectId;
import scc.cache.Identifiable;

@Entity("rentals")
public class RentalDAO extends Document implements Identifiable {
    @Id
    private ObjectId mongoID;
    @Property("id")
    private String id;
    @Property("house_id")
    private String houseID;
    @Property("tenant_id")
    private String tenantID;
    @Property("start_date")
    private String startDate;
    @Property("end_date")
    private String endDate;
    @Property("price")
    private Float price;

    public RentalDAO() {}

    public ObjectId getMongoID() {
        return mongoID;
    }

    public void setMongoID(ObjectId mongoID) {
        this.mongoID = mongoID;
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

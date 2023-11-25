package scc.data.house;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import scc.cache.Identifiable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
@Entity("houses")
public class HouseDAO extends Document implements Identifiable{
    @Id
    private ObjectId mongoID;
    @Property("house_id")
    private String id;
    @Property("owner_id")
    private String ownerID;
    @Property("name")
    private String name;
    @Property("address")
    private Address address;
    @Property("description")
    private String description;
    @Property("photo_ids")
    private List<String> photoIDs = Collections.emptyList();
    @Property("available_periods")
    private Set<AvailablePeriod> availablePeriods = Collections.emptySet();

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

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhotoIDs() {
        return Collections.unmodifiableList(photoIDs);
    }

    public void setPhotoIDs(List<String> photoIDs) {
        this.photoIDs = photoIDs;
    }

    public Set<AvailablePeriod> getAvailablePeriods() {
        return Collections.unmodifiableSet(availablePeriods);
    }

    public void setAvailablePeriods(Set<AvailablePeriod> availablePeriods) {
        this.availablePeriods = availablePeriods;
    }
}

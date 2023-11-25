package scc.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import scc.cache.Identifiable;

/**
 * Represents a User, as stored in the database
 */
@Entity("users")
public class UserDAO implements Identifiable {
    private String id;
    @Id
    private ObjectId mongoID;
    @Property("username")
    private String username; // Aka nickname
    @Property("fullName")
    private String fullName;
    @Property("passwordHash")
    private String passwordHash;
    @Property("photo_id")
    private String photoID;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public User toUser() {
        User user = new User();
        user.setName(username);
        user.setPhotoID(photoID);

        return user;
    }
}

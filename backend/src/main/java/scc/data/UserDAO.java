package scc.data;

import scc.cache.Identifiable;

/**
 * Represents a User, as stored in the database
 */
public class UserDAO implements Identifiable {
    private String id;
    private String username;
    private String passwordHash;
    private String photoID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

package scc.data;

/**
 * Represents a User, as returned to the clients
 */

public class User {
    private String name;

    private String photoID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }
}

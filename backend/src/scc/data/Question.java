package scc.data;
import java.sql.Time;
import java.sql.Timestamp; // Is this correct import?

/**
 * Represents Questions asked about a house,
 * stores id of the question, houseId,
 * usedID - user who asked a question,
 * text of the question and timestamp of the question 
 */
public class Question {
    private String id; // Shouldn't it be int?
    private String houseId;
    private String userId;
    private String text;
    private Timestamp timestamp;
    
    public Question(String id, String houseId, String userId, String text, Timestamp timestamp) {
        super();
        this.id = id;
        this.houseId = houseId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String name) {
        this.houseId = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", houseId=" + houseId + ", userId=" + userId + ", text=" + text + ", timestamp=" + timestamp + "]";
    }

}

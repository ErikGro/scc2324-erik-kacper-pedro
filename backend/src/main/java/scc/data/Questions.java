package scc.data;

import scc.cache.Identifiable;

/**
 * Represents Questions asked about a house,
 * stores id of the question, houseId,
 * usedID - user who asked a question,
 * text of the question and timestamp of the question 
 */
public class Questions implements Identifiable {
    private String id; 
    private String houseId;
    private String userId;
    private String text;
    private String timestamp;
    private String answerUserId;
    private String answerText;
    private String answerTimestamp;

    public Questions () {}
    
    public Questions(String id, String houseId, String userId, String text, String timestamp, String answerUserId, String answerText, String answerTimestamp) {
        this.id = id;
        this.houseId = houseId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
        this.answerUserId = answerUserId;
        this.answerText = answerText;
        this.answerTimestamp = answerTimestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(String userId) {
        this.answerUserId = userId;
    }

    public String getAnswerText() {
        return answerText;
    }


    public void setAnswerText(String text) {
        this.answerText = text;
    }

    public String getAnswerTimestamp() {
        return answerTimestamp;
    }

    public void setAnswerTimestamp(String timestamp) {
        this.answerTimestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Question [id=" + id + ", houseId=" + houseId + ", userId=" + userId + ", text=" + text + ", timestamp=" + timestamp + "]\n" +
                "Answer [userId=" + answerUserId + ", text=" + answerText + ", timestamp=" + answerTimestamp + "]\n";
    }

}

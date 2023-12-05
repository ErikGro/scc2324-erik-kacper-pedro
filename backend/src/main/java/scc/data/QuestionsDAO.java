package scc.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import org.bson.Document;
import org.bson.types.ObjectId;
import scc.cache.Identifiable;

@Entity("questions")
public class QuestionsDAO implements Identifiable {
    @Id
    private ObjectId mongoID;
    @Property("id")
    private String id;
    @Property("house_id")
    private String houseId;
    @Property("user_id")
    private String userId;
    @Property("text")
    private String text;
    @Property("timestamp")
    private String timestamp;
    @Property("answer_user_id")
    private String answerUserId;
    @Property("answer_text")
    private String answerText;
    @Property("answer_timestampt")
    private String answerTimestamp;

    public QuestionsDAO() {}

    public ObjectId getMongoID() {
        return mongoID;
    }

    public void setMongoID(ObjectId mongoID) {
        this.mongoID = mongoID;
    }

    public QuestionsDAO(Questions q) {
        this(q.getId(), q.getHouseId(), q.getUserId(), q.getText(), q.getTimestamp(), q.getAnswerUserId(), q.getAnswerText(), q.getAnswerTimestamp());
    }

    public QuestionsDAO(String id, String houseId, String userId, String text, String timestamp, String answerUserId, String answerText, String answerTimestamp) {
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

    public Questions toQuestion() {
        return new Questions(id, houseId, userId, text, timestamp, answerUserId, answerText, answerTimestamp);
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

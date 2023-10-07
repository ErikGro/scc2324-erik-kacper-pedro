package scc.data;
import java.sql.Timestamp;

/**
 * Represents Answers to questions asked about a house,
 * stores id of the answer, questionId,
 * usedID - user who answered a question,
 * text of the answer and timestamp of the answer
 */
public class Answer {
    private String id;
    private String questionId;
    private String userId;
    private String text;
    private Timestamp timestamp;

    public Answer(String id, String questionId, String userId, String text, Timestamp timestamp) {
        super();
        this.id = id;
        this.questionId = questionId;
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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String name) {
        this.questionId = name;
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
        return "Answer [id=" + id + ", questionId=" + questionId + ", userId=" + userId + ", text=" + text + ", timestamp=" + timestamp + "]";
    }
}

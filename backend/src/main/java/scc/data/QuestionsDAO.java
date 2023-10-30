package scc.data;

import scc.cache.Cachable;

public class QuestionsDAO implements Cachable {

    private String _rid;
    private String _ts;
    private String id;
    private String houseId;
    private String userId;
    private String text;
    private String timestamp;
    private String answerUserId;
    private String answerText;
    private String answerTimestamp;

    public QuestionsDAO() {}
    
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
    
    public String get_rid() {
		return _rid;
	}
	public void set_rid(String _rid) {
		this._rid = _rid;
	}
	public String get_ts() {
		return _ts;
	}
	public void set_ts(String _ts) {
		this._ts = _ts;
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

    public String getCachingKey() {
        return id;
    }
}

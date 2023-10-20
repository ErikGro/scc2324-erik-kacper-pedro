package scc.data;

public class QuestionDAO {

    private String _rid;
    private String _ts;
    private String id;
    private String houseId;
    private String userId;
    private String text;
    private String timestamp;

    public QuestionDAO() {}
    
    public QuestionDAO(Question q) {
        this(q.getId(), q.getHouseId(), q.getUserId(), q.getText(), q.getTimestamp());
    }

    public QuestionDAO(String id, String houseId, String userId, String text, String timestamp) {
        this.id = id;
        this.houseId = houseId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
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

    public Question toQuestion() {
        return new Question(id, houseId, userId, text, timestamp);
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", houseId=" + houseId + ", userId=" + userId + ", text=" + text + ", timestamp=" + timestamp + "]";
    }


}

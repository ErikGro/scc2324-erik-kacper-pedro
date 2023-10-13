package scc.data;

public class AnswerDAO {
    
    private String _rid;
    private String _ts;
    private String id;
    private String questionId;
    private String userId;
    private String text;
    private String timestamp;

    public AnswerDAO() {}

    public AnswerDAO(Answer a) {
        this(a.getId(), a.getQuestionId(), a.getUserId(), a.getText(), a.getTimestamp());
    }
    
    public AnswerDAO(String id, String questionId, String userId, String text, String timestamp) {
        this.id = id;
        this.questionId = questionId;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Answer [id=" + id + ", questionId=" + questionId + ", userId=" + userId + ", text=" + text + ", timestamp=" + timestamp + "]";
    } 

}

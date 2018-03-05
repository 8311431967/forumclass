package kun.clSystem.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "reply_id")
    private Integer replyId;

    @Column(name = "reply_name")
    private String replyName;

    @Column(name = "thread")
    private String thread;

    @Column(name = "summary")
    private String summary;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "num_of_support")
    private Integer numOfSupport;

    @Column(name = "num_of_answer")
    private Integer numOfAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id",nullable = false)
    private Question question;

    private boolean isSupport;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getNumOfSupport() {
        return numOfSupport;
    }

    public void setNumOfSupport(Integer numOfSupport) {
        this.numOfSupport = numOfSupport;
    }

    public Integer getNumOfAnswer() {
        return numOfAnswer;
    }

    public void setNumOfAnswer(Integer numOfAnswer) {
        this.numOfAnswer = numOfAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isSupport() {
        return isSupport;
    }

    public void setSupport(boolean support) {
        isSupport = support;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", replyId=" + replyId +
                ", replyName='" + replyName + '\'' +
                ", thread='" + thread + '\'' +
                ", summary='" + summary + '\'' +
                ", time=" + time +
                ", numOfSupport=" + numOfSupport +
                ", numOfAnswer=" + numOfAnswer +
                ", question=" + question +
                ", isSupport=" + isSupport +
                ", type=" + type +
                '}';
    }
}

package kun.clSystem.domain;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "question_content")
public class QuestionContent implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "content")
    private String content;

    public QuestionContent(Integer questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "QuestionContent{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                '}';
    }
}

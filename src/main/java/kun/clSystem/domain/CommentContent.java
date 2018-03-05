package kun.clSystem.domain;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "comment_content")
public class CommentContent implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "content")
    private String content;

    public CommentContent(Integer commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
    public CommentContent(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentContent{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                '}';
    }
}

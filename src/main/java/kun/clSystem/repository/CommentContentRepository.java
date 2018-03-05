package kun.clSystem.repository;

import kun.clSystem.domain.CommentContent;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface CommentContentRepository extends Repository<CommentContent, Integer> {
    CommentContent save(CommentContent commentContent);

    CommentContent findByCommentId(Integer commentId);
}

package kun.clSystem.repository;

import kun.clSystem.domain.Support;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
@org.springframework.stereotype.Repository
public interface SupportRepository extends Repository<Support,Integer> {
    @Query("select count(s.id) from Support s where s.userId=?1 and s.commentId=?2")
    int isSupportExisted(Integer uid, Integer id);

    void save(Support support);

    @Query("delete from Support s where s.userId=?1 and s.commentId=?2")
    @Modifying
    int deleteByUserIdAndCommentId(Integer uid, Integer commentId);
}

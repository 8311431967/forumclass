package kun.clSystem.repository;

import kun.clSystem.domain.Question;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface QuestionRepository extends Repository<Question,Integer> {
    Question save(Question question);

    @Query("select q from Question q order by q.numOfAnswers desc")
    @Cacheable
    List<Question> findOrderByHot(Pageable pageable);
    //    List<Question> findAll();
    Page<Question> findAll(Pageable pageable);

    @Query("select q from Question q where q.authorId=?1")
    Page<Question> getUserQuestion(Integer uid, Pageable pageable);

    @Query("select count(q.id) from Question q where q.authorId=?1")
    int getQuestionCountByAuthorId(Integer uid);

    @Cacheable
    Question findById(Integer qid);

    @Query("delete from Question q where q.id=?1 and q.authorId=?2")
    @Modifying
    int removeQuestion(Integer qid, Integer uid);

    @Query("select q from Question q where q.title like concat('%',?1,'%') ")
    List<Question> getQuestionsByKey(String key);

    @Query("update Question  q set q.numOfAnswers=q.numOfAnswers+1 where q.id=?1")
    @Modifying
    void addComment(Integer id);

    @Query("update Question q set q.numOfAnswers=q.numOfAnswers-?2 where q.id=?1")
    @Modifying
    void substractComment(int qid,int number);
}

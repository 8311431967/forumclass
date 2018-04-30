package kun.clSystem.repository;

import kun.clSystem.domain.QuestionContent;
import org.springframework.data.repository.Repository;
@org.springframework.stereotype.Repository
public interface QuestionContentRepository extends Repository<QuestionContent,Integer> {
    QuestionContent save(QuestionContent questionContent);

    QuestionContent findByQuestionId(Integer questionId);
}

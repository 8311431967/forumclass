package kun.clSystem.service;

import kun.clSystem.domain.Comment;
import kun.clSystem.domain.Question;
import kun.clSystem.domain.QuestionContent;
import kun.clSystem.repository.CommentRepository;
import kun.clSystem.repository.QuestionContentRepository;
import kun.clSystem.repository.QuestionRepository;
import kun.clSystem.repository.SupportRepository;
import kun.clSystem.util.ContentUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private QuestionContentRepository questionContentRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private SupportRepository supportRepository;

    @Transactional
    public Question insert(Question question) {
        Timestamp time=new Timestamp(System.currentTimeMillis());
        question.setTime(time);
        String summary=question.getSummary();
//        System.out.print("summary:"+summary);
        question.setNumOfAnswers(0);
        if(summary.length()>200){
            question.setSummary(summary.substring(0,200));
            question.setType(1);
            question=questionRepository.save(question);
            QuestionContent questionContent=new QuestionContent(question.getId(),summary);
            questionContentRepository.save(questionContent);
        }else{
            question.setType(0);
            question=questionRepository.save(question);
        }
        question.setSummary(ContentUtil.transform(question.getSummary()));
        return question;
    }


    public QuestionContent getQuestionContent(Integer id) {
        QuestionContent q=questionContentRepository.findByQuestionId(id);
        if(q!=null){
            q.setContent(ContentUtil.transform(q.getContent()));
        }
        return q;
    }

    public Page<Question> getQuestionListByPageOrderByTime(Pageable pageable) {
        Page<Question> questionPage = questionRepository.findAll(pageable);
        List<Question> questions = questionPage.getContent();
        for(Question q:questions){
            q.setSummary(ContentUtil.transform(q.getSummary()));
        }
        return questionPage;
    }

    public List<Question> getQuestionListByPageOrderByHot(Pageable pageable) {

//        int begin=page*size;
        List<Question> questions = questionRepository.findOrderByHot(pageable);
//        List<Question>  = questionPage.getContent();
        for(Question q:questions){
            q.setSummary(ContentUtil.transform(q.getSummary()));
        }
        return questions;
    }

    public int  getQuestionCountByAuthorId(Integer uid){
        return questionRepository.getQuestionCountByAuthorId(uid);
    }

    public Question getQuestionById(Integer qid) {
        Question question = questionRepository.findById(qid);
        if(question!=null){
            question.setSummary(ContentUtil.transform(question.getSummary()));
        }
        return question;
    }

    public Map<String,Object> getQuestionComments(Integer uid, Integer qid, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getQuestionComments(qid, pageable);
        List<Comment> commentList=commentPage.getContent();
        if(uid!=null){
            for(Comment comment:commentList){
                comment.setSummary(ContentUtil.transform(comment.getSummary()));
                if(supportRepository.isSupportExisted(uid,comment.getId())>0){
                    comment.setSupport(true);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("last", commentPage.isLast());
        map.put("content", commentList);
        return map;
    }

    @Transactional
    public boolean removeQuestion(Integer qid, Integer uid) {
        if(questionRepository.removeQuestion(qid,uid)>0){
            return true;
        }else{
            return false;
        }
    }

    public List<Question> searchQuestion(String key) {
        List<Question> questionList=questionRepository.getQuestionsByKey(key);
        for(Question q:questionList){
            q.setSummary(ContentUtil.transform(q.getSummary()));
        }
        return questionList;
    }
}

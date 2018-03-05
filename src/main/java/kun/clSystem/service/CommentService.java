package kun.clSystem.service;

import kun.clSystem.domain.Comment;
import kun.clSystem.domain.CommentContent;
import kun.clSystem.repository.CommentContentRepository;
import kun.clSystem.repository.CommentRepository;
import kun.clSystem.repository.QuestionRepository;
import kun.clSystem.repository.SupportRepository;
import kun.clSystem.util.ContentUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private CommentContentRepository commentContentRepository;
    @Resource
    private SupportRepository supportRepository;
    @Resource
    private QuestionRepository questionRepository;

    @Transactional
    public Comment saveComment(Comment comment) {
        if(comment.getQuestion().getId()==null
                || comment.getAuthorId()==null || comment.getAuthorName()==null
                ||comment.getSummary()==null){
            return null;
        }
        Timestamp time=new Timestamp(System.currentTimeMillis());
        comment.setTime(time);
        System.out.println(comment);
        String summary=comment.getSummary();
        if(summary.length()>200){
            comment.setSummary(summary.substring(0,200));
            comment.setType(1);
            comment=commentRepository.save(comment);
            CommentContent commentContent=new CommentContent(comment.getId(),summary);
            commentContentRepository.save(commentContent);
        }else{
            comment=commentRepository.save(comment);
        }

        //若是第一级评论，则问题增加一个评论
        //若不是第一级评论，则父评论增加一个评论
        if(comment.getParentId()!=null){
            Integer ppid=Integer.parseInt(comment.getThread().split("/")[1]);
//            System.out.println("ppid:"+ppid);
            commentRepository.addChildComment(ppid);
        }else{
            questionRepository.addComment(comment.getQuestion().getId());
        }
//        comment.setSummary(ContentUtil.transform(comment.getSummary()));
        return comment;
    }


    public Page<Comment> getHotCommentsByPage(Pageable pageable, Integer uid) {
        Page<Comment> commentPage = commentRepository.findByHot(pageable);
        List<Comment> commentList = commentPage.getContent();
        if(uid!=null){
            for(Comment comment:commentList){
                comment.setSummary(ContentUtil.transform(comment.getSummary()));
                if(supportRepository.isSupportExisted(uid,comment.getId())>0){
                    comment.setSupport(true);
                }
            }
        }
        return commentPage;
    }

    public List<Comment> getChildComment(Integer uid, Integer qid, String thread) {
        List<Comment> commentList=commentRepository.findByThread(qid,thread);
        if(uid!=null){
            for(Comment comment:commentList){
                comment.setSummary(ContentUtil.transform(comment.getSummary()));
                if(supportRepository.isSupportExisted(uid,comment.getId())>0){
                    comment.setSupport(true);
                }
            }
        }
        return commentList;
    }


    public CommentContent findByCommentId(Integer id) {
        CommentContent commentContent = commentContentRepository.findByCommentId(id);
        if(commentContent!=null){
            commentContent.setContent(ContentUtil.transform(commentContent.getContent()));
        }
        return commentContent;
    }

    @Transactional
    public int removeComment(Integer cid, Integer uid) {
        Comment comment=commentRepository.findById(cid);
        if(comment==null || !comment.getAuthorId().equals(uid)){
            return 0;
        }
        //获得要删除的评论数（该评论+子评论）
        int number = commentRepository.getNumOfChildComment(comment.getThread()+cid+"/")+1;
        //删除该评论,获得删除的评论数(该评论及其子评论)
        commentRepository.removeComment(cid,comment.getThread()+cid+"/");
        System.out.println("number:"+number);
        //如果有父评论，则其回答数减number
        if(comment.getParentId()!=null){
            Integer ppid=Integer.parseInt(comment.getThread().split("/")[1]);
            commentRepository.subNumOfAnswer(ppid,number);
        }else{
            //更新问题评论数
            questionRepository.substractComment(comment.getQuestion().getId(),1);
        }
        return number;
    }
}

package kun.clSystem.service;

import kun.clSystem.domain.Support;
import kun.clSystem.repository.CommentRepository;
import kun.clSystem.repository.SupportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SupportService {

    @Resource
    private SupportRepository supportRepository;
    @Resource
    private CommentRepository commentRepository;

    @Transactional
    public boolean save(Support support) {
        try{
            supportRepository.save(support);
            //增加评论的赞数
            commentRepository.addSupport(support.getCommentId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean removeSupport(Integer uid, Integer commentId) {
        if(supportRepository.deleteByUserIdAndCommentId(uid,commentId)>0){
            commentRepository.removeSupport(commentId);//评论的赞数减一
            return true;
        }else{
            return false;
        }
    }
}

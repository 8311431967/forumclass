package kun.clSystem.service;

import kun.clSystem.domain.Comment;
import kun.clSystem.domain.Question;
import kun.clSystem.domain.User;
import kun.clSystem.dto.Message;
import kun.clSystem.repository.CommentRepository;
import kun.clSystem.repository.QuestionRepository;
import kun.clSystem.repository.SupportRepository;
import kun.clSystem.repository.UserRepository;
import kun.clSystem.util.MD5Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private SupportRepository supportRepository;

    public Message<String> insertUser(String email, String username, String password1, String password2) {
        Message<String> message=new Message<>();
        message.setFlag("fail");
        if(!password1.equals(password2)){
            message.setContent("密码不一致");
            return message;
        }
        String md5Pass= MD5Util.getMD5(password1);
        User user=new User(email,username,md5Pass);
        try{
            userRepository.save(user);
            message.setFlag("success");
        }catch (Exception e){
            e.printStackTrace();
            message.setContent("插入数据库出错");
        }
        return message;

    }

    public User doLogin(User user) {
        String md5Pass= MD5Util.getMD5(user.getPassword());
        user.setPassword(md5Pass);
        User user1=userRepository.findByEmailAndPassword(user.getEmail(),user.getPassword());

        return user1;
    }

    public User findUserById(Integer id) {
        User user = userRepository.findOne(id);
        if(user!=null){
            user.setNumOfQuestion(questionRepository.getQuestionCountByAuthorId(id));
            user.setNumOfAnswer(commentRepository.getCommentCountByUserId(id));
        }
        return user;
    }

    @Transactional
    public boolean updateUserSex(Integer uid, Integer sex) {
        try{
            userRepository.updateUserSex(uid,sex);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public String updatePassword(Integer uid, String oldPass, String newPass1, String newPass2) {
        if(!newPass1.equals(newPass2)){
            return "密码不同";
        }
        if(userRepository.updatePassword(uid,MD5Util.getMD5(oldPass),MD5Util.getMD5(newPass1))>0){
            return "success";
        }else{
            return "原密码错误";
        }

    }

    public Page<Question> getUserQuestion(Integer uid, Pageable pageable) {
        return questionRepository.getUserQuestion(uid,pageable);
    }

    public Map<String,Object> getUserAnswers(Integer uid, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.getUserAnswers(uid, pageable);
        Map<String, Object> map = new HashMap<>();
        List<Comment> commentList = commentPage.getContent();
        for(Comment comment:commentList){
            if(supportRepository.isSupportExisted(uid,comment.getId())>0){
                comment.setSupport(true);
            }else{
                comment.setSupport(false);
            }
        }
        map.put("isLast", commentPage.isLast());
        map.put("content", commentList);
        return map;
    }

    public List<User> searchUser(String key) {
        List<User> userList = userRepository.searchUserByKey(key);
        for(User user:userList){
            user.setNumOfQuestion(questionRepository.getQuestionCountByAuthorId(user.getId()));
            user.setNumOfAnswer(commentRepository.getCommentCountByUserId(user.getId()));
            user.setPassword(null);
        }
        return userList;
    }
}

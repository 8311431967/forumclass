package kun.clSystem.controller;

import com.google.gson.Gson;
import kun.clSystem.domain.Question;
import kun.clSystem.domain.User;
import kun.clSystem.dto.Message;
import kun.clSystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    @Resource
    private UserService userService;
    private Gson gson=new Gson();
    //    处理用户注册
    @PostMapping(value = "/user")
    @ResponseBody
    public String insertUser(@RequestParam(value = "email")String email,
                             @RequestParam(value = "username")String username,
                             @RequestParam(value = "password1")String password1,
                             @RequestParam(value = "password2")String password2){
        System.out.print(email+" "+username+" "+password1+" "+password2);
        Message<String> message=userService.insertUser(email,username,password1,password2);
        return gson.toJson(message);
    }

    //    处理用户登录
    @PostMapping(value = "/loginResult",produces = "application/json")
    @ResponseBody
    public String doLogin(@RequestParam("email")String email,
                          @RequestParam("password")String password,
                          @RequestParam("code")String checkCode,
                          HttpSession session){
        System.out.println("checkCode:"+checkCode);
        Message<String> message=new Message<>();
        message.setFlag("fail");

        Object cko = session.getAttribute("simpleCaptcha") ; //验证码对象
        if(cko == null){
            message.setContent("验证码已失效，请重新输入！");
            return gson.toJson(message);
        }
        String captcha = cko.toString();
        System.out.println("captcha:"+captcha);
        Date now = new Date();
        Long codeTime = Long.valueOf(session.getAttribute("codeTime")+"");
        if(StringUtils.isEmpty(checkCode) || captcha == null ||  !(checkCode.equalsIgnoreCase(captcha))) {
            message.setContent("验证码错误");
            return gson.toJson(message);
        } else if ((now.getTime()-codeTime)/1000/60>5) {
            //验证码有效时长为5分钟
            message.setContent("验证码已失效，请重新输入！");
            return gson.toJson(message);
        }

        User user=new User(email,password);
        session.removeAttribute("simpleCaptcha");
        User user1=userService.doLogin(user);
        if(user1!=null){
            message.setFlag("success");
            session.setAttribute("user",user1);
        }else{
            message.setFlag("fail");
            message.setContent("登录失败，检查用户名或密码！");
        }
        return gson.toJson(message);
    }
    //    请求id用户信息
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public String showUser(@PathVariable("id")Integer id){
        Map<String,Object> map=new HashMap<>();
        User user=userService.findUserById(id);
        if(user!=null){
            map.put("result","success");
            user.setPassword(null);
            map.put("user",user);
//            添加回答数，提问数
        }else{
            map.put("result","fail");
        }
        return gson.toJson(map);
    }

    @GetMapping(value = "/u/{id}")
    public String showUserPage(@PathVariable("id")Integer uid){
        return "myAnswers";
    }

    //    更新用户性别
    @PostMapping(value = "/u/{id}/sex")
    @ResponseBody
    public String updateUserSex(@PathVariable("id")Integer uid,@RequestParam("sex")Integer sex){
        Map<String,String> map=new HashMap<>();
        if(userService.updateUserSex(uid,sex)){
            map.put("result","success");
        }else{
            map.put("result","fail");
        }
        return gson.toJson(map);
    }

    //    更新密码
    @PostMapping(value = "/u/{id}/password")
    @ResponseBody
    public String updatePassword(@PathVariable("id")Integer uid,
                                 @RequestParam("oldPass")String oldPass,
                                 @RequestParam("newPass1")String newPass1,
                                 @RequestParam("newPass2")String newPass2){
        Map<String,String> map=new HashMap<>();
        String result=userService.updatePassword(uid,oldPass,newPass1,newPass2);
        if(result.equals("success")){
            map.put("result",result);
        }else{
            map.put("result","fail");
            map.put("reason",result);
        }

        return gson.toJson(map);
    }

    //    查看id为{id}的用户的提问
    @GetMapping(value = "/u/{id}/questions")
    @ResponseBody
    public Map<String,Object> getUserQuestion(@RequestParam(value = "page",defaultValue = "0")Integer page,
                                              @RequestParam(value = "size",defaultValue = "2")Integer size,
                                              @PathVariable("id")Integer uid){
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        Pageable pageable=new PageRequest(page,size,sort);
        Page<Question> questionPage=userService.getUserQuestion(uid,pageable);
        Map<String,Object> map=new HashMap<>();
        map.put("content", questionPage.getContent());
        map.put("last", questionPage.isLast());
        map.put("number", questionPage.getTotalElements());
        return map;
    }

    //    查看id为{id}的用户的回答
    @GetMapping(value = "/u/{id}/answers")
    @ResponseBody
    public Map<String,Object> getUserAnswers(@RequestParam(value = "page",defaultValue = "0")Integer page,
                                             @RequestParam(value = "size",defaultValue = "2")Integer size,
                                             @PathVariable("id")Integer uid){
        Sort sort=new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(page, size, sort);
        return userService.getUserAnswers(uid, pageable);

    }

    @GetMapping(value = "/user/{id}/answers")
    public String showUserAnswers(@PathVariable("id")Integer uid){
        return "myAnswers";
    }

    @GetMapping(value = "/user/{id}/questions")
    public String showUserQuestions(@PathVariable("id")Integer uid){
        return "myAsks";
    }

    @GetMapping(value = "/user/{id}/infos")
    public String showUserInfos(@PathVariable("id")Integer uid){
        return "myInfo";
    }
}

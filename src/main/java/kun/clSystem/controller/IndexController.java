package kun.clSystem.controller;

import kun.clSystem.domain.Question;
import kun.clSystem.domain.User;
import kun.clSystem.service.QuestionService;
import kun.clSystem.service.UserService;
import kun.clSystem.util.ImageCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Resource
    private UserService userService;
    @Resource
    private QuestionService questionService;

    @GetMapping(value = {"/","/login"})
    public String index(HttpSession session){
        return "login";
//        User user=new User();
//        user.setUserName("dragon");
//        user.setId(1);
//        user.setSex(1);
//        user.setEmail("1803240383@qq.com");
//        user.setIntegral(0);
//        session.setAttribute("user",user);
//        return "redirect:/index";
    }
    @GetMapping(value = "/register")
    public String register(){
        return "register";
    }

    @GetMapping(value = "/index")
    public String showIndex(){
        return "home";
    }

    @GetMapping(value = "/discovery")
    public String showDescovery(){
        return "discovery";
    }

    @GetMapping(value = "/searchQuestion")
    public String searchQuestion(){
        return "searchResultQue";
    }

    @GetMapping(value = "/searchUser")
    public String searchUser(){
        return "searchResultUser";
    }

    @PostMapping(value = "/searchQResult")
    @ResponseBody
    public Map<String,Object> getSearchQResult(@RequestParam("key")String key){
        Map<String,Object> map=new HashMap<>();
        if(key==null || "".equals(key.trim())){
            map.put("count",0);
            return map;
        }
        List<Question> questionList=questionService.searchQuestion(key);
        if(questionList.size()==0){
            map.put("count",0);
        }else{
            map.put("count",questionList.size());
            map.put("content",questionList);
        }
        return map;
    }

    @PostMapping(value = "/searchUResult")
    @ResponseBody
    public Map<String,Object> getSearchUResult(@RequestParam("key")String key){
        Map<String,Object> map=new HashMap<>();
        if(key==null || "".equals(key.trim())){
            map.put("count",0);
            return map;
        }
        List<User> userList=userService.searchUser(key);
        if(userList.size()==0){
            map.put("count",0);
        }else{
            map.put("count",userList.size());
            map.put("content",userList);
        }
        return map;
    }

    @RequestMapping(value = "/images/imagecode")
    public String imagecode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();
        Map<String,Object> map = ImageCode.getImageCode(60, 20, os);
        String simpleCaptcha = "simpleCaptcha";
        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        request.getSession().setAttribute("codeTime",new Date().getTime());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
        } catch (IOException e) {
            return "";
        }
        return null;
    }

    @RequestMapping(value = "/test")
    public String test(){
        return "test";
    }

    @PutMapping(value = "/testPut")
    @ResponseBody
    public String testPut(){
        return "put success!";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}

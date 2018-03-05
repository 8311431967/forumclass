package kun.clSystem.controller;

import kun.clSystem.domain.Support;
import kun.clSystem.service.SupportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/supports")
public class SupportController {
    @Resource
    private SupportService supportService;
    //对回答点赞
    @PostMapping(value = "/support")
    @ResponseBody
    public Map<String, String> save(@RequestBody Support support){
        Map<String, String> map = new HashMap<>();
        if(supportService.save(support)){
            map.put("result", "success");
        }else{
            map.put("result", "fail");
        }
        return map;
    }

    //取消点赞
    @PostMapping("/support/remove")
    @ResponseBody
    public Map<String,String> removeSupport(@RequestParam("userId")Integer uid,
                                            @RequestParam("commentId")Integer commentId){
        Map<String, String> map = new HashMap<>();
        if(supportService.removeSupport(uid,commentId)){
            map.put("result", "success");
        }else{
            map.put("result", "fail");
        }
        return map;
    }

}

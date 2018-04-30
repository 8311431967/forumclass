package kun.clSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController {
    @RequestMapping("/500")
    public String showServerError() {
        return "500";
    }

    @RequestMapping(value = "/404")
    public String showNotFoundError(){
        return "404";
    }
}

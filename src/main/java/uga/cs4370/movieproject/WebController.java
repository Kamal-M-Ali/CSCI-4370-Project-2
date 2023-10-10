package uga.cs4370.movieproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("dynamic")
public class WebController {
    @GetMapping("/")
    @ResponseBody
    public String root() {
        return "Hello from root";
    }

    @GetMapping("/page")
    public ModelAndView page() {
        ModelAndView mv = new ModelAndView("page1");
        mv.addObject("message", "Hello World");
        return mv;
    }

}
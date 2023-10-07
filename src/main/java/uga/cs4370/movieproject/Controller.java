package uga.cs4370.movieproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dynamic")
public class Controller {
    @GetMapping("/")
    public String index() { return "Greetings from Spring Boot!"; }
}

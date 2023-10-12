package uga.cs4370.movieproject;

import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("dynamic")
public class WebController {
    private final APIHandler apiHandler;
    WebController() { apiHandler = new APIHandler(); }

    @GetMapping("/")
    public ModelAndView index()
    {
        return apiHandler.index();
    }

    @GetMapping("/browse")
    public ModelAndView browse(@Nullable @RequestParam("search") String search)
    {
        return apiHandler.browse(search);
    }

    @GetMapping("/edit")
    public ModelAndView edit(@Nullable @RequestParam("search") String search)
    {
        return apiHandler.edit(search);
    }

    @PostMapping("/delete:{movieId}")
    public String delete(@PathVariable int movieId)
    {
        return apiHandler.delete(movieId);
    }

    @GetMapping("/view:{movieId}")
    public ModelAndView view(@PathVariable int movieId)
    {
        return apiHandler.view(movieId);
    }

    @PostMapping("/rate:{movieId}")
    public String rate(@PathVariable int movieId, @RequestParam("score") int score)
    {
        return apiHandler.rate(movieId, score);
    }

    @PostMapping("/comment:{movieId}")
    public String comment(@PathVariable int movieId, @RequestParam("body") String body)
    {
        return apiHandler.comment(movieId, body);
    }

    @GetMapping("/review:{movieId}")
    public ModelAndView review(@PathVariable int movieId)
    {
        return apiHandler.review(movieId);
    }
}
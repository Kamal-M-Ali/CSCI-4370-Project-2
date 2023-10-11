package uga.cs4370.movieproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.text.NumberFormat;

@Controller
@RequestMapping("dynamic")
public class WebController {
    @GetMapping("/")
    public ModelAndView browse()
    {
        ModelAndView mv = new ModelAndView("index");
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        if (databaseConnection != null)
            if (databaseConnection.query("SELECT COUNT(DISTINCT movieId) AS totalMovies FROM Movies;", (ResultSet rs) -> {
                if (rs.next()) {
                    String total = NumberFormat.getInstance().format(rs.getInt("totalMovies"));
                    mv.addObject(
                            "totalMovies",
                             total + " movies on the site!");
                }
            })) return mv;

        mv.addObject("totalMovies", "");
        return mv;
    }

}
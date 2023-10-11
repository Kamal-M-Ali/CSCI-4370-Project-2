package uga.cs4370.movieproject;

import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.cs4370.movieproject.model.Movie;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("dynamic")
public class WebController {
    // defining constants for the SQL statements
    private static final String GET_MOVIE_COUNT =
            "SELECT COUNT(DISTINCT movieId) AS totalMovies " +
            "FROM Movies;";
    private static final String GET_ALL_MOVIES =
            "SELECT movieId, title, tagline, voteAverage " +
            "FROM Movies;";

    private static final String GET_MOVIE_SUBSET =
            "SELECT movieId, title, tagline, voteAverage " +
            "FROM Movies " +
            "WHERE title LIKE '%%%s%%'";

    private static final String GET_MOVIE =
            "SELECT * " +
            "FROM Movies " +
            "WHERE movieId=%d";

    @GetMapping("/")
    public ModelAndView index()
    {
        ModelAndView mv = new ModelAndView("index");
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        if (databaseConnection != null)
            if (databaseConnection.query(GET_MOVIE_COUNT, (ResultSet rs) -> {
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

    @GetMapping("/browse")
    public ModelAndView browse(@Nullable @RequestParam("search") String search)
    {
        ModelAndView mv = new ModelAndView("browse");
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        List<Movie> movies = new ArrayList<>();
        if (databaseConnection != null) {
            String statement = search != null ? String.format(GET_MOVIE_SUBSET, search) : GET_ALL_MOVIES;
            databaseConnection.query(statement, (ResultSet rs) -> {
                while (rs.next()) {
                    movies.add(new Movie(
                            rs.getInt("movieId"),
                            rs.getString("title"),
                            rs.getDouble("voteAverage")
                    ));
                }
            });
        }

        mv.addObject("movies", movies);
        return mv;
    }

    @GetMapping("/view:{movieId}")
    public ModelAndView view(@PathVariable int movieId)
    {
        ModelAndView mv = new ModelAndView("view");
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        if (databaseConnection != null) {
            if (databaseConnection.query(String.format(GET_MOVIE, movieId), (ResultSet rs) -> {
                if (rs.next()) {
                    mv.addObject("title", rs.getString("title"));
                    mv.addObject("tagline", rs.getString("tagline"));
                    mv.addObject("voteAverage", rs.getDouble("voteAverage"));
                    mv.addObject("imdb", rs.getString("imdb"));
                    mv.addObject("overview", rs.getString("overview"));
                    mv.addObject("releaseDate", rs.getDate("releaseDate"));
                    mv.addObject("runtime", rs.getInt("runtime"));
                    mv.addObject("voteCount", NumberFormat.getInstance().format(rs.getInt("voteCount")));
                    mv.addObject("budget", NumberFormat.getInstance().format(rs.getInt("budget")));
                    mv.addObject("revenue", NumberFormat.getInstance().format(rs.getLong("revenue")));


                    mv.addObject("genres", "");
                }
            })) return mv;
        }

        mv.addObject("title", "Failed to fetch movie data");
        mv.addObject("voteAverage", "");
        mv.addObject("tagline", "");
        mv.addObject("imdb", "");
        mv.addObject("overview", "");
        mv.addObject("genres", "");
        mv.addObject("releaseDate", "");
        mv.addObject("runtime", "");
        mv.addObject("voteCount", "");
        mv.addObject("budget", "");
        mv.addObject("revenue", "");

        return mv;
    }

    @GetMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mv = new ModelAndView("edit");

        mv.addObject("totalMovies", "Hello from edit");
        return mv;
    }
}
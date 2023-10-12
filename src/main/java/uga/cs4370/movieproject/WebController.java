package uga.cs4370.movieproject;

import io.micrometer.common.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uga.cs4370.movieproject.model.Comment;
import uga.cs4370.movieproject.model.Movie;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("dynamic")
public class WebController {
    @GetMapping("/")
    public ModelAndView index()
    {
        ModelAndView mv = new ModelAndView("index");
        Database databaseConnection = Database.getInstance();

        if (databaseConnection != null) {
            databaseConnection.query(Database.GET_MOVIE_COUNT, (ResultSet rs) -> {
                if (rs.next()) {
                    String total = NumberFormat.getInstance().format(rs.getInt("totalMovies"));
                    mv.addObject(
                            "totalMovies",
                            total + " movies on the site!");
                }
            });
        }

        return mv;
    }

    @GetMapping("/browse")
    public ModelAndView browse(@Nullable @RequestParam("search") String search)
    {
        ModelAndView mv = new ModelAndView("browse");
        mv.addObject("movies", getMovieList(search));
        return mv;
    }

    @GetMapping("/edit")
    public ModelAndView edit(@Nullable @RequestParam("search") String search)
    {
        ModelAndView mv = new ModelAndView("edit");
        if (search != null)
            mv.addObject("movies", getMovieList(search));
        return mv;
    }

    @PostMapping("/delete:{movieId}")
    public String delete(@PathVariable int movieId)
    {
        Database databaseConnection = Database.getInstance();
        if (databaseConnection != null)
            databaseConnection.query(String.format(Database.DELETE_MOVIE, movieId), null);
        return "redirect:/dynamic/edit";
    }

    @GetMapping("/view:{movieId}")
    public ModelAndView view(@PathVariable int movieId)
    {
        ModelAndView mv = new ModelAndView("view");
        Database databaseConnection = Database.getInstance();

        List<Comment> comments = new ArrayList<Comment>();

        if (databaseConnection != null) {
            if(databaseConnection.query(String.format(Database.GET_MOVIE_WITH_COMMENTS, movieId), (ResultSet rs) -> {
                if (rs.next()) {
                    mv.addObject("title", rs.getString("title"));
                    mv.addObject("tagline", rs.getString("tagline"));
                    mv.addObject("voteAverageFixed", String.format("%.2f",rs.getDouble("voteAverage")));
                    mv.addObject("imdb", rs.getString("imdb"));
                    mv.addObject("overview", rs.getString("overview"));
                    mv.addObject("releaseDate", rs.getDate("releaseDate"));
                    mv.addObject("runtime", rs.getInt("runtime"));
                    mv.addObject("voteCount", NumberFormat.getInstance().format(rs.getInt("voteCount")));
                    mv.addObject("budget", NumberFormat.getInstance().format(rs.getInt("budget")));
                    mv.addObject("revenue", NumberFormat.getInstance().format(rs.getLong("revenue")));
                    mv.addObject("genres", "");

                    // load the comments
                    do {
                        // ensures a comment exists
                        if (rs.getString("body") != null) {
                            comments.add(new Comment(
                                    rs.getInt("userId"),
                                    rs.getString("profileName"),
                                    rs.getString("body"),
                                    rs.getTimestamp("commentedOn")
                            ));
                        }
                    } while (rs.next());
                    if (!comments.isEmpty())
                        mv.addObject("comments", comments);
                }
            })) return mv;
        }

        // error reading data
        mv.addObject("title", "Failed to fetch movie data.");
        mv.addObject("tagline", "Sorry about that");
        mv.addObject("voteAverageFixed", "");
        mv.addObject("imdb", "");
        mv.addObject("overview", "");
        mv.addObject("releaseDate", "");
        mv.addObject("runtime", "");
        mv.addObject("voteCount", "");
        mv.addObject("budget", "");
        mv.addObject("revenue", "");
        mv.addObject("genres", "");

        return mv;
    }

    @PostMapping("/rate:{movieId}")
    public String rate(@PathVariable int movieId, @RequestParam("score") int score)
    {
        List<Movie> movies = new ArrayList<>();
        Database databaseConnection = Database.getInstance();

        if (databaseConnection != null) {
            databaseConnection.query(String.format(Database.GET_MOVIE, movieId), (ResultSet rs) -> {
               if (rs.next()) {
                   movies.add(new Movie(
                           rs.getInt("movieId"),
                           rs.getString("title"),
                           rs.getDouble("voteAverage"),
                           rs.getInt("voteCount")
                   ));
               }
            });

            movies.get(0).rate(score);
            databaseConnection.query(
                    String.format(
                            Database.UPDATE_VOTE, movies.get(0).getVoteAverage(), movies.get(0).getVoteCount(), movieId
                    ),
                    null);
        }

        return "redirect:/dynamic/view:" + movieId;
    }

    private List<Movie> getMovieList(String search)
    {
        Database databaseConnection = Database.getInstance();

        List<Movie> movies = new ArrayList<>();
        if (databaseConnection != null) {
            String statement = search != null ? String.format(Database.GET_MOVIE_SUBSET, search) : Database.GET_ALL_MOVIES;
            databaseConnection.query(statement, (ResultSet rs) -> {
                while (rs.next()) {
                    movies.add(new Movie(
                            rs.getInt("movieId"),
                            rs.getString("title"),
                            rs.getDouble("voteAverage"),
                            rs.getInt("voteCount")
                    ));
                }
            });
        }

        return movies;
    }
}
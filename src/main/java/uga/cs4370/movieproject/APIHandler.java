package uga.cs4370.movieproject;

import org.springframework.web.servlet.ModelAndView;
import uga.cs4370.movieproject.model.Comment;
import uga.cs4370.movieproject.model.Movie;
import uga.cs4370.movieproject.model.Review;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class APIHandler {
    public ModelAndView index()
    {
        ModelAndView mv = new ModelAndView("index");
        Database conn = Database.getInstance();

        if (conn != null) {
            conn.query(Database.GET_MOVIE_COUNT, (ResultSet rs) -> {
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

    public ModelAndView browse(String search)
    {
        ModelAndView mv = new ModelAndView("browse");
        mv.addObject("movies", getMovieList(search));
        return mv;
    }

    public ModelAndView edit(String search)
    {
        ModelAndView mv = new ModelAndView("edit");
        if (search != null)
            mv.addObject("movies", getMovieList(search));
        return mv;
    }

    public String delete(int movieId)
    {
        Database conn = Database.getInstance();
        if (conn != null)
            conn.query(String.format(Database.DELETE_MOVIE, movieId), null);
        return "redirect:/dynamic/edit";
    }

    public ModelAndView view(int movieId)
    {
        ModelAndView mv = new ModelAndView("view");
        Database conn = Database.getInstance();
        List<Comment> comments = new ArrayList<Comment>();

        if (conn != null) {
            if(conn.query(String.format(Database.GET_MOVIE_WITH_COMMENTS, movieId), (ResultSet rs) -> {
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

    public String rate(int movieId, int score)
    {
        List<Movie> movies = new ArrayList<>();
        Database conn = Database.getInstance();

        if (conn != null) {
            conn.query(String.format(Database.GET_MOVIE, movieId), (ResultSet rs) -> {
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
            conn.query(
                    String.format(
                            Database.UPDATE_VOTE, movies.get(0).getVoteAverage(), movies.get(0).getVoteCount(), movieId
                    ),
                    null);
        }

        return "redirect:/dynamic/view:" + movieId;
    }

    public String comment(int movieId, String body)
    {
        Database conn = Database.getInstance();

        if (conn != null) {
            String timestamp = new Timestamp((new Date()).getTime()).toString();
            // using 1 as the user id=anonymous (since no login functionality)
            conn.query(String.format(Database.ADD_COMMENT, body, timestamp, movieId, 1), null);
        }

        return "redirect:/dynamic/view:" + movieId;
    }

    public ModelAndView review(int movieId)
    {
        ModelAndView mv = new ModelAndView("review");
        List<Review> reviews = new ArrayList<>();
        Database conn = Database.getInstance();

        if (conn != null) {
            if (conn.query(String.format(Database.GET_MOVIE_REVIEWS, movieId), (ResultSet rs) -> {
                if (rs.next()) {
                    mv.addObject("title", rs.getString("title"));

                    // load the comments
                    do {
                        // ensures a comment exists
                        if (rs.getString("body") != null) {
                            reviews.add(new Review(
                                    rs.getInt("userId"),
                                    rs.getString("profileName"),
                                    rs.getString("body"),
                                    rs.getInt("score"),
                                    rs.getTimestamp("reviewedOn")
                            ));
                        }
                    } while (rs.next());
                    if (!reviews.isEmpty())
                        mv.addObject("reviews", reviews);
                }
            })) return mv;
        }

        // error reading data
        mv.addObject("title", "Failed to fetch movie data.");
        return mv;
    }

    private List<Movie> getMovieList(String search)
    {
        Database conn = Database.getInstance();

        List<Movie> movies = new ArrayList<>();
        if (conn != null) {
            String statement = search != null ? String.format(Database.GET_MOVIE_SUBSET, search) : Database.GET_ALL_MOVIES;
            conn.query(statement, (ResultSet rs) -> {
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
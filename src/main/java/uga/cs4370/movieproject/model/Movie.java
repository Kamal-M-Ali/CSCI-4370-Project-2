package uga.cs4370.movieproject.model;

import java.util.Date;

public class Movie {
    private int movieId;
    private String title;
    private double voteAverage;

    public Movie()
    {
        this.movieId = 0;
        this.title = null;
        this.voteAverage = 0.0;
    }

    public Movie(int movieId, String title, double voteAverage)
    {
        this.movieId = movieId;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public int getMovieId() { return movieId; }
}

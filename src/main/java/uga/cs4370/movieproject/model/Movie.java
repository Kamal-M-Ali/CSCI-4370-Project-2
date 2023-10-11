package uga.cs4370.movieproject.model;

import java.util.Date;

public class Movie {
    private int movieId;
    private String title;
    private String tagline;
    private double voteAverage;

    public Movie()
    {
        this.movieId = 0;
        this.title = null;
        this.tagline = null;
        this.voteAverage = 0.0;
    }

    public Movie(int movieId, String title, String tagline, double voteAverage)
    {
        this.movieId = movieId;
        this.title = title;
        this.tagline = tagline;
        this.voteAverage = voteAverage;
    }

    public int getMovieId() { return movieId; }
}

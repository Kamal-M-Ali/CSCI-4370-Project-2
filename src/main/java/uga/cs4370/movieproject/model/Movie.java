package uga.cs4370.movieproject.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class Movie {
    private int movieId;
    private String title;
    private String voteAverageFixed;
    private double voteAverage;
    private int voteCount;

    public Movie()
    {
        this.movieId = 0;
        this.title = null;
        this.voteAverageFixed = "";
        this.voteAverage = 0.0;
        this.voteCount = 0;
    }

    public Movie(int movieId, String title, double voteAverage, int voteCount)
    {
        this.movieId = movieId;
        this.title = title;
        this.voteAverageFixed = String.format("%.2f", voteAverage);
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public int getMovieId() { return movieId; }
    public double getVoteAverage() { return voteAverage; }
    public int getVoteCount() { return voteCount; }

    public void rate(int score)
    {
        ++voteCount;
        voteAverage = ((voteCount - 1) * voteAverage + score) / voteCount;
        voteAverageFixed = String.format("%.2f", voteAverage);
    }
}

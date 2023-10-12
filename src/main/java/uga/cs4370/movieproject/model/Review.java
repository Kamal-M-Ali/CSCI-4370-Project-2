package uga.cs4370.movieproject.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

public class Review {
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT);
    private int userId;
    private String profileName;
    private String body;
    private int score;
    private String reviewedOn;

    public Review()
    {
        this.userId = 0;
        this.profileName = null;
        this.body = null;
        this.score = 0;
        this.reviewedOn = null;
    }

    public Review(int userId, String profileName, String body, int score, Timestamp reviewedOn)
    {
        this.userId = userId;
        this.profileName = profileName;
        this.body = body;
        this.score = score;
        this.reviewedOn = DATE_FORMATTER.format(new Date(reviewedOn.getTime()));
    }
}

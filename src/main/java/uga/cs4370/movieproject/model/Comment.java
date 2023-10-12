package uga.cs4370.movieproject.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT);
    private int userId;
    private String profileName;
    private String body;
    private String commentedOn;


    public Comment()
    {
        this.userId = 0;
        this.profileName = null;
        this.body = null;
        this.commentedOn = null;
    }

    public Comment(int userId, String profileName, String body, Timestamp commentedOn)
    {
        this.userId = userId;
        this.profileName = profileName;
        this.body = body;
        this.commentedOn = DATE_FORMATTER.format(new Date(commentedOn.getTime()));
    }
}

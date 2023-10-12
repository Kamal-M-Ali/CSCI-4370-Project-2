package uga.cs4370.movieproject;

import org.apache.tomcat.util.json.ParseException;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A singleton class for accessing the MySql database.
 * Use the {@link Database#getInstance()} method to get the object.
 */
public class Database {
    // defining constants for the SQL statements
    public static final String GET_MOVIE_COUNT =
            "SELECT COUNT(DISTINCT movieId) AS totalMovies " +
            "FROM Movies;";
    public static final String GET_ALL_MOVIES =
            "SELECT movieId, title, voteAverage, voteCount " +
            "FROM Movies;";
    public static final String GET_MOVIE_SUBSET =
            "SELECT movieId, title, voteAverage, voteCount " +
            "FROM Movies " +
            "WHERE title LIKE '%%%s%%'";
    public static final String GET_MOVIE_WITH_COMMENTS =
            "SELECT * FROM " +
            "Movies NATURAL LEFT OUTER JOIN Comments NATURAL LEFT OUTER JOIN Users " +
            "WHERE movieId=%d";
    public static final String GET_MOVIE =
            "SELECT * " +
            "FROM Movies " +
            "WHERE movieId=%d";
    public static final String DELETE_MOVIE =
            "DELETE FROM Movies " +
            "WHERE movieId=%d";
    public static final String UPDATE_VOTE =
            "UPDATE Movies " +
            "SET voteAverage=%f, voteCount=%d " +
            "WHERE movieId=%d";
    public static final String ADD_COMMENT =
            "INSERT INTO Comments(body,commentedOn,movieId,userId) " +
            "VALUES ('%s','%s',%d,%d);";
    public static final String GET_MOVIE_REVIEWS =
            "SELECT title, userId, profileName, body, score, reviewedOn " +
            "FROM Movies NATURAL JOIN Reviews NATURAL JOIN Users " +
            "WHERE movieId=%d";
    public static final String GET_USER_REVIEWS =
            "SELECT title, userId, profileName, body, score, reviewedOn " +
            "FROM Users NATURAL JOIN Reviews NATURAL JOIN Movies " +
            "WHERE userId=%d " +
            "ORDER BY reviewedOn DESC;";
    public static final String GET_USER_COMMENTS =
            "SELECT title, userId, profileName, body, commentedOn " +
            "FROM Users NATURAL JOIN Comments NATURAL JOIN Movies " +
            "WHERE userId=%d " +
            "ORDER BY commentedOn DESC";
    public static final String GET_USER_PROFILE = "SELECT title, userId, profileName, %d";

    private static final String KEY = "jdbc:mysql://localhost:33306/project2?user=root&password=mysqlpass";
    private static Database instance;
    private Connection connection;

    private Database() throws SQLException
    {
        this.connection = DriverManager.getConnection(KEY);
    }

    /**
     * Will create and return a DatabaseConnection object if one does not exist, otherwise it will return the existing
     * object.
     * @return the {@link Database} object
     */
    public static Database getInstance()
    {
        if (instance == null) {
            try {
                instance = new Database();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }

        return instance;
    }

    /**
     * If a callback is provided will executeQuery() for the statement and call the callback on the resulting set.
     * Otherwise, if the callback is null it will just execute() the statement.
     * @param statement the sql statement to execute
     * @param callback if not null, will call the callback function on the set resulting from the query
     * @return true if query was successful, false otherwise
     */
    public boolean query(String statement, Callback callback)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            if (callback != null) {
                rs = stmt.executeQuery(statement);
                callback.fn(rs);
            } else {
                stmt.execute(statement);
            }
        } catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        } catch (ParseException e) {
            System.out.println("JSON Exception: " + e.getMessage());
            return false;
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore

                stmt = null;
            }
        }
        return true;
    }
}

package uga.cs4370.movieproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;

import java.sql.*;

/**
 * A singleton class for accessing the MySql database.
 * Use the {@link DatabaseConnection#getInstance()} method to get the object.
 */
public class DatabaseConnection {

    private static String KEY = "jdbc:mysql://localhost:33306/project2?user=root&password=mysqlpass";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException
    {
        this.connection = DriverManager.getConnection(KEY);
    }

    /**
     * Will create and return a DatabaseConnection object if one does not exist, otherwise it will return the existing
     * object.
     * @return the {@link DatabaseConnection} object
     */
    public static DatabaseConnection getInstance()
    {
        if (instance == null) {
            try {
                instance = new DatabaseConnection();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }

        return instance;
    }

    /**
     * Will attempt to execute the given SQL statement on the database.
     * TODO: Update method to make more sense in the context of the project. Right now it's basically a copy pasted
     *       example from eLC.
     * @param statement the sql statement to execute
     * @param callback will call the callback function on the set resulting from the query
     * @return true if query was successful, false otherwise
     */
    public boolean query(String statement, Callback callback)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(statement);
            callback.fn(rs);
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

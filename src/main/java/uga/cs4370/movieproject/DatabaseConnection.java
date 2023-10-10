package uga.cs4370.movieproject;

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
     * @return true if query was successful, false otherwise
     */
    public boolean query(String statement)
    {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(statement);

            int colCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for(int i = 1; i <= colCount; i++)
                    System.out.print(rs.getString(i) + "\t");
                System.out.println();
            }
        }
        catch (SQLException e){
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        finally {
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

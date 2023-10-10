package uga.cs4370.movieproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Boilerplate code taken from https://dev.mysql.com/doc/connector-j/8.1/en/connector-j-usagenotes-basic.html
 * and modified by the instructor of the course (Sami Menik).
 */
@SpringBootApplication
public class App
{
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);

        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.query("SELECT * FROM Users;");
    }
}

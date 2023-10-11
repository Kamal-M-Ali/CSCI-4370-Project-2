package uga.cs4370.movieproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class App
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

package uga.cs4370.movieproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.json.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback {
    public void fn(ResultSet set) throws SQLException, ParseException;
}

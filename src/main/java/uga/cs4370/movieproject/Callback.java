package uga.cs4370.movieproject;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback {
    public void fn(ResultSet set) throws SQLException;
}

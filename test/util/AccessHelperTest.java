package util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class AccessHelperTest {

    @Test
    public void getConnection() {
       AccessHelper.getConnection();
       System.out.println("Done");
    }

    @Test
    public void getResultSet() throws SQLException {
        String sql = "select * from test";
        ResultSet rs = AccessHelper.getResultSet(sql);
        while (rs.next()) {
            System.out.println(rs.getString("FLIGHTID"));
        }
    }
}
package util;

import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SimpleTimeZone;
import java.util.logging.*;

import static org.junit.Assert.*;

public class AccessHelperTest {



    @Test
    public void getConnection() {
       AccessHelper.getConnection();
       System.out.println("Done");
    }

    @Test
    public void getResultSet() throws SQLException {
        String time = "20180601";
        ResultSet rs = AccessHelper.getResultSet(time);
        int i = 0;
        while (rs.next()) {
            i++;

            System.out.println(rs.getString("FLIGHTID"));
            if (i ==20) {
                break;
            }
        }
    }


}
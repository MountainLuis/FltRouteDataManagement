package util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class MysqlHelperNewTest {

    @Test
    public void getConnection() {
        Connection conn  = MysqlHelperNew.getConnection();
        System.out.println("Done");
    }

    @Test
    public void getResultSet() {
        String sql = "select * from route where id < 3";
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()){
                System.out.println(rs.getString("route_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
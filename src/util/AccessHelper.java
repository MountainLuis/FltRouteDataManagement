package util;

import java.sql.*;
import java.util.List;

public class AccessHelper {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//            String url = "jdbc:ucanaccess://d://data//fme201806.accdb";
            String url = "jdbc:ucanaccess://d://test.accdb";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static ResultSet getResultSet(String time) {
//        String sql = "select * from fme where P_DEPTIME like'" + time +"%'";
        String sql = "select * from test where P_DEPTIME like'" + time +"%'";
        ResultSet rs = null;
        Connection conn = getConnection();
        if (conn == null) {
            return  null;
        }
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

}

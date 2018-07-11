package util;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * 此程序用在DealWithNewNAIPData程序中，用于读写一个新的数据库。
 */
public class MysqlHelperNew {
    public static final String url = "jdbc:mysql://localhost:3306/airspace_2018";
    public static final String username = "root";
    public static final String password = "123456";
    private static Connection conn = null;

    public static Connection getConnection(){
        if(conn != null)
            return conn;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static ResultSet getResultSet(String sql){
        ResultSet rs = null;

        Connection conn = getConnection();
        if(conn == null)
            return null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
    public static void insertIntoDB(Map<String, List<String>> routes) {
        String sql = "INSERT INTO route_naip_copy (route,fix_pt,seq) VALUES (?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int idx = 0;
            String tmp = null;
            for (String r : routes.keySet()) {
                for (int i = 0; i < routes.get(r).size(); i++) {
                    String pt = routes.get(r).get(i);
                    pstmt.setString(1, r);
                    pstmt.setString(2, pt);
                    pstmt.setInt(3, i);
                    pstmt.addBatch();
                    idx++;
                    if (idx % 1000 == 0) {
                        pstmt.executeBatch();
                    }
                }
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

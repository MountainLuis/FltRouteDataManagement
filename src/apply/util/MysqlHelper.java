package apply.util;

import code.bada.bean.AcftState;

import java.sql.*;
import java.util.List;

public class MysqlHelper {
//    public static final String URL = "jdbc:mysql://localhost:3306/mabfs_test";
    public static final String URL = "jdbc:mysql://localhost:3306/test";
    public static final String USER = "root";
    public static final String PASSWD = "root";
//    public static final String PASSWD = "kos084713";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return conn;
    }
    public static ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        Connection conn = getConnection();
        if (conn == null) {
            return null;
        }
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void insert(List<AcftState> list, String fltNo) {
        String sql = " INSERT INTO flight_track (FLTNO, ALTITUDE, HEADING, LONGTITUDE, LATITUDE, CAS, TAS, TIME) VALUES" +
                "(?,?,?,?,?,?,?,?)";
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(AcftState state: list){
                pstmt.setString(1, fltNo);
                pstmt.setDouble(2, state.pAltitude);
                pstmt.setDouble(3, state.heading);
                pstmt.setDouble(4, state.longitude);
                pstmt.setDouble(5, state.latitude);
                pstmt.setDouble(6, state.CAS);
                pstmt.setDouble(7, state.TAS);
                pstmt.setLong(8, state.time);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(StringBuffer suffix) {
        conn = getConnection();
        String prefix = " INSERT INTO flight_track (FLTNO, ALTITUDE, HEADING, LONGTITUDE, LATITUDE, CAS, TAS, TIME) VALUES";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = prefix + suffix.substring(0, suffix.length() - 1);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                  stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
      // 下面这个方法有问题，已经拼好sql的情况下，直接执行就好，不需要addbatch。
    public static void insert2() {
//        // 开时时间
//        Long begin = System.currentTimeMillis();
//        conn = getConnection();
//        // sql前缀
//        String prefix = "INSERT INTO tb_big_data (count, create_time, random) VALUES ";
//        try {
//            // 保存sql后缀
//            StringBuffer suffix = new StringBuffer();
//            // 设置事务为非自动提交
//            conn.setAutoCommit(false);
//            // Statement st = conn.createStatement();
//            // 比起st，pst会更好些
//            PreparedStatement pst = conn.prepareStatement("");
//            // 外层循环，总提交事务次数
//            for (int i = 1; i <= 100; i++) {
//                // 第次提交步长
//                for (int j = 1; j <= 10000; j++) {
//                    // 构建sql后缀
//                    suffix.append("(" + j * i + "," + "'HELLO'" + "," + i * j * Math.random() + "),");
//                }
//                // 构建完整sql
//                String sql = prefix + suffix.substring(0, suffix.length() - 1);
//                // 添加执行sql
//                pst.addBatch(sql);
//                // 执行操作
//                pst.executeBatch();
//                // 提交事务
//                conn.commit();
//                // 清空上一次添加的数据
//                suffix = new StringBuffer();
//            }
//            // 头等连接
//            pst.close();
////            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        // 结束时间
//        Long end = System.currentTimeMillis();
//        // 耗时
//        System.out.println("cast : " + (end - begin) / 1000 + " ms");
    }
}

package util;

import bean.FltPlan;
import bean.Point;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * 此程序用在DealWithNewNAIPData程序中，用于读写一个新的数据库。
 */
public class MysqlHelperNew {
    public static final String url = "jdbc:mysql://localhost:3306/test";
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
    public static void insert2NameIntoDB(Map<String, List<Point>> routes) {
        String sql = "INSERT INTO route_naip_fullname (route,fix_pt,pt_name,seq) VALUES (?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int idx = 0;
            String tmp = null;
            for (String r : routes.keySet()) {
                for (int i = 0; i < routes.get(r).size(); i++) {
                    Point pt = routes.get(r).get(i);
                    pstmt.setString(1, r);
                    pstmt.setString(2, pt.pid);
                    pstmt.setString(3, pt.name);
                    pstmt.setInt(4, i);
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
    public static void createPointTable(String table) {
        String  pointSql = "CREATE TABLE " + table + " (ID int(10)," +
                "FixPt_ID varchar(225), FixPt_lat varchar(225), FixPt_Long varchar(225), " +
                "Seg_Dist varchar(20), Height_Cons varchar(40), Speed_Cons varchar(40), Heading varchar(40), IsTurnPt varchar(40)," +
                "Point_Seq int(10),FLT_PATH varchar(225)" +
                ")charset=utf8;";
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            if(stmt.executeUpdate(pointSql) != -1) {
                System.out.println("新建表完成" + table);
            } else{
                System.out.println("执行失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createPlanTable(String table) {
        String  planSql = "CREATE TABLE " + table + " (ID int(10)," +
                "FLT_NO varchar(10), REGISTRA_NUM varchar(255), ACFT_TYPE varchar(20)," +
                "ACFT_CLASS varchar(20)," +
                "TO_AIP varchar(20), LD_AIP varchar(10),APPEAR_TIME varchar(20), " +
                "START_POINT varchar(20), END_POINT varchar(20), " +
                "FLT_PATH longtext, " +
                "SID_PATH varchar(40), STAR_PATH varchar(40), TO_RUNWAY varchar(20), LD_RUNWAY varchar(20)" +
                ")charset=utf8;";
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            if(stmt.executeUpdate(planSql) != -1) {
                System.out.println("新建表完成" + table);
            } else{
                System.out.println("执行失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertFltPlanList(String tableName, List<FltPlan> plans) {
        String key = "MySQL";
        String sql = "INSERT INTO " + tableName + " (" +
                "FLT_NO, REGISTRA_NUM, ACFT_TYPE, TO_AIP, LD_AIP, APPEAR_TIME, FLT_PATH)" +
                "VALUES (?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < plans.size(); i++) {
                FltPlan p = plans.get(i);
                pstmt.setString(1,p.flt_no);
                pstmt.setString(2,p.regitration_num);
                pstmt.setString(3,p.acft_type);
                pstmt.setString(4,p.to_ap);
                pstmt.setString(5,p.ld_ap);
                pstmt.setString(6,p.dep_time);
                pstmt.setString(7,p.flt_path);
                pstmt.addBatch();
                if (i % 1000 == 0){
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            pstmt.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertODPoints(String table, Map<String, List<Point>> routes) {
        String sql = "INSERT INTO " + table + " (FixPt_ID,FixPt_Lat,FixPt_Long,Point_Seq, FLT_PATH) VALUES (?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int idx = 0;
            String tmp = null;
            for (String r : routes.keySet()) {
                if (routes.get(r).isEmpty() || routes.get(r) == null) {
                    System.out.println("查无此路" + r);
                    continue;
                }
                for (int i = 0; i < routes.get(r).size(); i++) {
                    Point pt = routes.get(r).get(i);
                    pstmt.setString(1, pt.pid);
                    String lat0 = String.valueOf((int)pt.latitude);
                    pstmt.setString(2, lat0);
                    String lng0 = String.valueOf((int)pt.longitude);
                    pstmt.setString(3, lng0);
                    pstmt.setInt(4, i);
                    pstmt.setString(5,r);
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

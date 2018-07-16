package util;

import bean.FltPlan;
import bean.Point;
import bean.PointInfo;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class JDBCHelper {
    public static Connection getConnection(String key){
        Connection conn = null;
        if(conn != null)
            return conn;
        try{
            Class.forName(DBPropertiesUtil.getDriverProperties(key));
            conn = DriverManager.getConnection(DBPropertiesUtil.getUrlProperties(key),
                    DBPropertiesUtil.getUsernameProperties(key), DBPropertiesUtil.getPasswordProperties(key));
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
    public static ResultSet getResultSet(String table, String key){
         String sql = "select * from " + table ;
        ResultSet rs = null;
        Connection conn = getConnection(key);
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
    public static ResultSet getResultSetWithSql(String sql, String key) {
        ResultSet rs = null;
        Connection conn = getConnection(key);
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
    /**
     * 创建指定的表
     * @param table 表名称
     * @param c 表类型，p：flightplan; r：route; t ：point;
     */
    public static void createTable(String table, char c) {
        String  planSql = "CREATE TABLE " + table + "(id int(10)," +
                "flt_no varchar(10), registration_num varchar(255), acft_type varchar(20)," +
                "to_ap varchar(10), ld_ap varchar(10),dep_time varchar(20),arr_time varchar(20), flt_path longtext" +
                ")charset=utf8;";
        String  routeSql = "CREATE TABLE " + table + "(id int(10)," +
                "fix_pt varchar(10), seq int(10), path varchar(20)" +
                 ")charset=utf8;";
        String  pointSql = "CREATE TABLE " + table + "(id int(10)," +
                "pid varchar(10), name varchar(20), latitude double, longitude double" +
                 ")charset=utf8;";
        switch (c) {
            case 'p':
                create(planSql);
                break;
            case 'r':
                create(routeSql);
                break;
            case 't':
                create(pointSql);
                break;
                default:
                    System.out.println("表类型有误。");
        }

    }
    private static void create(String sql) {
        String key = "MySQL";
        Connection conn = getConnection(key);
        try {
            Statement stmt = conn.createStatement();
            if(stmt.executeUpdate(sql) != -1) {
                System.out.println("新建表完成");
            } else{
                System.out.println("执行失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param tableName
     * @param plans
     */
    public static void insertFltPlanList(String tableName, List<FltPlan> plans) {
        String key = "MySQL";
         String sql = "INSERT INTO " + tableName + " (" +
                "flt_no, registration_num, acft_type, to_ap, ld_ap, dep_time, arr_time, flt_path)" +
                "VALUES (?,?,?,?,?,?,?,?)";
        Connection conn = getConnection(key);
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
                pstmt.setString(7,p.arr_time);
                pstmt.setString(8,p.flt_path);
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
    public static void insertFltRouteMap(String tablename, Map<String, List<PointInfo>> pathMap) {
        String key = "MySQL";
        String sql = "insert into " + tablename + "(fix_pt, seq, path) values (?,?,?)";
        Connection conn = getConnection(key);
        try {
            int i = 0;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (String p : pathMap.keySet()) {
                for (int j = 0; j < pathMap.get(p).size(); j++) {
                    PointInfo pi = pathMap.get(p).get(j);
                    i++;
                    pstmt.setString(1,pi.fix_pt);
                    pstmt.setInt(2,j);
                    pstmt.setString(3,p);
                    pstmt.addBatch();
                    if (i % 2000 == 0) {
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
    public static void insertPointList(String tableName, List<Point> pointList) {
        String key = "MySQL";
        String sql = "INSERT INTO " + tableName + " (" +
                "pid, name, latitude, longitude) VALUES (?,?,?,?)";
        Connection conn = getConnection(key);
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < pointList.size(); i++) {
                Point p = pointList.get(i);
                pstmt.setString(1,p.pid);
                pstmt.setString(2,p.name);
                pstmt.setDouble(3,p.latitude);
                pstmt.setDouble(4,p.longitude);
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
}

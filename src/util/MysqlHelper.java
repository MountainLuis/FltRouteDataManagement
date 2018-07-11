package util;

import bean.PointInfo;
import bean.RoutePoint;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class MysqlHelper {
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

    /**
     * insert route points.
     * @param pList
     */
    public static void insertIntoMysql(List<PointInfo> pList) {
        String sql = "insert into route_all (fix_pt,longitude,latitude,route,seq) values (?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int i = 0;
            for (PointInfo pi : pList) {
                i++;
                pstmt.setString(1,pi.fix_pt);
                pstmt.setDouble(2,pi.longitude);
                pstmt.setDouble(3,pi.latitude);
                pstmt.setString(4,pi.enRoute);
                pstmt.setInt(5,pi.idx);
                pstmt.addBatch();
                if (i % 2000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert flight path points.
     * @param pathMap
     */
    public static void insertFltPathIntoMysql(Map<String, List<PointInfo>> pathMap) {
        String sql = "insert into flt_path_China_2018 "
                + "(fix_pt, longitude, latitude, height_cons, speed_cons, seq, flt_path) values (?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            int i = 0;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (String p : pathMap.keySet()) {
                for (int j = 0; j < pathMap.get(p).size(); j++) {
                    PointInfo pi = pathMap.get(p).get(j);
                    i++;
                    pstmt.setString(1,pi.fix_pt);
                    pstmt.setDouble(2,pi.longitude);
                    pstmt.setDouble(3,pi.latitude);
                    pstmt.setString(4,pi.height_cons);
                    pstmt.setString(5,pi.speed_cons);
                    pstmt.setInt(6,j);
                    pstmt.setString(7,p);
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
    public static void insertFltPathIntoMysqlSingle(String route, List<PointInfo> pathList) {
        String sql = "insert into flt_path_China_2018 "
                + "(fix_pt, longitude, latitude, height_cons, speed_cons, seq, flt_path) values (?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
                for (int j = 0; j < pathList.size(); j++) {
                    PointInfo pi = pathList.get(j);
                    pstmt.setString(1,pi.fix_pt);
                    pstmt.setDouble(2,pi.longitude);
                    pstmt.setDouble(3,pi.latitude);
                    pstmt.setString(4,pi.height_cons);
                    pstmt.setString(5,pi.speed_cons);
                    pstmt.setInt(6,j);
                    pstmt.setString(7,route);
                    pstmt.addBatch();
                }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertIntoSimplePath(String od, String path) {
        String sql = "INSERT INTO simple_path_2018 (od, path) VALUES (" +
                od + "," + path +")";
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
    public static void insertIntoSimplePathWithStringArrayList(List<String[]> strList) {
        String sql = "INSERT INTO simple_path_2018 (od, path) VALUES (?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < strList.size(); i++) {
                String[] tmp = strList.get(i);
                pstmt.setString(1,tmp[0]);
                pstmt.setString(2,tmp[1]);
                pstmt.addBatch();
                if (i % 2000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
    public static void insertNaipInfoIntoDB(List<RoutePoint> pList) {
        String sql = "INSERT INTO naip_route (fix_pt, seq, route) VALUES (?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int i = 0, j = 0;
            String r = null;
            for (RoutePoint p : pList) {
                if (!p.route.equals(r)) {
                    i = 0;
                }
                pstmt.setString(1,p.pt);
                pstmt.setInt(2,i);
                pstmt.setString(3,p.route);
                pstmt.addBatch();
                i++; j++;
                r = p.route;
                if (j % 2000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

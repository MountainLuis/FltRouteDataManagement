package util;

import bean.FltPlan;
import bean.PointInfo;
import bean.RoutePoint;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class MysqlHelper {
    public static final String url = "jdbc:mysql://localhost:3306/flightplan";
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

    /**
     * 读取表
     * @param table
     * @return
     */
    public static ResultSet getResultSet(String table){
        String sql = "select * from " + table;
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
     * 创建表
     * @param tableName
     */
    public static void createTable(String tableName) {
        String  sql = "CREATE TABLE " + tableName + "(id int(10)," +
                "flt_no varchar(10), registration_num varchar(20), acft_type varchar(10)," +
                "to_ap varchar(10), ld_ap varchar(10),dep_time varchar(20),arr_time varchar(20), flt_path text" +
                ")charset=utf8;";
        Connection conn = getConnection();
        try {
            Statement stmt = conn.createStatement();
            if(stmt.executeUpdate(sql) != -1) {
                System.out.println("执行完成");
            } else{
                System.out.println("执行失败！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * 向数据库中插入航路和航路点信息；
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
//                pstmt.setDouble(2,pi.longitude);
//                pstmt.setDouble(3,pi.latitude);
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
     * 向数据库中插入城市对路径信息；
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
//                    pstmt.setDouble(2,pi.longitude);
//                    pstmt.setDouble(3,pi.latitude);
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

    /**
     * 向数据库中插入一条城市对的航路点信息；
     * @param route
     * @param pathList
     */
    public static void insertFltPathIntoMysqlSingle(String route, List<PointInfo> pathList) {
        String sql = "insert into flt_path_China_2018 "
                + "(fix_pt, longitude, latitude, height_cons, speed_cons, seq, flt_path) values (?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
                for (int j = 0; j < pathList.size(); j++) {
                    PointInfo pi = pathList.get(j);
                    pstmt.setString(1,pi.fix_pt);
//                    pstmt.setDouble(2,pi.longitude);
//                    pstmt.setDouble(3,pi.latitude);
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

    /**
     * 向数据库中插入城市对航路点信息，航路点用一个字符串表示；插入一条；
     * @param od
     * @param path
     */
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

    /**
     * 向数据库中插入城市对航路点信息，航路点用字符串表达；全部插入；
     * @param strList
     */
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

    /**
     * 将来自网络的NAIP数据插入到数据库中。
     * 因转储结束，为避免重复插入，引用位置将此方法注释掉了。
     * @param pList
     */
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

    /**
     * 向数据库中插入航班计划数据。
     * @param tableName
     */
    public static void insertPlanTable(String tableName, List<FltPlan> plans) {
        long start  = System.currentTimeMillis();
        String sql = "INSERT INTO " + tableName + " (" +
                "flt_no, registration_num, acft_type, to_ap, ld_ap, dep_time, arr_time, flt_path)" +
                "VALUES (?,?,?,?,?,?,?,?)";
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
                pstmt.setString(7,p.arr_time);
                pstmt.setString(8,p.flt_path);
                pstmt.addBatch();
                if (i % 1000 == 0){
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            pstmt.close();
            System.out.println("Insert FltPlan Finished.Used time: " +
                    (System.currentTimeMillis() - start)/1000);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

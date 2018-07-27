package dataDump;
import bean.Point;
import util.JDBCHelper;
import util.MysqlHelperNew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 此程序主要是从NAIP新数据中提取航路经过的航路点名称列表，并转储到数据库中。
 * 数据库有两份，route_naip是航路点字母名称列表，route_naip_copy是航路点全称列表。
 * 两列表的行数相同，但航路排列顺序不同，因Map的keySet是无序的。
 * 11日更新，两表合一。
 */
public class DealWithNewNaipData {
    String[] rePoints = new String[]{"DO","CH","UF","HG","RL","G","YU","PA","CD","QM","WB","KM","FK","FY"};
    public List<String> repeatpoints = Arrays.asList(rePoints);

    Map<String, String> routeInfo = null;
    Map<String, List<RelationInfo>> routeRelation = null;
    Map<String, LegInfo> routeLeg = null;
    public Map<String, Point> pointInfo = null;

    public DealWithNewNaipData() {
        routeInfo = getRouteInfo();
        routeRelation = getRelationInfo();
        routeLeg = getRouteLegInfo();
        pointInfo = getPointInfo();
    }
    public static void main(String[] args) {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
        Map<String,  List<Point>> res = dnnd.dealWithAllData();
        System.out.println(res.size());
        long start = System.currentTimeMillis();
//        MysqlHelperNew.insert2NameIntoDB(res);
        dnnd.storageRoute(res);
        System.out.println("cost time:" + (System.currentTimeMillis() - start)/1000);
    }

    public void storageRoute(Map<String,List<Point>> routeMap) {
    String table = "route_naip";
        JDBCHelper.createTable(table, 'r');
        JDBCHelper.insertRoutePoint(table,routeMap);
    }

    public Map<String,  List<Point>> dealWithAllData() {
        Map<String,  List<Point>> routes = new HashMap<>();
        for (String route : routeInfo.keySet()) {
            List<Point> pList =spliceRouteLeg(routeRelation.get(route));
            routes.put(routeInfo.get(route), pList);
            if (routeInfo.get(route).equals("W66")) {
                System.out.println(routeInfo.get(route) + " " + pList.size() + " : " + routeRelation.get(route).size());
            }
        }
     return routes;
    }
    public List<Point> spliceRouteLeg(List<RelationInfo> legs) {
        List<Point> pList = new ArrayList<>();
        for (int i = 0; i < legs.size();i++) {
            RelationInfo ri = legs.get(i);

            LegInfo li = routeLeg.get(ri.legID);
            Point p0 = null;
            Point p1 = null;
            if (li.startID.equals(ri.startID)) {
                 p0 = pointInfo.get(li.startName);
                 p1 = pointInfo.get(li.endName);
            } else if (li.startID.equals(ri.endID)) {
                 p0 = pointInfo.get(li.endName);
                 p1 = pointInfo.get(li.startName);
            } else {
                System.err.println("Error " + li.legID + " " + ri.routeID);
            }
            pList.add(p0);
            if ( i == legs.size() - 1) {
                pList.add(p1);
            }
         }
        return pList;
    }
    public String transferNameToID(String name) {
        String res = null;
        if (name.contains("-")) {
            if (pointInfo.containsKey(name)) {
//                res = pointInfo.get(name);
            } else {
                res = name.split("-")[0];
            }
        } else if(name.equals("额济纳旗")) {
            res = "JNQ";
        }else {
            res = name;
        }
        return res;
    }

    public Map<String, String> getRouteInfo() {
        String sql = "select * from route";
        Map<String, String> routeInfo = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String num = rs.getString("route_no");
                String id = rs.getString("route_id");
                routeInfo.put(num, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeInfo;
    }
    public Map<String, List<RelationInfo>> getRelationInfo() {
        String sql = "select * from route_relation";
        Map<String, List<RelationInfo>> routeRelation = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try {
            while (rs.next()) {
                String r = rs.getString("route_id");
                if (routeRelation.keySet().contains(r)) {
                    List<RelationInfo> legs = routeRelation.get(r);
                    RelationInfo ri = new RelationInfo();
                    ri.routeID = r;
                    ri.legID = rs.getString("route_leg_id");
                    ri.legSeq = rs.getString("route_leg_seq");
                    ri.startID = rs.getString("start_id");
                    ri.endID = rs.getString("end_id");
                    legs.add(ri);
                    routeRelation.put(r,legs);
                } else {
                    List<RelationInfo> legs = new ArrayList<>();
                    RelationInfo ri = new RelationInfo();
                    ri.routeID = r;
                    ri.legID = rs.getString("route_leg_id");
                    ri.legSeq = rs.getString("route_leg_seq");
                    ri.startID = rs.getString("start_id");
                    ri.endID = rs.getString("end_id");
                    legs.add(ri);
                    routeRelation.put(r,legs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeRelation;
    }
    public Map<String, LegInfo> getRouteLegInfo() {
        String sql = "select * from route_leg";
        Map<String, LegInfo> routeLeg = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String leg = rs.getString("route_leg_id");
                LegInfo li = new LegInfo();
                li.legID = leg;
                li.startName = rs.getString("start_pt_name");
                li.startID = rs.getString("start_pt_id");
                li.endName = rs.getString("end_pt_name");
                li.endID = rs.getString("end_pt_id");
                routeLeg.put(leg, li);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeLeg;
    }
    public Map<String, Point> getPointInfo() {
        String sql = "select * from point_report";
        Map<String, Point> pointInfo = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String name = rs.getString("point_name");
                String pid = rs.getString("point_id");
                String type = rs.getString("point_type");
                double lng = dealWithCoord(rs.getString("longitude"));
                double lat = dealWithCoord(rs.getString("latitude"));
                Point p = new Point();
                 if (pid.equals("")) {
                     p.pid = name;
                     p.name = name;

                } else {
                     p.pid = pid;
                     if (name.equals("额济纳旗")) {
                         p.name = name;
                     } else {
                         p.name = name + "-" + type;
                     }
                }
//                if (pointInfo.containsKey(p.name)) {
//                    System.out.println("----" + p.name);
//                }
                if (repeatpoints.contains(p.pid)) {
                     p.pid = p.pid + "_" + name;
                }
                p.latitude = lat;
                p.longitude = lng;
                pointInfo.put(p.name, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pointInfo;
    }
    public double dealWithCoord(String s) {
        if (s.length() < 3) {
            return -1;
        }
        double res = 0;
        String sub = s.substring(1, s.length());
        String dgr = null;
        String min = null;
        String sec = null;
        if (s.contains("N")) {
            dgr = sub.substring(0,2);
            min = sub.substring(2,4);
            sec = sub.substring(4,sub.length());
        }else if (s.contains("E")) {
            dgr = sub.substring(0,3);
            min = sub.substring(3,5);
            sec = sub.substring(5,sub.length());
        }
        res = Double.parseDouble(dgr) + (Double.parseDouble(min) * 60 + Double.parseDouble(sec))/3600;
        return res;
    }
}
class RelationInfo{
    public String routeID;
    public String legSeq;
    public String legID;
    public String startID;
    public String endID;
}
class LegInfo{
    public String legID;
    public String startName;
    public String startID;
    public  String endName;
    public String endID;
}

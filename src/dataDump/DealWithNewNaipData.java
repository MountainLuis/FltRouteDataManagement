package dataDump;
import bean.Point;
import util.MysqlHelperNew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此程序主要是从NAIP新数据中提取航路经过的航路点名称列表，并转储到数据库中。
 * 数据库有两份，route_naip是航路点字母名称列表，route_naip_copy是航路点全称列表。
 * 两列表的行数相同，但航路排列顺序不同，因Map的keySet是无序的。
 * 11日更新，两表合一。
 */
public class DealWithNewNaipData {
    Map<String, String> routeInfo = null;
    Map<String, List<String>> routeRelation = null;
    Map<String, String[]> routeLeg = null;
    public Map<String, String> pointInfo = null;

    public DealWithNewNaipData() {
        routeInfo = getRouteInfo();
        routeRelation = getRelationInfo();
        routeLeg = getRouteLegInfo();
        pointInfo = getPointInfo();
    }
    public static void main(String[] args) {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
        Map<String,  List<Point>> res = dnnd.dealWithAllData();
        System.out.println("Insert into DB:");
        long start = System.currentTimeMillis();
        MysqlHelperNew.insert2NameIntoDB(res);
        System.out.println("Insert end.");
        System.out.println("cost time:" + (System.currentTimeMillis() - start)/1000);
        dnnd.printMap(res);
    }
    public void printMap(Map<String,  List<Point>> res) {
        for (String r : res.keySet()) {
            for (Point p : res.get(r)) {
                System.out.println(r + " " + p.name + " : " + p.pid);
            }
        }
    }


    public Map<String,  List<Point>> dealWithAllData() {
        Map<String,  List<Point>> routes = new HashMap<>();
        for (String route : routeInfo.keySet()) {
            List<Point> pList =spliceRouteLeg(routeRelation.get(route));
            routes.put(routeInfo.get(route), pList);
        }
     return routes;
    }
    public List<Point> spliceRouteLeg(List<String> legs) {
        List<Point> pList = new ArrayList<>();

        for (String leg : legs) {
            String[] pts = routeLeg.get(leg);
            Point p0 = new Point(pts[0],transferNameToID(pts[0]));
            Point p1 = new Point(pts[1],transferNameToID(pts[1]));
            if (!pList.contains(p0)){
                pList.add(p0);
            }
            if (!pList.contains(p1)) {
                pList.add(p1);
            }
         }
        return pList;
    }
    public String transferNameToID(String name) {
        String res = null;
        if (name.contains("-")) {
            if (pointInfo.containsKey(name)) {
                res = pointInfo.get(name);
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
    public Map<String, List<String>> getRelationInfo() {
        String sql = "select * from route_relation";
        Map<String, List<String>> routeRelation = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try {
            while (rs.next()) {
                String r = rs.getString("route_id");
                if (routeRelation.keySet().contains(r)) {
                    List<String> legs = routeRelation.get(r);
                    String leg = rs.getString("route_leg_id");
                    legs.add(leg);
                    routeRelation.put(r,legs);
                } else {
                    List<String> legs = new ArrayList<>();
                    String leg = rs.getString("route_leg_id");
                    legs.add(leg);
                    routeRelation.put(r,legs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeRelation;
    }
    public Map<String, String[]> getRouteLegInfo() {
        String sql = "select * from route_leg";
        Map<String, String[]> routeLeg = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String leg = rs.getString("route_leg_id");
                String start = rs.getString("start_pt_name");
                String end = rs.getString("end_pt_name");
                String[] res = new String[]{start, end};
                routeLeg.put(leg, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeLeg;
    }
    public Map<String, String> getPointInfo() {
        String sql = "select * from point_report";
        Map<String, String> pointInfo = new HashMap<>();
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String name = rs.getString("point_name");
                String pid = rs.getString("point_id");
                String type = rs.getString("point_type");
                 if (pid.equals("")) {
                     continue;
                } else {
                    name = name + "-" + type;
//                     if (pointInfo.keySet().contains(name)) {
//                         System.out.println(name);
//                     }
                     pointInfo.put(name, pid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pointInfo;
    }

}

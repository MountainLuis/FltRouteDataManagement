package dataDump;

import bean.Point;
import util.MysqlHelperNew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealWithNewNaip {

        Map<String, String> routeInfo = null;
        Map<String, List<String>> routeRelation = null;
        Map<String, String[]> routeLeg = null;
        public Map<String, Point> pointInfo = null;

        public DealWithNewNaip() {
            routeInfo = getRouteInfo();
            routeRelation = getRelationInfo();
            routeLeg = getRouteLegInfo();
            pointInfo = getPointInfo();
        }
    public void dealWithData() {
            for (String r : routeInfo.keySet()) {
                List<String> legs = routeRelation.get(r);
                List<String> pts = getPointID(legs);

            }
    }
    public List<String> getPointID(List<String> legs) {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < legs.size(); i++) {
                String leg = legs.get(i);
                String[] pts = routeLeg.get(leg);
                res.add(pts[0]);
                if (i == legs.size() - 1) {
                    res.add(pts[1]);
                }
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
                String start = rs.getString("start_pt_id");
                String end = rs.getString("end_pt_id");
                String[] res = new String[]{start, end};
                routeLeg.put(leg, res);
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
                String id = rs.getString("id");
                String name = rs.getString("point_name");
                String pid = rs.getString("point_id");
                String type = rs.getString("point_type");
                double lng = dealWithCoord(rs.getString("longitude"));
                double lat = dealWithCoord(rs.getString("latitude"));
                if (pid.equals("")) {
                    Point p = new Point();
                    p.pid = id;
                    p.name = name;
                    p.latitude = lat;
                    p.longitude = lng;
                    pointInfo.put(id, p);
                } else {
                    Point p = new Point();
                    p.pid = id;
                    p.name = pid;
                    p.latitude = lat;
                    p.longitude = lng;
                    pointInfo.put(id, p);
                }
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

package dataDump;

import bean.Point;
import util.JDBCHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DumpRouteMessage {
    private Map<String,List<Point>> routeMap = new HashMap<>();

    public static void main(String[] args) {
        DumpRouteMessage drm = new DumpRouteMessage();
        drm.getRouteMap();
//        drm.storageRouteMap();
        Map<String,List<Point>> routes = drm.reverseRoute();
        drm.storageRouteMap(routes);
        String route = "R200";
        List<Point> res = routes.get(route);
        for (Point p : res) {
            System.out.println(p.pid + " " + p.name + " " + p.seq);
        }

    }
    public Map<String,List<Point>> reverseRoute() {
        Map<String,List<Point>> res = new HashMap<>();
        for (String route : routeMap.keySet()) {
            List<Point> pointList = routeMap.get(route);
            Collections.reverse(pointList);
            res.put(route, pointList);
        }
        return res;
    }
    public void  getRouteMap() {
        String table = "route_naip";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try {
            while (rs.next()) {
                String route = rs.getString("route");
                String fix_pt = rs.getString("fix_pt");
                double lng = rs.getDouble("longitude");
                double lat = rs.getDouble("latitude");
                int seq = rs.getInt("seq") - 1;

                Point p = new Point(fix_pt,lng,lat,route,seq);
                if(route != null && !route.equals("") ){
                    routeMap.computeIfAbsent(route, (String name)->new ArrayList<>()).add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void storageRouteMap(){
        String table = "route_naip_0";
        JDBCHelper.createTable(table,'r');
        JDBCHelper.insertRoutePoint(table,routeMap);
    }

    public void storageRouteMap(Map<String,List<Point>> routes){
        String table = "route_naip_0";
        JDBCHelper.insertRoutePoint(table,routes);
    }

}

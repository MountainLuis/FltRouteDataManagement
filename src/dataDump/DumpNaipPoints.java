package dataDump;

import bean.Point;
import util.JDBCHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DumpNaipPoints {
    public static void main(String[] args) {
        DumpNaipPoints dnp = new DumpNaipPoints();
        List<Point> pointList = dnp.getNaipData();
        dnp.storagePoints(pointList);
    }
    public List<Point> getNaipData() {
        List<Point> pointList = new ArrayList<>();
        String table = "point_report";
        String key = "MySQL";
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try {
            while (rs.next()) {
                Point p = new Point();
                p.longitude = dealWithCoord(rs.getString("longitude"));
                p.latitude = dealWithCoord(rs.getString("latitude"));
                String name = rs.getString("point_name");
                String id = rs.getString("point_id");
                if (id.equals("")) {
                    p.pid = name;
                    p.name = name;
                } else {
                    p.pid = id;
                    p.name = name;
                }
                pointList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pointList;
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
    public void storagePoints(List<Point> points) {
        String table = "allpoint_naip";
        JDBCHelper.createTable(table,'t');
        JDBCHelper.insertPointList(table, points);
        System.out.println("Done.");
    }
}

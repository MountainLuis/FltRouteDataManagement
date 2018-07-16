package util;

import bean.FltPlan;
import bean.Point;
import bean.PointInfo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JDBCHelperTest {

    @Test
    public void getResultSetWithSql() {
        String sql = "select * from test where id < 10";
//        String sql = "select * from plan201806_abroad where id < 10";
//        String key = "MySQL";
        String key = "Access";
        ResultSet rs = JDBCHelper.getResultSetWithSql(sql,key);

        try {
            while (rs.next()) {
                System.out.println(rs.getString("FLIGHTID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getConnection() {
        String key = "MySQL";
        String key2 = "Access";
        Connection conn  = JDBCHelper.getConnection(key2);
        System.out.println("Done");
    }

    @Test
    public void getResultSet() {
//        String table = "route_all";
        String table = "test";
//        String key = "MySQL";
        String key = "Access";
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try{
            int i = 0;
            while (rs.next()) {
                System.out.println(rs.getString("FLIGHTID"));
                if(i++ == 10) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createTable() {
        String table = "test"; //t
        String table2 = "test2";//r
        String table3 = "test3";//p
        JDBCHelper.createTable(table,'t');
        System.out.println("Done");
    }

    @Test
    public void insertFltPlanList() {
        FltPlan p = new FltPlan("CCA1202", "ZBAA","ZUUU");
        List<FltPlan> fltPlanList = new ArrayList<>();
        fltPlanList.add(p);
        JDBCHelper.insertFltPlanList("test3", fltPlanList);
        System.out.println("Done");
    }

    @Test
    public void insertFltRouteMap() {
        String r = "A123";
        PointInfo p1 = new PointInfo("H1"), p2 = new PointInfo("HHHHH");
        List<PointInfo> pList = new ArrayList<>();
        pList.add(p1);pList.add(p2);
        Map<String,List<PointInfo>> rmap = new HashMap<>();
        rmap.put(r,pList);
        JDBCHelper.insertFltRouteMap("test2", rmap);
        System.out.println("Done");

    }

    @Test
    public void insertPointList() {
        List<Point> plist = new ArrayList<>();
        Point p = new Point("A123", "呵呵");
        plist.add(p);
        JDBCHelper.insertPointList("test",plist);
        System.out.println("Done");
    }
}
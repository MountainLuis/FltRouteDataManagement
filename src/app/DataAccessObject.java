package app;

import bean.PointInfo;
import util.MysqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataAccessObject {
    public Map<String, List<PointInfo>> routeMap = new HashMap<>();
    public DataAccessObject() {
        routeMap = getRouteData();
    }

    /**
     * 获取国际航路数据
     * @return
     */
    public  Map<String, List<PointInfo>> getRouteData() {
        String table = "route_all";
        ResultSet rs = MysqlHelper.getResultSet(table);
        Map<String, List<PointInfo>> routeMap = new HashMap<>();
        try{
            while (rs.next()) {
                String r = rs.getString("route");
                PointInfo pi = new PointInfo();
                pi.fix_pt = rs.getString("fix_pt");
//                pi.longitude = rs.getDouble("longitude");
//                pi.latitude = rs.getDouble("latitude");
                pi.idx = rs.getInt("seq");
                pi.enRoute = r;
                if (routeMap.keySet().contains(r)) {
                    List<PointInfo> pList = routeMap.get(r);
                    pList.add(pi);
                    routeMap.put(r,pList);
                } else {
                    List<PointInfo> pList = new ArrayList<>();
                    pList.add(pi);
                    routeMap.put(r, pList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return routeMap;
    }
    public List<PointInfo> getPtSeq(String r) {
        return routeMap.get(r);
    }
    public List<PointInfo> getSubPtSeq(String r, String startPt, String endPt) {
        List<PointInfo> pList = null;
        List<PointInfo> route = getPtSeq(r);
        int start = 0, end = 0;
        for (int i = 0; i < route.size(); i++) {
            PointInfo pi = route.get(i);
            if (pi.fix_pt.equals(startPt)) {
                start = i;
            }
            if (pi.fix_pt.equals(endPt)) {
                end = i;
            }
        }
        if (start == end) {
            System.out.println("End Point Error."+ r  + startPt + start + " " + endPt + end);
            System.exit(0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
            pList = route.subList(start + 1,end + 1);
            Collections.reverse(pList);
        } else {
            pList = route.subList(start, end);
        }
        return  pList;
    }
    public Map<String, List<PointInfo>> getNaipData() {
        String table2 = "route_naip_fullname";
        ResultSet rs = MysqlHelper.getResultSet(table2);
        Map<String, List<PointInfo>> naipRoute = new HashMap<>();
        try{
            while (rs.next()) {
                String r = rs.getString("route");
                PointInfo pi = new PointInfo();
                pi.fix_pt = rs.getString("fix_pt");
                pi.idx = rs.getInt("seq");
                pi.enRoute = r;
                if (naipRoute.keySet().contains(r)) {
                    List<PointInfo> pList = naipRoute.get(r);
                    pList.add(pi);
                    naipRoute.put(r,pList);
                } else {
                    List<PointInfo> pList = new ArrayList<>();
                    pList.add(pi);
                    naipRoute.put(r, pList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return naipRoute;
    }



}

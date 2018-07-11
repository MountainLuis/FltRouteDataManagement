package app;

import bean.PointInfo;
import util.MysqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GetRouteData {
    public Map<String, List<PointInfo>> routeMap = new HashMap<>();
    public GetRouteData() {
        routeMap = getDataFromSql();
    }

    public  Map<String, List<PointInfo>> getDataFromSql() {
        String sql = "select * from route_all";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try{
            while (rs.next()) {
                String r = rs.getString("route");
                PointInfo pi = new PointInfo();
                pi.fix_pt = rs.getString("fix_pt");
                pi.longitude = rs.getDouble("longitude");
                pi.latitude = rs.getDouble("latitude");
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

}

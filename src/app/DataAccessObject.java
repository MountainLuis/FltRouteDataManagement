package app;

import bean.FltPlan;
import bean.PointInfo;
import org.apache.log4j.Logger;
import util.AccessHelper;
import util.MysqlHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataAccessObject {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);

    public static Map<String, List<PointInfo>> routeMap = null;
    public static Map<String, List<PointInfo>> naipMap = null;
    public DataAccessObject() {
        routeMap = getRouteData();
        naipMap = getNaipData();
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
    /**
     * 获取NAIP航路点数据
     * @return
     */
    public Map<String, List<PointInfo>> getNaipData() {
        String table2 = "route_naip";
        ResultSet rs = MysqlHelper.getResultSet(table2);
        Map<String, List<PointInfo>> naipRoute = new HashMap<>();
        try{
            while (rs.next()) {
                String r = rs.getString("route");
                PointInfo pi = new PointInfo();
                pi.fix_pt = rs.getString("fix_pt");
                pi.pt_name = rs.getString("pt_name");
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
    public List<PointInfo> getPtSeq(String r) {
        if (!routeMap.keySet().contains(r)) {
            System.err.println("航路" + r + "不在route_all里。");
            System.exit(0);
        }
        return routeMap.get(r);
    }
    public List<PointInfo> getPtSeqNaip(String r) {
        if (!naipMap.keySet().contains(r)) {
            System.err.println("航路" + r + "不在 naip航路里。");
            System.exit(0);
        }
        return naipMap.get(r);
    }

    public List<PointInfo> getSubPtSeq(String r, String startPt, String endPt, int abroad) {
        List<PointInfo> pList = null;
        List<PointInfo> route = null;
        if (abroad == 1) {
            route = getPtSeq(r);
        } else if (abroad == 0) {
            route = getPtSeqNaip(r);
        } else {
            System.out.println("abroad标签有误" + r);
        }
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
            String res = "航路"+ r  + "不包含点：" + startPt + " " + endPt;
            LOGGER.info(res);
//            System.out.println("End Point Error:"+ r  + " " + startPt + " " + endPt);
            return pList;
//            System.exit(0);
        }
        if (start +1 == end || end + 1 == start) {
//            pList.add(route.get((start < end ? end : start)));
            return pList;
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
            pList = route.subList(start + 1,end);
            Collections.reverse(pList);
        } else {
            pList = route.subList(start + 1, end);
        }
        return  pList;
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
            String res = "航路"+ r  + "不包含点：" + startPt + " " + endPt;
            LOGGER.info(res);
//            System.out.println("End Point Error:"+ r  + " " + startPt + " " + endPt);
            return pList;
//            System.exit(0);
        }
        if (start +1 == end || end + 1 == start) {
//            pList.add(route.get((start < end ? end : start)));
            return pList;
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
            pList = route.subList(start + 1,end);
            Collections.reverse(pList);
        } else {
            pList = route.subList(start + 1, end);
        }
        return  pList;
    }
    public List<FltPlan> getFltPlan(String time) {
        List<FltPlan> plans = new ArrayList<>();
        ResultSet rs = AccessHelper.getResultSet(time);
        try {
            while (rs.next()) {
                FltPlan fp = new FltPlan();
                fp.flt_no = rs.getString("FLIGHTID");
                fp.regitration_num = rs.getString("P_REGISTENUM");
                fp.acft_type = rs.getString("P_AIRCRAFTTYPE");
                fp.to_ap = rs.getString("P_DEPAP");
                fp.ld_ap = rs.getString("P_ARRAP");
                fp.dep_time = rs.getString("P_DEPTIME");
                fp.arr_time = rs.getString("P_ARRTIME");
                fp.flt_path = rs.getString("P_ROUTE");
                if (fp.to_ap == null || fp.ld_ap == null) {
                    continue;
                } else {
                    plans.add(fp);
                }
            }
            System.out.println("读取计划" + time + "完成。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }
    public List<FltPlan> getFltPlan() {
        List<FltPlan> plans = new ArrayList<>();
        ResultSet rs = AccessHelper.getResultSet();
        try {
            while (rs.next()) {
                FltPlan fp = new FltPlan();
                fp.flt_no = rs.getString("FLIGHTID");
                fp.regitration_num = rs.getString("P_REGISTENUM");
                fp.acft_type = rs.getString("P_AIRCRAFTTYPE");
                fp.to_ap = rs.getString("P_DEPAP");
                fp.ld_ap = rs.getString("P_ARRAP");
                fp.dep_time = rs.getString("P_DEPTIME");
                fp.arr_time = rs.getString("P_ARRTIME");
                fp.flt_path = rs.getString("P_ROUTE");
                if (fp.to_ap == null || fp.ld_ap == null || fp.flt_path == "") {
                    continue;
                } else {
                    plans.add(fp);
                }
            }
            System.out.println("读取计划完成。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }
}

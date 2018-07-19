package dataManager;

import bean.FltPlan;
import bean.PointInfo;

import org.apache.log4j.Logger;
import util.JDBCHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataAccessObject {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    private static Map<String, List<PointInfo>> routeMap = null;
    private static Map<String, List<PointInfo>> naipMap = null;
    private static Map<String, double[]> allPtMap = null;
    private static Map<String, double[]> allNaipPtMap = null;
    private static List<String> boundries = null;
    public DataAccessObject() {
        routeMap = getRouteData();
        naipMap = getNaipData();
        allPtMap = getAllPtMap();
        allNaipPtMap = getAllNaipPtMap();
        boundries = getNationalBoundries();
    }

    private Map<String,double[]> getAllNaipPtMap() {
        String key = "MySQL";
        String table = "allpoint_naip";
        Map<String, double[]> allPtMap = new HashMap<>();
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try {
            while (rs.next()) {
                String pt = rs.getString("pid");
                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                double[] res = {lat,lng};
                allPtMap.put(pt, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPtMap;
    }
    public List<String> getNationalBoundries() {
        List<String> res = new ArrayList<>();
        String key = "MySQL";
        String table = "national_boundaries";
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try {
            while (rs.next()) {
                String pt = rs.getString("fix_pt");
                res.add(pt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取国际航路数据
     * @return
     */
    public  Map<String, List<PointInfo>> getRouteData() {
        String key = "MySQL";
        String table = "route_all";
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        Map<String, List<PointInfo>> routeMap = new HashMap<>();
        try{
            while (rs.next()) {
                String r = rs.getString("route");
                PointInfo pi = new PointInfo();
                pi.fix_pt = rs.getString("fix_pt");
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
        String key = "MySQL";
        String table2 = "route_naip";
        ResultSet rs = JDBCHelper.getResultSet(table2, key);
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

    /**
     * 从allPoint取得所有点的坐标；
     * @return
     */
    public  Map<String, double[]> getAllPtMap(){
        String key = "MySQL";
        String table = "allpoint";
        Map<String, double[]> allPtMap = new HashMap<>();
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try {
            while (rs.next()) {
                String pt = rs.getString("pid");
                double lat = rs.getDouble("latitude");
                double lng = rs.getDouble("longitude");
                double[] res = {lat,lng};
                allPtMap.put(pt, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPtMap;
    }
    /**
     * 从route_all取得指定航路的航路点集合
     * @param r
     * @return
     */
    public List<PointInfo> getPtSeq(String r) {
        if (!routeMap.keySet().contains(r)) {
            LOGGER.error("航路" + r + "不在route_all里");
            return null;
         }
        return routeMap.get(r);
    }

    /**
     * 从route_naip取得指定航路的航路点集合
     * @param r
     * @return
     */
    public List<PointInfo> getPtSeqNaip(String r) {
        if (!naipMap.keySet().contains(r)) {
            LOGGER.error("航路" + r + "不在 naip航路里。");
            return null;
        }
        return naipMap.get(r);
    }

    /**
     *
     * @param r
     * @param startPt
     * @param endPt
     * @param abroad
     * @return
     */
    public List<PointInfo> getSubPtSeq(String r, String startPt, String endPt, int abroad) {
        List<PointInfo> pList = null;  //result
        List<PointInfo> route = null;
        if (startPt.equals(endPt)) {
         LOGGER.error("航段首末点不能是同一个点：" + r + " " + startPt);
         return pList;
        }
        switch (abroad) {
            case 0:
                route = getPtSeqNaip(r);
                break;
            case 1:
                route = getPtSeq(r);
                break;
                default:
                    LOGGER.error("abroad标签有误" + r);
        }
        int start = -1, end = -1;
        for (int i = 0; i < route.size(); i++) {
            PointInfo pi = route.get(i);
            if (pi.fix_pt.equals(startPt)) {
                start = i;
            }
            if (pi.fix_pt.equals(endPt)) {
                end = i;
            }
        }
        if (start == -1 || end == -1) {
            LOGGER.info( "航路"+ r  + "不包含点：" + startPt + " : " + endPt);
            return pList;
        }
        if (start +1 == end || end + 1 == start) {
            LOGGER.info(r + "航路上两点相邻" + startPt + " " + endPt);
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

    /**
     * 从Access数据库中取得航班计划
      * @return
     */
    public List<FltPlan> getFltPlanFromAccess(String time) {
        String key = "Access";
        String sql = "select * from test where P_DEPTIME like '" + time + "%'";
//        String sql = "select * from test";
        List<FltPlan> plans = new ArrayList<>();
        ResultSet rs = JDBCHelper.getResultSetWithSql(sql, key);
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
            System.out.println("读取计划完成。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    /**
     * 从MySQL中取得航班计划
     * @param table
     * @return
     */
    public List<FltPlan> getFltPlanFromMysql(String table) {
        List<FltPlan> plans = new ArrayList<>();
        String key = "MySQL";
        ResultSet rs = JDBCHelper.getResultSet(table, key);
        try {
//            int i = 0;
            while (rs.next()) {
                FltPlan fp = new FltPlan();
                fp.flt_no = rs.getString("flt_no");
                fp.regitration_num = rs.getString("registration_num");
                fp.acft_type = rs.getString("acft_type");
                fp.to_ap = rs.getString("to_ap");
                fp.ld_ap = rs.getString("ld_ap");
                fp.dep_time = rs.getString("dep_time");
                fp.arr_time = rs.getString("arr_time");
                fp.flt_path = rs.getString("flt_path");
                if (fp.to_ap == null || fp.ld_ap == null || fp.flt_path == "") {
                    continue;
                } else {
                    plans.add(fp);
                 }
//                if (i++ == 10) {
//                    break;
//                }
            }
            System.out.println("读取计划完成。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    /**
     * 取得指定航路点的经纬度
     * @param id
     * @return
     */
    public double[] getFixPtCoordinate(String id) {
        if (allPtMap.containsKey(id)) {
            return allPtMap.get(id);
        }else if( allNaipPtMap.containsKey(id)) {
            return allNaipPtMap.get(id);
        } else {

//            LOGGER.info(id + "不在数据库中");
            return null;
        }
    }

    public boolean isRouteMapContainKey(String key) {
        return routeMap.containsKey(key);
    }
    public boolean isNaipMapContainKey(String key) {
        return naipMap.containsKey(key);
    }
    public boolean isBoundriesContainPoint(String p) {
        return boundries.contains(p);
    }
}

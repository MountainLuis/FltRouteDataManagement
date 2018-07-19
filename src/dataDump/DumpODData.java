package dataDump;

import bean.FltPlan;
import bean.Point;
import util.MysqlHelper;
import util.MysqlHelperNew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DumpODData {
    Map<String,String[]> pointMap = null;
    Map<String, String> tmpMap = new HashMap<>();
    Set<String> ptUnFind = new HashSet<>();
    List<String> deleteList = new ArrayList<>();

    public DumpODData() {
        pointMap = getPointInfo();
    }
    public static void  main(String[] args) {
        DumpODData dddd = new DumpODData();
        List<FltPlan> planList = dddd.getFltPlanData();
        System.out.println(planList.size());
        dddd.storagePlans(planList);
        Map<String, List<Point>> odm = dddd.dealWithOD();
        dddd.storageODPoints(odm);
        System.out.println(dddd.ptUnFind.size());
        System.out.println(planList.size() + " plans");


      Iterator iter = dddd.ptUnFind.iterator();
      while (iter.hasNext()) {
          System.out.println(iter.next());
      }
      for (String s:dddd.deleteList) {
          System.out.println(s + " ====");
      }
    }
    public List<FltPlan> deletePlan(List<FltPlan> planList ){
        for (FltPlan plan: planList) {
            if (deleteList.contains(plan.flt_path)) {
                planList.remove(plan);
            }
        }
        return planList;
    }



    public Map<String, List<Point>> dealWithOD(){
        Map<String, List<Point>> odMap = new HashMap<>();
        for (String path : tmpMap.keySet()) {
            List<Point> pList = getODPoints(tmpMap.get(path));
            if (pList == null) {
                deleteList.add(path);
                continue;
            } else {
                odMap.put(path, pList);
            }
        }
        return odMap;
    }

    public List<FltPlan> getFltPlanData() {
        List<FltPlan> fltPlanList = new ArrayList<>();
        String sql = "select * from odpath";
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                FltPlan p = new FltPlan();
                p.flt_no =rs.getString("flt_no");
                p.regitration_num = rs.getString("reg_num");
                p.acft_type = rs.getString("acft_type");
                p.to_ap = rs.getString("to_ap");
                p.ld_ap = rs.getString("ld_ap");
                p.dep_time = rs.getString("dep_time");
                String odName = p.flt_no + "-" + p.regitration_num + "-" + rs.getString("od").toUpperCase();
                p.flt_path = odName;
                fltPlanList.add(p);
                String path = rs.getString("path");
                tmpMap.put(odName,path);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fltPlanList;
    }
    public List<Point> getODPoints(String s) {
        String sub = s.substring(1,s.length()-1);
        String[] pts = sub.split("-");
        List<Point> enRoute = new ArrayList<>();
        for (int i = 0; i < pts.length; i++) {
            Point fp = getPointData(pts[i]);
            if (fp == null) {
//                System.out.println(s + "!!!");
                return null;
            }
            enRoute.add(fp);
        }
        return  enRoute;
    }
    public Point getPointData(String pi){
        Point p = new Point();
        p.pid = pi;
        String[] res = getPointCoord(pi);
        if (res == null) {
            return  null;
        }else {
            p.latitude = Double.parseDouble(res[0]);
            p.longitude = Double.parseDouble(res[1]);
        }
    return  p;
    }
    public void storagePlans(List<FltPlan> fltPlanList){
        String table = "flightplan_20150210";
        MysqlHelperNew.createPlanTable(table);
        MysqlHelperNew.insertFltPlanList(table, fltPlanList);
        System.out.println("Done");
    }
    public  Map<String,String[]> getPointInfo() {
        Map<String,String[]> pMap = new HashMap<>();
        String sql = "select * from navigation_control";
        ResultSet rs = MysqlHelperNew.getResultSet(sql);
        try{
            while (rs.next()) {
                String pt = rs.getString("FixPt_ID");
                String lat = rs.getString("FixPt_Lat");
                String lng = rs.getString("FixPt_Long");
                String[] res = {lat, lng};
                pMap.put(pt, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = "select * from allpoint_2015";
        ResultSet rs2 = MysqlHelperNew.getResultSet(sql2);
        try{
            while (rs2.next()) {
                String pt = rs2.getString("pid");
                String lng = rs2.getString("latitude");
                String lat = rs2.getString("longitude");
                String[] res = {lat, lng};
                pMap.put(pt, res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pMap;
    }

    public String[] getPointCoord(String pt) {
        if (pointMap.containsKey(pt)) {
            return pointMap.get(pt);
        } else {
//            System.out.println(pt + "找不到" );
            ptUnFind.add(pt);
            return null;
        }
    }
    public void storageODPoints(Map<String, List<Point>> odm){
        String table = "odroute_20150210";
        MysqlHelperNew.createPointTable(table);
        MysqlHelperNew.insertODPoints(table, odm);
        System.out.println("Done");
    }
}

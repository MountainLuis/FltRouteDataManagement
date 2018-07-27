package dataManager;

import bean.FltPlan;
import bean.Point;
import bean.PointInfo;
import org.apache.log4j.Logger;
import util.JDBCHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 三个步骤，
 * 1，从所有计划中提取出国内起飞航班
 * 2，将国内计划的一些重名点替换成不重复的名字
 * 3，将缺少Path的计划补全，无法补全的剔除
 *
 */

public class FltPlanSplit {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    DataAccessObject dao = new DataAccessObject();
    String[] rePoints = new String[]{"DO","CH","UF","HG","RL","G","YU","PA","CD","QM","WB","KM","FK","FY"};
    String[] reP = new String[]{"DO_青州","DO_姚集","CH_长海","CH_CHEUNG CHAU","UF_安康","UF_龙门"
            ,"HG_衡水","HG_天河","RL_哈密","RL_汉口","G_珠海","G_西安","G_咸阳","YU_延安","YU_沱沱河","PA_安庆",
            "PA_五通","CD_青白口","CD_常德","QM_盐步","QM_牡丹江T","WB_莘庄","WB_临汝","KM_怀来","KM_大虎山","FK_阜康"
            ,"FK_路桥","FY_鞍山","FY_庆阳"};
    public List<String> repeatpoints = Arrays.asList(rePoints);
    public List<String > rePList = Arrays.asList(reP);
    public  Map<String, List<String>> ptRoutes = null;
    public FltPlanSplit() {
        ptRoutes = getPointRoute();
    }
   static int countFill =0;
    public static void main(String[] args) {
        FltPlanSplit fps = new FltPlanSplit();
        List<FltPlan> planList = fps.getFltPlan();
        System.out.println(planList.size());
        fps.selectPath(planList);  // 此方法用于替换航路中重复点的名称
//        System.out.println("count B330 :" + countB330 + " count XUZ" + countXUZ);
//        for (int i = 0; i < 100; i++) {
//            System.out.println(planList.get(i).flt_path);
//        }
//        List<FltPlan> res = fps.getDomesticPath(planList);
//        Map<String, String> odm = fps.getODMap(planList);

//        System.out.println("Map have " +odm.size() + " ods");
//        fps.checkNUllMapValue(odm);
 //        List<FltPlan> res = fps.fillPath(odm, planList);
//        System.out.println(res.size());
        fps.storageFltPlan(planList);
//        LOGGER.info("no od path plans : " + countFill);
    }
    public List<FltPlan> getFltPlan() {
        String table = "fme201706_domestic_v4";
        List<FltPlan> res = dao.getStandardFltPlanFromMySQL(table, "");
        return res;
    }
    public void storageFltPlan(List<FltPlan> planList) {
        String table = "fme201706_domestic_v4_1";
        dao.storageFltPlan(table, planList);
    }
    public void checkNUllMapValue(Map<String, String> odMap) {
        int countNUll = 0;
        int countFULL = 0;
        for (String s : odMap.keySet()) {
//            if (odMap.get(s) == null || odMap.get(s).equals("")) {
//                countNUll++;
//                LOGGER.warn(s);
//            } else {
//                countFULL++;
//            }
            LOGGER.info(s);
        }
//        System.out.println("has value :" + countFULL);
//        System.out.println("no value : " + countNUll);
    }

    static  int countChanged = 0;
    public void selectPath(List<FltPlan> plans){
        for (FltPlan p : plans) {
            String path = isSelected(p);
            if (path.equals(p.flt_path)) {
                continue;
            } else {
//                System.out.println("==========" + p.flt_path);
                countChanged++;
                p.flt_path = path;
//                System.out.println("+++++++++++" + p.flt_path);
            }
        }
        LOGGER.debug("changed : " + countChanged);
    }

    public String isSelected(FltPlan plan) {
        String path = plan.flt_path;
        String[] pts = path.trim().split("\\s+");
        List<String> points = Arrays.asList(pts);
        int idx = getIndexOfPoint(points);
        if (idx == -1) {
            return path;
        } else {
            String pp = splitPointName(points.get(idx));
            if (idx > 0 && idx < points.size()-1) {
                String r1 = points.get(idx - 1);
                String r2 = points.get(idx + 1);
                String poi = judgeRoute(pp,r1, r2);
                if (poi == null) {
                    LOGGER.info(plan.flt_no + "-" + plan.to_ap+ "-" + plan.ld_ap + "  " + path);
                }
                LOGGER.debug("changed: " + points.get(idx) + "  " + poi);
                points.set(idx,poi);
            } else {
                if (idx == 0) {
                    String r1 = points.get(idx + 1);
                    String poi = judgeRoute(pp,r1, "");
                    if (poi == null) {
                        LOGGER.info(plan.flt_no + "-" + plan.to_ap+ "-" + plan.ld_ap  + "   " +path);
                    }
                    LOGGER.debug("changed: " + points.get(idx) + "  " + poi);
                    points.set(idx,poi);
                }
                if (idx == points.size() - 1) {
                    String r1 = points.get(idx - 1);
                    String poi = judgeRoute(pp, r1, "");
                    if (poi == null) {
                        LOGGER.info(plan.flt_no + "-" + plan.to_ap+ "-" + plan.ld_ap  + "  " + path);
                    }
                    LOGGER.debug("changed: " + points.get(idx) + "  " + poi);
                    points.set(idx,poi);
                }
            }
            return addListToStr(points);
        }
    }
    public String addListToStr(List<String> ptList) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ptList.size(); i++) {
            if (ptList.get(i) == null || ptList.get(i).equals("")) {
                continue;
            }
            res.append(ptList.get(i));
            if (i < ptList.size() -1) {
                res.append("  ");
            }
        }
        return res.toString();
    }

    public int getIndexOfPoint(List<String> points) {
        for (int i = 0; i < points.size(); i++) {
            if (repeatpoints.contains(splitPointName(points.get(i)))) {
                return i;
            } else {
                continue;
            }
        }
        return -1;
    }
    private String splitPointName(String s) {
        if (s.contains("/")) {
            String[] t = s.split("/");
            if (t[0].equals("VRK")) {
                return "VKR";
            }
            return s.split("/")[0];
        } else {
            return s;
        }
    }

    public Map<String, String> getODMap(List<FltPlan> fltPlans) {
//        Map<String, String> odMap = new HashMap<>();
        Map<String, String> noPathODMap = new HashMap<>();
        for (FltPlan plan : fltPlans) {
            String od = plan.to_ap + "-" + plan.ld_ap;
            String path = plan.flt_path;
            if (path.equals("")) {
                noPathODMap.put(od, "");
              continue;
            } else {
//                odMap.put(od, path);
                continue;
            }
        }
//        return odMap;
        return noPathODMap;
    }
    public Set<String> getODSet(List<FltPlan> fltPlans) {
        Set<String> res = new HashSet<>();
        for (FltPlan plan : fltPlans) {
            String od = plan.to_ap + "-" + plan.ld_ap;
            res.add(od);
        }
        return res;
    }



    public List<FltPlan> fillPath(Map<String, String> odMap, List<FltPlan> planList){
        List<FltPlan> res = new ArrayList<>();
        for (FltPlan plan : planList) {
            if (plan.to_ap.equals("") || plan.ld_ap.equals("") ) {
                LOGGER.warn(plan.flt_no + " no Airport");
                continue;
            }
            String od = plan.to_ap + "-" + plan.ld_ap;
            if (plan.flt_path.equals("")) {
                if (odMap.containsKey(od)){
                    String path = odMap.get(od);
//                    if (path.equals("")) {
//                        LOGGER.warn(plan.flt_no + " no value in map");
//                    }
                    plan.flt_path = path;
                    res.add(plan);

                } else {
//                    res.add(plan);
                    countFill++;
                    LOGGER.info(plan.toString() + " no od");
                }
            } else {
                res.add(plan);
            }
        }
        return res;
    }

    public List<FltPlan> getDomesticPath(List<FltPlan> planList) {
        List<FltPlan> res = new ArrayList<>();
        for (FltPlan plan : planList) {
            if (isDomesticAP(plan.to_ap)) {
                if (isDomesticAP(plan.ld_ap)) {
                    res.add(plan);
                } else {
                    String path = cutPathIn2Out(plan.flt_path);
                    if (!path.equals(plan.flt_path)) {
                        LOGGER.warn(plan.flt_no);
                    }
                    plan.flt_path = path;
                    res.add(plan);
                }
            } else {
//                if (isDomesticAP(plan.ld_ap)) {
////                    String path = cutPathOut2In(plan.flt_path);
////                    plan.flt_path = path;
////                    res.add(plan);
////                } else {
////
////                }
                continue;
            }
        }
        return res;
    }
    public boolean isDomesticAP(String s) {
        String pattern = "^Z[A-Z]*$";
        return s.matches(pattern);
    }
    public String cutPathIn2Out(String path) {
        String[] pts = path.split("\\s+");
        String res = null;
        for (String s : pts) {
            if (s.contains("/")) {
                String tt = s.split("/")[0];
                if (dao.isBoundriesContainPoint(s)) {
                    res = path.substring(0,path.indexOf(s) + s.length());
                } else {
                    continue;
                }
            } else {
                if (dao.isBoundriesContainPoint(s)) {
                    res = path.substring(0,path.indexOf(s) + s.length());
                }else  {
                    continue;
                }
            }
        }
        if (res != null) {
            return res;
        } else {
            return path;
        }
    }
    public String cutPathOut2In(String path) {
        String[] pts = path.split("\\s+");
        String res = null;
        for (String s : pts) {
            if (s.contains("/")) {
                String tt = s.split("/")[0];
                if (dao.isBoundriesContainPoint(s)) {
                    res = path.substring(path.indexOf(s));
                } else {
                    continue;
                }
            } else {
                if (dao.isBoundriesContainPoint(s)) {
                    res = path.substring(path.indexOf(s));
                }else  {
                    continue;
                }
            }
        }
        if (res != null) {
            return res;
        } else {
            return path;
        }
    }


    public Map<String, List<String>> getPointRoute() {
        Map<String,List<String>> pointRouteMap = new HashMap<>();
        for (String point : rePList){
            List<String> routes = getRouteWithPoint(point);
            pointRouteMap.put(point, routes);
        }
        return pointRouteMap;
    }
    public List<String> getRouteWithPoint(String pt) {
        List<String> routes = new ArrayList<>();
        String sql = "select route from route_naip where fix_pt = '" + pt + "'";
     ResultSet rs = JDBCHelper.getResultSetWithSql(sql, "MySQL");
     try{
         while (rs.next()) {
             String r = rs.getString("route");
             routes.add(r);
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return  routes;
    }
    public String judgeRoute(String pp,String r1, String r2){
        String res = null;
        for (String p: ptRoutes.keySet())  {
            if ((!r1.equals("") && ptRoutes.get(p).contains(r1)) || (!r2.equals("") && ptRoutes.get(p).contains(r2))) {
                res = p;
                return res;
            } else {
                continue;
//                System.out.println("没找到适合的航路");
            }
        }
        if (pp.equals("CH") && (r1.equals("B330") || r2.equals("B330"))) {
                    res = "CH_CHEUNG CHAU";
        }
        if ((pp.equals("CD") && r1.equals("TAOYUAN") && r2.equals(""))
                || (pp.equals("CD") && r1.equals("TAOYUAN"))
                || (pp.equals("CD") && r1.equals("LLC") && r2.equals(""))){
            res = "CD_常德";
        }
        if ((r1.equals("JR")&& r2.equals("") && pp.equals("CD"))
                || (r1.equals("CU")&& r2.equals("JR") && pp.equals("CD"))
                || (r1.equals("KM")&& r2.equals("JR") && pp.equals("CD"))) {
            res = "CD_青白口";
        }
        if (r1.equals("NSH") && pp.equals("UF") && r2.equals("")) {
            res = "UF_安康";
        }
//        if ((r1.equals("DPX") && r2.equals("XUZ"))||(r2.equals("DPX") && r1.equals("XUZ"))) {
////            countXUZ++;
//            res = "DO_姚集";
//        }
//        if (r1.equals("DCT") && pp.equals("CD")&&r2.equals("DCT")) {
//            res = "CD_青白口";
//        }
//        if ((r1.equals("KALMU") && pp.equals("CD") && r2.equals("LLC")) ||
//                ((r1.equals("H9") && pp.equals("CD") && r2.equals("W194")))){
//            res = "CD_常德";
//        }
        if (pp.equals("KM")){
            res = "KM_怀来";
        }
//        if (pp.equals("PA") && r1.equals("W")) {
//            res = "PA_五通";
//        }
//        if (pp.equals("G") && r2.equals("")) {
//            res = "";
//        }
        return  res;
    }



}

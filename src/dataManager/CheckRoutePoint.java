package dataManager;

import bada.util.GeoUtil;
import bean.FltPlan;
import bean.Point;
import org.apache.log4j.Logger;
import sun.util.resources.cldr.ff.LocaleNames_ff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckRoutePoint {
    private static final Logger LOGGER = Logger.getLogger(CheckRoutePoint.class);
    DataAccessObject dao = new DataAccessObject();

    public List<FltPlan> getFltPlans() {
        String table = "fme201806_domestic_final_0";
        List<FltPlan> res = dao.getStandardFltPlanFromMySQL(table, "");
        return res;
    }
    public static void main(String[] args) {
        CheckRoutePoint crp = new CheckRoutePoint();
        List<FltPlan> plans = crp.getFltPlans();
//        crp.checkFltPlans(plans);
//        LOGGER.warn(countFar);
        crp.checkPointsAndRoutes(plans);
    }
    public void checkPointsAndRoutes(List<FltPlan> plans) {
        int countNum = 0;
        for (FltPlan plan : plans) {
           int count =  checkPath(plan.flt_path);
           if (count < 3 ) {
               countNum++;
               LOGGER.debug(count + " " + plan.flt_no);
           }
        }
        LOGGER.debug(countNum + "!!");
    }
    public int checkPath(String path) {
        int noP = 0;
        int countP = 0;
        String[] pt = path.split("\\s+");
        List<String> pts = Arrays.asList(pt);
        for (String p0 : pts) {
            String p = splitPointName(p0);
            if (dao.isNaipMapContainKey(p)) {
                countP++;
                continue;
            }else if (dao.isNaipPoints(p)){
                countP++;
                continue;
            }else if(p.equals("DCT")) {
                continue;
            } else if (p.equals("")){
                continue;
            }else {
                noP++;
//                LOGGER.debug(p + "not found.");
            }
        }
        return countP;
    }
    private String splitPointName(String pt) {
        if (pt.contains("/")) {
            return pt.split("/")[0];
        }else {
            return pt;
        }
    }




    static int countFar = 0, countAngel = 0;
    public void checkFltPlans(List<FltPlan> planList) {
        for (FltPlan plan: planList) {
         List<Point> points = getPointList(plan);
         if (checkDistBetweenPoints(points) ) {
             continue;
         } else {
             LOGGER.info(plan.toString() + " Points Too Far.");
             countFar++;
         }
         if (checkAngelBetweenPoints(points)){
             continue;
         }else {
             LOGGER.info(plan.toString() + " path is not smooth");
             countAngel++;
         }
        }
        LOGGER.debug("far :" + countFar + "  not smooth: " + countAngel);
    }
    public List<Point> getPointList(FltPlan plan) {
        List<Point> pointList = new ArrayList<>();
        String path = plan.flt_path;
        String[] pts = path.split("\\s+");
        for (String pt : pts) {
            if (dao.isNaipMapContainKey(pt) || !dao.isNaipPoints(pt)) {
               continue;
            }
            Point p = dao.getNaipPoint(pt);
            if (p.pid.equals("")) {
                LOGGER.debug(pt + " not naip point");
                continue;
            } else {
                pointList.add(p);
            }
        }
        return pointList;
    }
    public boolean checkDistBetweenPoints(List<Point> pointList) {
        for (int i = 0; i < pointList.size() - 1; i++) {
            Point p1 = pointList.get(i);
            Point p2 = pointList.get(i + 1);
            if (isTrueDist(p1,p2)) {
                continue;
            } else {
                LOGGER.info(p1.pid + " " + p2.pid);
                return false;
            }
        }
        return true;
    }
    public boolean isTrueDist(Point p1, Point p2) {
        double dist = GeoUtil.calDist(p1.longitude,p1.latitude,p2.longitude,p2.latitude);
        if (dist < 500) {
            return  true;
        }else {
            return false;
        }
    }

    public boolean checkAngelBetweenPoints(List<Point> pointList) {
        if (pointList.size() < 3) {
            return true;
        }
        for (int i = 1; i < pointList.size() - 1; i++) {
            Point p1 = pointList.get(i-1);
            Point p2 = pointList.get(i);
            Point p3 = pointList.get(i + 1);
            if (isTrueHeading(p1,p2,p3)) {
                continue;
            } else {
                LOGGER.info(p1.pid + " " + p2.pid + " " + p3.pid);
                return  false;
            }
        }
        return true;
    }
    public boolean isTrueHeading(Point p1, Point p2, Point p3) {
        double h1 = GeoUtil.fixAngle(GeoUtil.calHeading(p1.longitude,p1.latitude,p2.longitude,p2.latitude));
        double h2 = GeoUtil.fixAngle(GeoUtil.calHeading(p2.longitude,p2.latitude,p3.longitude,p3.latitude));
        if (Math.abs(h2 - h1) > 90) {
            return false;
        } else {
            return true;
        }
    }
}

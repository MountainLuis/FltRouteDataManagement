package dataManager;

import bada.util.GeoUtil;
import bean.FltPlan;
import bean.Point;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CheckRoutePoint {
    private static final Logger LOGGER = Logger.getLogger(CheckRoutePoint.class);
    DataAccessObject dao = new DataAccessObject();

    public List<FltPlan> getFltPlans() {
        String table = "fme201806_domestic_v5";
        List<FltPlan> res = dao.getStandardFltPlanFromMySQL(table, "");
        return res;
    }
    public static void main(String[] args) {
        CheckRoutePoint crp = new CheckRoutePoint();
        List<FltPlan> plans = crp.getFltPlans();
        crp.checkFltPlans(plans);
        LOGGER.warn(countFar);
    }
    static int countFar = 0;
    public void checkFltPlans(List<FltPlan> planList) {
        for (FltPlan plan: planList) {
         List<Point> points = getPointList(plan);
         if (checkDistBetweenPoints(points)) {
             continue;
         } else {
             LOGGER.info(plan.toString() + " Points Too Far.");
             countFar++;
         }
        }
    }
    public List<Point> getPointList(FltPlan plan) {
        List<Point> pointList = new ArrayList<>();
        String path = plan.flt_path;
        String[] pts = path.split("-");
        for (String pt : pts) {
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

    public void checkAngelBetweenPoints(List<Point> pointList) {

    }
//    public void
}

package dataManager;

import bean.FltPlan;

import java.util.List;

public class FlightPlanService {
    public static DataAccessObject dao = new DataAccessObject();
    ODRouteManager om = new ODRouteManager();
    public void Planservice() {
    }

//    public void getFltPlan() {
//        long time = 201706;
//        for (int i = 0; i < 30; i++) {
//            String s = String.valueOf((time + i));
//            List<FltPlan> plans = dao.getFltPlan(s);
//            plans = fpm.changePaths(plans);
//            fpm.storagePlans(s, plans);
//            System.out.println(s + "Used time:" + (System.currentTimeMillis() - time) / 1000);
//    }
}

package apply.util;

import code.apply.bean.FlightPlan;
import code.apply.impl.FlightPlanManagement;
import code.bada.bean.FixPt;
import code.bada.bean.Route;
import org.junit.Test;

import java.util.List;

public class TestFlightPlanManagement {
    List<FixPt> pts = null;
    List<FlightPlan> plans = null;
    FlightPlanManagement fltMng = new FlightPlanManagement();
    @Test
    public void testGetData() {
        pts = fltMng.getRouteData("zbaa-zhhh");
        for (int i = 0; i < pts.size(); i++) {
            System.out.println(pts.get(i).ID);
        }
        System.out.println("--------------------------");
        pts = fltMng.getSidStarData("ZUUU02L-SID-CZH_01D");
        for (int i = 0; i < pts.size(); i++) {
            System.out.println(pts.get(i).ID);
        }
    }
    @Test
    public void testGetFltPlan() {
        plans = fltMng.getPlanData();
        for (int i = 0; i < 10; i++) {
            System.out.println(plans.get(i).getFltID());
        }
        System.out.println(plans.get(0).getSidPath() + " " + plans.get(0).getStarPath() + plans.get(0).getFltPath());
    }
    @Test
    public void testGetRoutes() {
        FlightPlan fltPlan  = new FlightPlan();
        fltPlan.setFltID("CDG4817");
        fltPlan.setAcftType("B738");
        fltPlan.setToTime(40346);
        fltPlan.setSidPath("ZSJN01-SID-WFG_61X");
        fltPlan.setStarPath(null);
        fltPlan.setFltPath("zsjn-zsyt");
        Route r = fltMng.getRoutes(fltPlan);
        System.out.println(r.SID.size() + " " + r.STAR.size() + " " + r.enRoute.size());
        System.out.println(r.startEnRouteAltitude + " " + r.cruiseAltitude + " " + r.endEnRouteAltitude);
    }

}

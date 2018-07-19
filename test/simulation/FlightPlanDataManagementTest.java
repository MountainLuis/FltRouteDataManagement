package simulation;

import bada.bean.FixPt;
import bada.bean.Route;
import bean.FltPlan;
import dataManager.DataAccessObject;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class FlightPlanDataManagementTest {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    @Test
    public void getFltPlan() {
        FlightPlanDataManagement fpdm = new FlightPlanDataManagement();
        List<FltPlan> planList = fpdm.getFltPlan();
        for (FltPlan p : planList) {
            System.out.println(p.flt_no);
        }
    }

    @Test
    public void routeDataServer() {
        FlightPlanDataManagement fpdm = new FlightPlanDataManagement();
        String p = "YAMGA-HKC-KAZAN-KOSHI-JEDAI-RUSAR-AKVAS-URUMA-SAPET-IGMON-BULAN-OSTAR-AISAR-DRAKE-DR1A";
        Route r = fpdm.routeDataServer(p);
        for (FixPt pt : r.enRoute) {
            System.out.println(pt.ID + " " + pt.latitude);
        }
    }
    @Test
    public void testAllPlanPath() {
        FlightPlanDataManagement fpdm = new FlightPlanDataManagement();
        List<FltPlan> planList = fpdm.getFltPlan();
        for (FltPlan p : planList) {
            Route r = fpdm.routeDataServer(p.flt_path);
        }
        System.out.println(fpdm.notFoundPt.size());
        Iterator iter = fpdm.notFoundPt.iterator();
        while (iter.hasNext()){
            LOGGER.info(iter.next());
        }
        LOGGER.info("以上航路点需要检查");
    }
}
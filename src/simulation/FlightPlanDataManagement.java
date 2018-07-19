package simulation;


import bada.bean.FixPt;
import bada.bean.Route;
import bean.FltPlan;
import dataManager.DataAccessObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 此程序用于处理用于仿真的数据
 */
public class FlightPlanDataManagement {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    public Set<String> notFoundPt = new HashSet<>();
    DataAccessObject dao = new DataAccessObject();

    public List<FltPlan> getFltPlan() {
        String planTable = "plan201806_abroad";
        List<FltPlan> planList = dao.getFltPlanFromMysql(planTable);
        return planList;
    }


    public Route routeDataServer(String p) {
        String[] pts = p.split("-");
        List<FixPt> enRoute = new ArrayList<>();
        for (int i = 0; i < pts.length; i++) {
            FixPt fp = new FixPt();
            fp.ID = pts[i];
            double[] coord = dao.getFixPtCoordinate(pts[i]);
            if (coord == null) {
                notFoundPt.add(pts[i]);
                 continue;
            }
            fp.latitude = dao.getFixPtCoordinate(pts[i])[0];
            fp.longitude = dao.getFixPtCoordinate(pts[i])[1];
            enRoute.add(fp);
        }
        Route r = new Route();
        r.enRoute = enRoute;
        return  r;
    }

}

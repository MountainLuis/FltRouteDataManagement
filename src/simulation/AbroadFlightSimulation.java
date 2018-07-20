package simulation;

import bada.bean.*;
import bada.exception.IllegalRouteException;
import bada.impl.AcftTrajectory;
import bean.AcftPtTime;
import bean.FltPlan;
import dataManager.DataAccessObject;
import org.apache.log4j.Logger;
import util.JDBCHelper;

import java.io.IOException;
import java.util.*;

public class AbroadFlightSimulation extends BaseTrajectory{
    private static final Logger LOGGER = Logger.getLogger(AbroadFlightSimulation.class);
    FlightPlanDataManagement fpdm = new FlightPlanDataManagement();
    List<AcftPtTime> ptTimes = new ArrayList<>();


    public static  void main(String[] args) throws IOException{
        AbroadFlightSimulation afs = new AbroadFlightSimulation();
        afs.dealWithAllData();
        System.out.println(afs.ptTimes.size());
//        afs.storagePtTimes();
    }
    public void storagePtTimes() {
        String table = "boundries_time";
        JDBCHelper.createTable(table,'y');
        JDBCHelper.insertPtTimes(table,ptTimes);
    }


    public void dealWithAllData(){
        List<FltPlan> planList = fpdm.getFltPlan();
        System.out.println("plans: " + planList.size());

            for (FltPlan plan : planList) {
                try {
                    Set<FixPt> res = TrajectoryAcft(plan);
                }catch (IllegalRouteException e) {
                    LOGGER.error(plan.toString() + " 航路上没有足够多的点。");
//                    System.out.println(plan.toString());
                    continue;
                }
            }


    }


//    public List<AcftState> TrajectoryAcft(FltPlan fltPlan) throws IllegalRouteException {
public Set<FixPt> TrajectoryAcft(FltPlan fltPlan) throws IllegalRouteException {
        AcftPerformance performance = null;
        if (acftPerformances.containsKey(fltPlan.acft_type)) {
            performance = acftPerformances.get(fltPlan.acft_type);
        }else {
            performance = acftPerformances.get("B77W");
        }
        Route r = fpdm.routeDataServer(fltPlan.flt_path);
        if (r == null || r.enRoute.isEmpty()) {
            return  null;
        }
        List<AcftState> res = new LinkedList<>();
        Set<FixPt> pts = new HashSet<>();
        AcftTrajectory trajectory = new AcftTrajectory(performance);
        trajectory.setStartTime(Long.parseLong(fltPlan.dep_time));
        trajectory.setRoute(r);
        FixPt pt0 = r.enRoute.get(0);
        FixPt pt = null;
        while (true) {
            AcftStateDetail state = (AcftStateDetail)trajectory.nextStep();
            if (state == null) {
                break;
            }
            pt = trajectory.getTarget();
            if(pt != null && !pt.getID().equals(pt0.getID())) {
                if (fpdm.dao.isBoundriesContainPoint(pt.ID)) {
                    AcftPtTime apt = new AcftPtTime(fltPlan.flt_no, pt.ID, state.time);
                    ptTimes.add(apt);
                }
                pt0 = pt;
            }
            pts.add(pt);
            res.add(state);
        }
//        return  res;
    return pts;
    }

    public AbroadFlightSimulation() throws IOException {
    }
}

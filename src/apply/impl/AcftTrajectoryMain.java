package apply.impl;

import code.apply.bean.AcftPtTime;
import code.apply.bean.FlightPlan;
import code.apply.montecarlo.CreateAndStatistics;
import code.apply.montecarlo.RouteManagement;
import code.bada.bean.*;
import code.bada.exception.IllegalRouteException;
import code.bada.impl.AcftTrajectory;

import java.io.IOException;
import java.util.*;

public class AcftTrajectoryMain extends BaseTrajectory{
    FlightPlanManagement fltMng = null;
    RouteManagement routeMng = null;
    Map<String, Integer> passTime = null;
    public AcftTrajectoryMain() throws IOException {
        fltMng = new FlightPlanManagement();
        routeMng = new RouteManagement();
        passTime = new HashMap<>();
    }

    /**
     * 此方法用于从数据库中读取flt_plan并推算轨迹
     * @param fltPlan
     * @return
     * @throws IllegalRouteException
     */
    public List<AcftState> TrajectoryAcft(FlightPlan fltPlan) throws IllegalRouteException {
        AcftPerformance performance = acftPerformances.get(fltPlan.getAcftType());
        Route r = fltMng.getRoutes(fltPlan);

        List<AcftState> result = new LinkedList<>();
        AcftTrajectory trajectory = new AcftTrajectory(performance);
        trajectory.setStartTime(fltPlan.getToTime());
        trajectory.setRoute(r);
        while(true){
            AcftStateDetail state = (AcftStateDetail) trajectory.nextStep();
            if(state == null){
                break;
            }
            result.add(state);
        }
        return result;
    }

    /**
     *这个方法用于推算随机生成航班的轨迹
     * @param fltPlan
     * @return
     * @throws IllegalRouteException
     */
    public List<AcftState> TrajectoryAcftForMonteCarlo (FlightPlan fltPlan) throws IllegalRouteException {
        AcftPerformance performance = acftPerformances.get(fltPlan.getAcftType());
        Route r = routeMng.routes.get(fltPlan.getFltPath());
        List<AcftState> result = new LinkedList<>();
        AcftTrajectory trajectory = new AcftTrajectory(performance);
        trajectory.setStartTime(fltPlan.getToTime());
        trajectory.setRoute(r);

        FixPt pt0 = r.enRoute.get(0);
        FixPt pt = null;
        int time = 0;
        while(true){
            AcftStateDetail state = (AcftStateDetail) trajectory.nextStep();
            if(state == null){
//                recordPassTime(fltPlan.getFltID(), pt0.getID(), time);
                break;
            }
            pt = trajectory.getTarget();
            time = (int) state.time;
            if(pt != null && !pt.getID().equals(pt0.getID())) {
//                recordPassTime(fltPlan.getFltID(), pt0.getID(), time);
                pt0 = pt;
            }
            result.add(state);
        }
        return result;
    }

    /**
     * 此方法用于在预测轨迹时记录飞越航路点的时间
     * @param plan
     * @return
     * @throws IllegalRouteException
     */
    public List<AcftPtTime> TrajectoryForRecordTime(FlightPlan plan) throws IllegalRouteException {
        AcftPerformance performance = acftPerformances.get(plan.getAcftType());
        Route r = routeMng.routes.get(plan.getFltPath());

        AcftTrajectory trajectory = new AcftTrajectory(performance);
        trajectory.setStartTime(plan.getToTime());
        trajectory.setRoute(r);

        List<AcftPtTime> apts = new ArrayList<>();  //用于输出
        FixPt pt0 = r.enRoute.get(0);
        FixPt pt = null;
        int time = 0;

        while(true){
            AcftStateDetail state = (AcftStateDetail) trajectory.nextStep();
            if(state == null){
                apts.add(recordPassTime(plan.getFltID(), pt0.getID(), time, plan.getFltPath()));
                break;
            }
            pt = trajectory.getTarget();
            time = (int) state.time;
            if(pt != null && !pt.getID().equals(pt0.getID())) {
                apts.add(recordPassTime(plan.getFltID(), pt0.getID(), time, plan.getFltPath()));
                pt0 = pt;
            }
        }
        return apts;
    }
    public AcftPtTime  recordPassTime(String acftID,String pt, int time, String route) {
        AcftPtTime apt = new AcftPtTime();
        apt.acftID = acftID;
        apt.ptID = pt;
        apt.time = time;
        apt.route = route;
//        CreateAndStatistics.ptTimes.add(apt);
        return apt;
    }

//    public static void main(String[] args) throws IOException, IllegalRouteException {
//        AcftTrajectoryMain atm = new AcftTrajectoryMain();
//
//        FlightPlanManagement fpm = new FlightPlanManagement();
//        List<FlightPlan> plans = fpm.getPlanData();
//
//        long tic = System.currentTimeMillis();
//
//        for (FlightPlan plan : plans) {
////        for (int i = 0; i < 100; i++) {
////            FlightPlan plan = plans.get(i);
//            System.out.println(plan.getFltID());
//            List<AcftState> tracks = atm.TrajectoryAcft(plan);
//            atm.insertData(tracks, plan.getFltID());
//            System.out.println(plan.getFltID() + " : " + tracks.size());
//        }
//
//        long time = System.currentTimeMillis() - tic;
//        System.out.println(time);
//    }


}

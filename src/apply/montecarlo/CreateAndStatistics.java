package apply.montecarlo;

import code.apply.bean.AcftPtTime;
import code.apply.bean.FlightConflict;
import code.apply.bean.FlightPlan;
import code.apply.impl.AcftTrajectoryMain;
import code.apply.util.DataTransform;
import code.bada.bean.AcftState;
import code.bada.bean.FixPt;
import code.bada.exception.IllegalRouteException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class CreateAndStatistics {

    AcftTrajectoryMain atm = new AcftTrajectoryMain();
    Map<String, List<String>> linkedRoute = null;
    Map<String, List<FlightPlan>> allPlans = null;
    List<AcftPtTime> allPtTimes = null;
    Map<String, Integer> ptFlows = null;
    List<FlightConflict> conflicts = new ArrayList<>();
    Map<String, Integer> conflictNumber = new HashMap<>();

    public CreateAndStatistics() throws IOException, SQLException, IllegalRouteException {
        linkedRoute = RouteManagement.getLinkedRoutes();
        allPlans = getAllPlans();
        allPtTimes = getAllPtTimes(DataTransform.getAcftIDFromFltPlanMap(allPlans));
        initFlow();
    }

    public Map<String, List<FlightPlan>> getAllPlans() {
        List<String> routeName = new ArrayList<>(RouteManagement.getRouteName());
        Map<String, List<FlightPlan>> allPlans = new HashMap<>();
        for (int i = 0; i < routeName.size(); i++){
//        for (int i = 0; i < 10; i++) {  //为测试，取一个小样本
            allPlans.put(routeName.get(i), AcftGenerator.routeAcftGenerator(routeName.get(i)));
        }
        return allPlans;
    }

    public Map<String, List<AcftState>> getAllTracks(List<FlightPlan> allPlans) throws IllegalRouteException {
        Map<String, List<AcftState>> allTracks = new HashMap<>();
        for(int i = 0; i < allPlans.size(); i++) {
            List<AcftState> tracks = atm.TrajectoryAcftForMonteCarlo(allPlans.get(i));
            allTracks.put(allPlans.get(i).getFltID(), tracks);
        }
        return allTracks;
    }
    public List<AcftPtTime> getAllPtTimes(List<FlightPlan> allPlans) throws IllegalRouteException {
        List<AcftPtTime> allPtTimes = new ArrayList<>();
        for(int i = 0; i < allPlans.size(); i++) {
            allPtTimes.addAll(atm.TrajectoryForRecordTime(allPlans.get(i)));
        }
        return allPtTimes;
    }

    /**
     * 初始化流量Map
     */
    public void initFlow() {
        ptFlows = new HashMap<>();
        for(String pt : linkedRoute.keySet()) {
            int initFlow = linkedRoute.get(pt).size() * 30;
            ptFlows.put(pt, initFlow);
        }
    }
    public void calFlow2(){
        int idx = 0;
        List<String> ptss = new ArrayList<>(linkedRoute.keySet());
        for (int i = 0; i < ptss.size(); i++) {
//        for(int i = 0; i < 100; i++) {
            List<FlightPlan> plans = new ArrayList<>();
            List<String> routes = linkedRoute.get(ptss.get(i));
            for(int ii = 0; ii < routes.size(); ii++) {
                plans.addAll(allPlans.get(routes.get(ii)));
            }
            System.out.println("index: " + idx + "当前计算的点为： " + ptss.get(i)+ "；相关航路有 "
                    + linkedRoute.get(ptss.get(i)).size() + "条," + "共有航班" + plans.size() +"个。" );
            idx++;
            //下面执行冲突探测
            conflictDet(plans, ptss.get(i));

        }
        doDelete();
        System.out.println(conflicts.size());
        String filename = "PtConflictNums.json";
        DataTransform.map2jsons(conflictNumber,filename);


    }
    public void conflictDet(List<FlightPlan> plans, String pt) {
        for(int i = 0; i < plans.size(); i++) {
            for(int j = i+1; j < plans.size(); j++) {
                if(plans.get(i).getFltPath().equals(plans.get(j).getFltPath())) {
                    continue;
                }
                else {
                    judgeConflict(plans.get(i).getFltID(), plans.get(j).getFltID(),pt);
                }
            }
        }

    }



    public void calFlow(){
        int idx = 0;
        List<String> ptss = new ArrayList<>(linkedRoute.keySet());
        for(String pt: linkedRoute.keySet()) {
//        for(int ii = 0; ii < 100; ii++) {
//                String pt = ptss.get(ii);
            List<FlightPlan> plans = new ArrayList<>();
            System.out.println("index: " + idx + "当前计算的点为： " + pt + "；相关航路有 " + linkedRoute.get(pt).size() + "条。");
            idx++;
            if (linkedRoute.get(pt).size() > 1) { //对每个点进行判断，找出存在冲突的航班并处理
                List<String> routes = linkedRoute.get(pt);
                for (int i = 0; i < routes.size(); i++) {
                    plans.addAll(allPlans.get(routes.get(i)));
                }
    System.out.println("Total plans: " + plans.size());
                for (int i = 0; i < plans.size(); i++){
                    for (int j = i + 1; j < plans.size(); j++) {
                        if (plans.get(i).getFltPath().equals(plans.get(j).getFltPath())) {
                            continue;
                        }else {
                            judgeConflict(plans.get(i).getFltID(), plans.get(j).getFltID(),pt);
                        }
                    }
                }
            } else {
                continue;
            }
        }
//        doDelete();



    }
    public void doDelete() {
        Map<String, Integer> deleteRoutes = getRouteDelete();
        String filename = "dodeleteRoute.json";
        DataTransform.map2jsons(deleteRoutes, filename);
        for(String route : deleteRoutes.keySet()) {
//            List<FixPt> pts = RouteManagement.routes.get(route).enRoute;
//            for (FixPt pt : pts) {
//                ptFlows.put(pt.ID, (ptFlows.get(pt.ID) - deleteRoutes.get(pt.ID)));
//            }
//            System.out.println("route :"+ route + ", number: "+deleteRoutes.get(route));
        }
        System.out.println("涉及航路： " + deleteRoutes.size());

     }

    public Map<String, Integer> getRouteDelete() {
        Map<String, Integer> deleteRoute = new HashMap<>();
        for(FlightConflict fc : conflicts) {
            String str = fc.route1;
            if(deleteRoute.get(str) == null) {
                deleteRoute.put(str, 1);
            } else {
                int in = deleteRoute.get(str) + 1;
                deleteRoute.put(str, in);
            }
        }
        return deleteRoute;
    }


//    public List<String[]> getdeleteAcft() {
//        Set acfts = new HashSet<>();
//        for(FlightConflict fc : conflicts) {
//            String[] res = {fc.acft1, fc.route1};
//            acfts.add(Arrays.asList(res));
//        }
//        List<String[]> result = new ArrayList<>(acfts);
//        return result;
//    }
    public void deleteAcftPlan(String route, String acft) {
        Iterator<FlightPlan> iter = allPlans.get(route).iterator();
        while(iter.hasNext()) {
            FlightPlan fp = iter.next();
            if(fp.getFltID().equals(acft)) {
                iter.remove();
            }
        }
    }
    public void countPlus(String pt) {
        if(conflictNumber.get(pt) == null) {
            conflictNumber.put(pt, 1);
        } else {
            conflictNumber.put(pt, conflictNumber.get(pt) + 1);
        }


    }

    public void judgeConflict(String acft1, String acft2, String pt) {
        AcftPtTime apt1 = getPtTimeFromList(acft1, pt);
        AcftPtTime apt2 = getPtTimeFromList(acft2, pt);
        if(apt1.time >= apt2.time && (apt1.time - apt2.time) <= 60) {
            FlightConflict fc = new FlightConflict();
            fc.pt = pt;
            fc.acft1 = acft1; fc.time1 = apt1.time;fc.route1 =  apt1.route;
            fc.acft2 = acft2; fc.time2 = apt2.time;
            conflicts.add(fc);
            countPlus(pt);
//            deleteAcftPlan(apt1.route,acft1);
        } else if (apt1.time <= apt2.time && (apt2.time - apt1.time) <= 60) {
            FlightConflict fc = new FlightConflict();
            fc.pt = pt; fc.acft1 = acft2; fc.time1 = apt2.time; fc.route1 = apt2.route;
            fc.acft2 = acft1; fc.time2 = apt1.time;
            conflicts.add(fc);
            countPlus(pt);
//            deleteAcftPlan(apt2.route,acft2);
        }
    }

    public AcftPtTime getPtTimeFromList(String acft, String pt) {
        AcftPtTime apt = null;
        for (int i = 0; i < allPtTimes.size(); i++){
            if(allPtTimes.get(i).acftID.equals(acft) && allPtTimes.get(i).ptID.equals(pt)) {
                apt = allPtTimes.get(i);
            }
        }
        if (apt == null) {
            System.out.println(acft + " 不经过 " + pt + "点。");
        }
        return apt;
    }
    public int getPlanInAList() {
        List<FlightPlan> plans = new ArrayList<>();
        for(String route: allPlans.keySet()){
            plans.addAll(allPlans.get(route));
        }
        return plans.size();
    }

}

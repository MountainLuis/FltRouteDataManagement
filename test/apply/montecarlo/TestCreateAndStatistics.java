package apply.montecarlo;

import code.apply.bean.AcftPtTime;
import code.apply.bean.FlightConflict;
import code.apply.bean.FlightPlan;
import code.apply.util.DataTransform;
import code.bada.bean.AcftState;
import code.bada.exception.IllegalRouteException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class TestCreateAndStatistics {

    @Test
    public void testGetAllPlans() throws IOException, IllegalRouteException, SQLException {
        CreateAndStatistics cas = new  CreateAndStatistics();
        Map<String, List<FlightPlan>> allPlans = cas.getAllPlans();
        for(String route : allPlans.keySet()) {
            int i = 0;
            for(FlightPlan plan : allPlans.get(route)){
                i++;
                System.out.print(plan.getFltID() + " ");
                if(i % 10 == 0){
                    System.out.println();
                }
            }
            System.out.println();
        }
    }
    @Test
    public void testGetAllTracks() throws IOException, IllegalRouteException, SQLException {
        CreateAndStatistics cas = new  CreateAndStatistics();
        List<FlightPlan> allFltIds = DataTransform.getAcftIDFromFltPlanMap(cas.getAllPlans());
        Map<String, List<AcftState>> allTracks = cas.getAllTracks(allFltIds);
        for(String fltID : allTracks.keySet()){
            System.out.println(fltID + " " + allTracks.get(fltID).size());
        }
    }
    @Test
    public void testGetAllPtTimes() throws IOException, IllegalRouteException, SQLException {
        CreateAndStatistics cas = new CreateAndStatistics();
        List<FlightPlan> allFltIds = DataTransform.getAcftIDFromFltPlanMap(cas.getAllPlans());
        List<AcftPtTime> allPtTimes = cas.getAllPtTimes(allFltIds);
        for(int i = 0; i < allPtTimes.size(); i++) {
            System.out.println(allPtTimes.get(i).acftID + " " + allPtTimes.get(i).ptID + " "
            + allPtTimes.get(i).time);
        }
    }

    @Test
    public void testCalFlow() throws SQLException, IOException, IllegalRouteException {
        CreateAndStatistics cas = new CreateAndStatistics();
        System.out.println("linkedRoute: " + cas.linkedRoute.size() + " \t"
        + " allPlans: " + cas.allPlans.size() + "\t"
        + " allPtTimes: " + cas.allPtTimes.size() + "\t"
        + " ptFlows: " + cas.ptFlows.size());
        cas.calFlow();
//        for(FlightConflict fc: cas.conflicts) {
            System.out.println(cas.conflicts.size());
//        }
//        for(String pt : cas.ptFlows.keySet()) {
//            System.out.println(pt + " : " + cas.ptFlows.get(pt));
//        }
        DataTransform.list2json(cas.conflicts);
//        Map<String, Integer> cofRoutes = cas.getRouteDelete();
//        DataTransform.map2jsons(cofRoutes);
//        DataTransform.map2jsons(cas.ptFlows);
    }
    @Test
    public void testCalFlow2() throws SQLException, IOException, IllegalRouteException {
        CreateAndStatistics cas = new CreateAndStatistics();
        System.out.println("linkedRoute: " + cas.linkedRoute.size() + " \t"
                + " allPlans: " + cas.allPlans.size() + "\t"
                + " allPtTimes: " + cas.allPtTimes.size() + "\t"
                + " ptFlows: " + cas.ptFlows.size());
        System.out.println(cas.getPlanInAList()+ "====================");
        cas.calFlow2();
        System.out.println("Total conflicts: " + cas.conflicts.size());
//        for(String pt : cas.ptFlows.keySet()) {
//            System.out.println(pt + " : " + cas.ptFlows.get(pt));
//        }

        System.out.println(cas.getPlanInAList() + "=================");
        System.out.println("linkedRoute: " + cas.linkedRoute.size() + " \t"
                + " allPlans: " + cas.allPlans.size() + "\t"
                + " allPtTimes: " + cas.allPtTimes.size() + "\t"
                + " ptFlows: " + cas.ptFlows.size());
    }




    @Test
    public void testGetPtTimeFromList() throws SQLException, IOException, IllegalRouteException {
        CreateAndStatistics cas = new CreateAndStatistics();
        AcftPtTime apt = cas.getPtTimeFromList("H24-0", "P264");
        System.out.println(apt.acftID + " " + apt.time);

    }

    @Test
    public void testSetWithStringArray() {
        Set strArray = new HashSet<>();
        String[] res = {"Hello", "kkkkkkk"};
        String[] res2 = {"Hello", "kkkkkkk"};
        String[] res3 = {"Hello", "aaaaaaa"};
        String[] res4 = {"ALiBABA", "kkkkkkk"};
        strArray.add(Arrays.asList("Hello", "kkkkkkk"));
        strArray.add(Arrays.asList(res3));
        strArray.add(Arrays.asList(res2));
        strArray.add(Arrays.asList(res));
        System.out.println(strArray.size());
        System.out.println(strArray);
    }
    @Test
    public void testSetWithString(){
        Set<String> strs = new HashSet<>();
        String a = "aaa";
        String a2 = "aaa";
        String a3 = "aaa";
        strs.add(a);
        strs.add(a2);
        strs.add(a3);
        System.out.println(strs.size());
    }

    @Test
    public void testDeleteAcftPlan() throws SQLException, IOException, IllegalRouteException {
        List<FlightPlan> plans01 = AcftGenerator.routeAcftGenerator("A593");
        List<FlightPlan> plans02 = AcftGenerator.routeAcftGenerator("A1");
        Map<String, List<FlightPlan>> allPlans = new HashMap<>();
        allPlans.put("A593", plans01);
        allPlans.put("A1", plans02);
        CreateAndStatistics cas = new CreateAndStatistics();
        for(FlightPlan plan: allPlans.get("A593")) {
            System.out.println(plan.getFltID());
        }
//        cas.deleteAcftPlan(allPlans,"A593", "A593-24");

        for(FlightPlan plan: allPlans.get("A593")) {
            System.out.println(plan.getFltID());
        }

    }
}

package dataDump;

import bean.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DealWithNewNaipDataTest {

    @Test
    public void printMap() {
    }

    @Test
    public void dealWithAllData() {
    }

    @Test
    public void spliceRouteLeg() {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
//        List<String> legs = dnnd.routeRelation.get("14649");
//        List<Point> pts = dnnd.spliceRouteLeg(legs);
//        System.out.println(pts.size());
//        for(int i = 0; i < pts.size(); i++) {
//            System.out.println(pts.get(i).name + " " + i);
//        }
    }

    @Test
    public void getRouteInfo() {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
        System.out.println(dnnd.routeRelation.size());
        System.out.println(dnnd.routeLeg.size());
    }

    @Test
    public void getRelationInfo() {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
//        Map<String, List<String>> routeRelation = dnnd.getRelationInfo();
//        for (String s : routeRelation.keySet()) {
//            if (dnnd.routeInfo.get(s).equals("H11") ){
//                for (String ss : routeRelation.get(s)) {
//                    System.out.println(ss);
//                }
//            System.out.println(dnnd.routeInfo.get(s) + " " + routeRelation.get(s).size());
//        }
//        }
    }

    @Test
    public void getRouteLegInfo() {
    }

    @Test
    public void getPointInfo() {
        DealWithNewNaipData dnnd = new DealWithNewNaipData();
        Map<String, Point> pointInfo = dnnd.getPointInfo();
//        for (String r : pointInfo.keySet()) {
//            if (pointInfo.get(r).name.equals("CGA") || pointInfo.get(r).name == null) {
//                System.out.println(r + " " + pointInfo.get(r).name + " "  + pointInfo.get(r).longitude);
//            }
//        }
        System.out.println(pointInfo.size());
    }
    @Test
    public void testList() {
        String[] rePoints = new String[]{"DO","CH","UF","HG","RL","G","YU","PA","CD","QM","WB","KM","FK","FY"};
        List<String> repeatpoints = Arrays.asList(rePoints);
        repeatpoints.set(2,"PPP");
        for (String s : repeatpoints) {
            System.out.println(s);
        }
    }


}
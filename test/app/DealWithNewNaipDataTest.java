package app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DealWithNewNaipDataTest {

    @Test
    public void getRouteInfo() {
        DealWithNewNaipData dnn = new DealWithNewNaipData();
        Map<String , String> routeMap = dnn.getRouteInfo();
        for (String s : routeMap.keySet()) {
            System.out.println(s + " " + routeMap.get(s));
        }
    }

    @Test
    public void getRelationInfo() {
        DealWithNewNaipData dnn = new DealWithNewNaipData();
        Map<String , List<String>> routeRelation = dnn.getRelationInfo();
//        for (String s : routeRelation.keySet()) {
//            System.out.print(s + " : ");
//            for (String leg : routeRelation.get(s)) {
//                System.out.print(leg + " ");
//            }
//            System.out.println();
//        }
        String r = "14594";
        for (String leg : routeRelation.get(r)) {
            System.out.println(leg);
        }
    }

    @Test
    public void getRouteLegInfo() {
        DealWithNewNaipData dnn = new DealWithNewNaipData();
        Map<String, String[]> legs = dnn.getRouteLegInfo();
        for (String leg : legs.keySet()) {
            System.out.println(leg + " " + legs.get(leg)[0] + ":" + legs.get(leg)[1]);
        }

    }

    @Test
    public void spliceRouteLeg() {
        DealWithNewNaipData dnn = new DealWithNewNaipData();
        List<String> legs = new ArrayList<>();
        legs.add("13706");
        legs.add("13923");
        legs.add("13924");
        legs.add("13710");
        legs.add("16155");
        legs.add("16156");
        List<String> res = dnn.spliceRouteLeg(legs);
        for (String s : res) {
            System.out.println(s);
        }

    }

    @Test
    public void getPointInfo() {
        DealWithNewNaipData dnn = new DealWithNewNaipData();
         System.out.println(dnn.pointInfo.keySet().size());
        for (String s : dnn.pointInfo.keySet()) {
            System.out.println(s + " " + dnn.pointInfo.get(s));
        }
//

    }

//    @Test
//    public void getID() {
//        DealWithNewNaipData dnn = new DealWithNewNaipData();
//        String s1 = "铜山-VOR_DME";
//        String s2 = "无极-NDB";
//        String s3 = "P38";
//        System.out.println(dnn.getID(s1));
//        System.out.println(dnn.getID(s2));
//        System.out.println(dnn.getID(s3));
//    }
}
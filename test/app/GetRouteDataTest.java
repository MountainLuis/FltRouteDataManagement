package app;

import bean.PointInfo;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetRouteDataTest {

    @Test
    public void getDataFromSql() {
        GetRouteData  grd = new GetRouteData();
        Map<String, List<PointInfo>> routeMap = grd.getDataFromSql();
        System.out.println(routeMap.keySet().size());
        Iterator iter = routeMap.keySet().iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }
    @Test
    public void getRouteData() {
        GetRouteData  grd = new GetRouteData();
        System.out.println(grd.routeMap.keySet().size());
    }

    @Test
    public void getPtSeq() {
        GetRouteData grd = new GetRouteData();
        List<PointInfo> pList = grd.getPtSeq("Y346");
        for (PointInfo s : pList) {
            System.out.println(s.fix_pt + " : " + s.idx);
        }

    }

    @Test
    public void getSubPtSeq() {
        GetRouteData grd = new GetRouteData();
        String r = "A1";
        String start = "FENIX";
//        String start = "SUC";
        String end = "SUC";
//        String end = "OSTAR";
//        String end = "SUC";
//        String end = "FENIX";
        List<PointInfo> pList = grd.getSubPtSeq(r, start, end);
        for (PointInfo s : pList) {
            System.out.println(s.fix_pt + " : " + s.idx);
        }
    }
}
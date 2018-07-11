package app;

import bean.FltPlan;
import bean.PointInfo;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DAOTest {

    @Test
    public void getDataFromSql() {
        DataAccessObject  grd = new DataAccessObject();
        Map<String, List<PointInfo>> routeMap = grd.getRouteData();
        System.out.println(routeMap.keySet().size());
        Iterator iter = routeMap.keySet().iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }
    @Test
    public void getRouteData() {
        DataAccessObject  grd = new DataAccessObject();
        System.out.println(grd.routeMap.keySet().size());
    }

    @Test
    public void getPtSeq() {
        DataAccessObject grd = new DataAccessObject();
        List<PointInfo> pList = grd.getPtSeq("Y346");
        for (PointInfo s : pList) {
            System.out.println(s.fix_pt + " : " + s.idx);
        }

    }
    @Test
    public void getPtSeqNaip() {
        DataAccessObject grd = new DataAccessObject();
        List<PointInfo> pList = grd.getPtSeqNaip("Y475");
        for (PointInfo s : pList) {
            System.out.println(s.fix_pt + " : " + s.idx);
        }

    }

    @Test
    public void getSubPtSeq() {
        DataAccessObject grd = new DataAccessObject();
        String r = "A1";
//        String start = "FENIX";
        String start = "SUC";
//        String end = "SUC";
        String end = "OSTAR";
//        String end = "SUC";
//        String end = "FENIX";
        List<PointInfo> pList = grd.getSubPtSeq(r, start, end, 1);
        for (PointInfo s : pList) {
            System.out.println(s.fix_pt + " : " + s.idx);
        }
    }

    @Test
    public void getNaipData() {
        DataAccessObject dao = new DataAccessObject();
        Map<String, List<PointInfo>> naipData = dao.getNaipData();
        System.out.println(naipData.size() + "====================");
        Iterator iter = naipData.keySet().iterator();
        int i = 1;
        while(iter.hasNext()) {
            System.out.println(iter.next() + " " + i++);
        }
    }

    @Test
    public void getFltPlan() {
        DataAccessObject dao = new DataAccessObject();
        String time = "20180601";
        List<FltPlan> plans = dao.getFltPlan(time);
        for (FltPlan fp : plans) {
            System.out.println(fp.flt_no);
        }

    }
}
package dataManager;

import bean.FltPlan;
import bean.PointInfo;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataAccessObjectTest {

    @Test
    public void getRouteData() {
        DataAccessObject dao = new DataAccessObject();
        Map<String, List<PointInfo>> rMap = dao.getRouteData();
        Iterator iter = rMap.keySet().iterator();
        for (int i = 0; i < 10; i++) {
            System.out.println(iter.next());
        }
    }

    @Test
    public void getNaipData() {
        DataAccessObject dao = new DataAccessObject();
        Map<String, List<PointInfo>> rMap = dao.getNaipData();
        Iterator iter = rMap.keySet().iterator();
        for (int i = 0; i < 10; i++) {
            System.out.println(iter.next());
        }
    }

    @Test
    public void getPtSeq() {
        DataAccessObject dao = new DataAccessObject();
        String r = "A1";
        List<PointInfo> pList = dao.getPtSeq(r);
        for (PointInfo pi : pList) {
            System.out.println(pi.fix_pt);
        }
    }

    @Test
    public void getPtSeqNaip() {
        DataAccessObject dao = new DataAccessObject();
        String r = "A1";
        List<PointInfo> pList = dao.getPtSeqNaip(r);
        for (PointInfo pi : pList) {
            System.out.println(pi.fix_pt);
        }
    }

    @Test
    public void getSubPtSeq() {
        DataAccessObject dao = new DataAccessObject();
        String r = "A1";
        String startPt = "BUNTA";
        String endPt = "IKELA";
        List<PointInfo> pList = dao.getSubPtSeq(r, startPt, endPt, 0);
        for (PointInfo pi : pList) {
            System.out.println(pi.fix_pt);
        }
    }

    @Test
    public void getFltPlanFromAccess() {
        DataAccessObject dao = new DataAccessObject();
        List<FltPlan> plans = dao.getFltPlanFromAccess("201706");
        if (plans == null || plans.isEmpty()) {
            System.out.println("error");
        }
        for (FltPlan plan :plans) {
            System.out.println(plan.flt_no);
        }
    }

    @Test
    public void getFltPlanFromMysql() {
        DataAccessObject dao = new DataAccessObject();
        String table = "plan201806_abroad";
        List<FltPlan> plans = dao.getFltPlanFromMysql(table);
        for (FltPlan plan :plans) {
            System.out.println(plan.flt_no);
        }
    }

    @Test
    public void getAllPtMap() {
        DataAccessObject dao = new DataAccessObject();
        Map<String, double[]> pMap = dao.getAllPtMap();
        Iterator iter = pMap.keySet().iterator();
        for (int i = 0; i < 15;i++) {
            System.out.println(iter.next());
        }
    }

    @Test
    public void getFixPtCoordinate() {
        DataAccessObject dao = new DataAccessObject();
        String pt = "ZUKEP";
        System.out.println(dao.getFixPtCoordinate(pt)[0] + " " + dao.getFixPtCoordinate(pt)[1]);

    }

    @Test
    public void isRouteMapContainKey() {
        DataAccessObject dao = new DataAccessObject();
        String r = "A1";
        System.out.println(dao.isRouteMapContainKey(r));
    }

    @Test
    public void isNaipMapContainKey() {
        DataAccessObject dao = new DataAccessObject();
        String r = "A1";
        System.out.println(dao.isNaipMapContainKey(r));
    }
}
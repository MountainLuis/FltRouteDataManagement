package dataManager;

import bean.FltPlan;
import bean.PointInfo;
import org.junit.Test;

import java.util.*;

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
        for (String r : rMap.keySet()) {
           for (PointInfo pi : rMap.get(r)){
               System.out.println(pi.fix_pt + " " + r);
           }
        }
//        for (PointInfo pi : rMap.get("H4")) {
//            System.out.println(pi.fix_pt);
//        }
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
        String r = "A368";
        String startPt = "P221";
        String endPt = "FKG";
        for (int i = 0; i < 2000; i++) {
            List<PointInfo> pList = dao.getSubPtSeq(r, startPt, endPt, 0);
            System.out.println(addToString(pList));
        }
//        List<PointInfo> pList = dao.getSubPtSeq(r, startPt, endPt, 0);
//        for (PointInfo pi : pList) {
//            System.out.println(pi.fix_pt + " " + pi.enRoute);
//        }
    }
    public String addToString(List<PointInfo> pList){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pList.size(); i++) {
            PointInfo pi = pList.get(i);
            res.append(pi.fix_pt);
            if (i != pList.size() - 1) {
                res.append("-");
            }
        }
        return res.toString();
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
        Set<String> types = new HashSet<>();
        for (FltPlan plan :plans) {
            if (plan.acft_type.equals("")) {
                System.out.println("这个没机型 " + plan.flt_no);
            }
            types.add(plan.acft_type);
//            System.out.println(plan.acft_type);
        }
        System.out.println("TYPE : " + types.size());
        for (String type : types) {
            System.out.println(type);
        }
    }
    @Test
    public void getAllNaipPtMap() {
        DataAccessObject dao = new DataAccessObject();
        Map<String,double[]> ptmap = dao.getAllNaipPtMap();
        System.out.println(ptmap.size());
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

    @Test
    public void testFltPlan() {
        DataAccessObject dao = new DataAccessObject();
        String table = "fme201806_domestic_replace";
        List<FltPlan> res = dao.getStandardFltPlanFromMySQL(table, "");
        for (FltPlan plan : res) {
//            if (plan.flt_path.equals("")) {
            if (plan.flt_path == null) {
                System.out.println(plan.flt_no);
            }
        }
    }
}
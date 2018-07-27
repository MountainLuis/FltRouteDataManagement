package dataManager;

import bean.FltPlan;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 用于检查国外航班情况的
 */
public class DataCheck {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    DataAccessObject dao = new DataAccessObject();
    public static void main(String[] args) {
        DataCheck dc = new DataCheck();
        List<FltPlan> planList = dc.getFltPlan2();
        Set<String> ods = new HashSet<>();
        Set<String> aips = new HashSet<>();
        for (FltPlan plan : planList) {
            String od = plan.to_ap + "-" + plan.ld_ap;
            ods.add(od);
            aips.add(plan.to_ap);
            aips.add(plan.ld_ap);
        }
        Iterator iter = aips.iterator();
        while (iter.hasNext()) {
            LOGGER.info(iter.next());
        }
        System.out.println(aips.size());
    }
        /*
        DataCheck dc = new DataCheck();
//        List<FltPlan> planList = dc.getFltPlan();
        List<FltPlan> planList = dc.getFltPlan2();
        List<FltPlan> res = new ArrayList<>();
        System.out.println("total plans: " + planList.size());
//        int i = 0;
        int countBDR = 0;
        int countNBDR = 0;
        for (FltPlan plan : planList) {
            String bp = dc.splitPath2(plan.flt_path);
            if (bp != null) {
                countBDR++;
//                LOGGER.info(plan.flt_no + "-" + plan.dep_time + "-" + bp);
                System.out.println(plan.flt_no + " " + bp);
            } else {
                countNBDR++;
                res.add(plan);
//                LOGGER.info(plan.flt_no + "-" + plan.dep_time + "-" + bp);
                System.out.println("!!!" + plan.flt_no + " no boundries.");
            }
//            if (i++ == 1000) {
//                break;
//            }
        }
//        List<FltPlan> plans = new ArrayList<>();
//        for (FltPlan plan : res) {
//            if (plan.to_ap.equals("VHHH") || plan.to_ap.equals("VMMC")) {
//                if (plan.ld_ap.startsWith("Z")) {
//                    continue;
//                } else {
//                    plans.add(plan);
//                }
//            } else {
//                plans.add(plan);
//            }
//        }
        System.out.println("有边界点的计划： " + countBDR + " ; 没有的：" + countNBDR);
        String table = "NoBoundriesPlan_four";
        dc.dao.storageFltPlan(table,res);
    }
    */


    public List<FltPlan> getFltPlan2() {
//        String table = "noboundriesplan_new";
        String table = "noboundriesplan_four";
        List<FltPlan> planList = dao.getStandardFltPlanFromMySQL(table,"");
        return planList;
    }

    public List<FltPlan> getFltPlan() {
        String table = "fme201806";
        List<FltPlan> planList = dao.getSelectedFltPlan(table,"");
        return planList;
    }
    public String splitPath(String path){
//        String[] pts = path.split("\\s+");
        String[] pts = path.split("-");
        for (int i = 0; i < pts.length; i++) {
//            if (pts[i].contains("/")) {
//                String p = pts[i].split("/")[0];
//                if (dao.isBoundriesContainPoint(p)) {
////                    System.out.println(p + " is boundry point");
//                    return p;
//                }else {
//                    continue;
//                }
//            } else  {
            if (dao.isBoundriesContainPoint(pts[i])) {
//                    System.out.println(pts[i] + " is boundry point");
                return pts[i];
            }else {
                continue;
            }
//            }
        }
        return null;
    }
    public String splitPath2(String path){
         String[] pts = path.split("-");
        for (int i = 0; i < pts.length; i++) {
            if (dao.isNaipPoints(pts[i])) {
//                    System.out.println(pts[i] + " is boundry point");
                return pts[i];
            }else {
                continue;
            }
//            }
        }
        return null;
    }


}

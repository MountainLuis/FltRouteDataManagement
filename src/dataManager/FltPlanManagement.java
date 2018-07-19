package dataManager;

import bean.FltPlan;
import util.MysqlHelper;

import java.util.ArrayList;
import java.util.List;

public class FltPlanManagement {
//    public static RouteManager rm = new RouteManager();
    public static ODRouteManager orm = new ODRouteManager();
//    public static DataAccessObject dao = new DataAccessObject();

//    public static void main(String[] args) {
//        FltPlanManagement fpm = new FltPlanManagement();
//        long time = 20180605;
//        for (int i = 0; i < 30; i++) {
//            String s = String.valueOf((time + i));
//            List<FltPlan> plans = rm.dao.getFltPlan(s);
//            plans = fpm.changePaths(plans);
//            fpm.storagePlans(s, plans);
//            System.out.println(s + "Used time:" + (System.currentTimeMillis() - time) / 1000);
//        }
//    }
    public static  void main(String[] args){
        FltPlanManagement fpm = new FltPlanManagement();
//        List<FltPlan> plans = orm.dao.getFltPlan();
//        plans = fpm.changePath(plans);
//        fpm.storagePlans(plans);
        System.out.println("Done.");
    }



//    public List<FltPlan> changePaths(List<FltPlan> plans) {
//        List<FltPlan> pList = new ArrayList<>();
//        for (FltPlan plan : plans) {
//            String path = rm.transferFltPath(plan.flt_path, plan.to_ap,plan.ld_ap);
//            plan.flt_path = path;
//            pList.add(plan);
//        }
//        return pList;
//    }
    public List<FltPlan> changePath(List<FltPlan> plans) {
        List<FltPlan> pList = new ArrayList<>();
        for (FltPlan plan : plans) {
            String path = orm.addToString(orm.splitRoute(plan.flt_path));
            plan.flt_path = path;
            pList.add(plan);
        }
        return pList;
    }
    /**
     * 把处理完的计划数据存储到对应的表中。
     * @param time
     * @param plans
     */
    public void storagePlans(String time, List<FltPlan> plans) {
        String table = "plan" + time;
        MysqlHelper.createTable(table);
        MysqlHelper.insertPlanTable(table, plans);
    }
    public void storagePlans(List<FltPlan> plans) {
        String table = "plan201706_Abroad";
        MysqlHelper.createTable(table);
        MysqlHelper.insertPlanTable(table, plans);
    }

}

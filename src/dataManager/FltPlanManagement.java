package dataManager;

import bean.FltPlan;
import util.MysqlHelper;

import java.util.ArrayList;
import java.util.List;

public class FltPlanManagement {
//    public static RouteManager rm = new RouteManager();
    public static ODRouteManager orm = new ODRouteManager();
    public static  void main(String[] args){
        FltPlanManagement fpm = new FltPlanManagement();
        List<FltPlan> plans = orm.dao.getStandardFltPlanFromMySQL("noboundriesplan","");
        plans = fpm.changePath(plans);
        fpm.storagePlans(plans);
        System.out.println("Done.");
        int i = 0;
        for (FltPlan plan : plans) {
            System.out.println(plan.flt_no + " : " +plan.flt_path);
            if (i++ == 100) {
                break;
            }
        }
    }

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
      * @param plans
     */
//    public void storagePlans(String time, List<FltPlan> plans) {
//        String table = "plan" + time;
//        MysqlHelper.createTable(table);
//        MysqlHelper.insertPlanTable(table, plans);
//    }
    public void storagePlans(List<FltPlan> plans) {
        String table = "noboundriesPlan_new";
        orm.dao.storageFltPlan(table, plans);
    }

}

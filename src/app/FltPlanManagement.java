package app;

import bean.FltPlan;
import util.MysqlHelper;

import java.util.List;

public class FltPlanManagement {

    public static void main(String[] args) {
        long time = 20180601;
        for (int i = 0; i < 30; i++) {
            String s = String.valueOf((time + i));
            System.out.println(s);
        }
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

}

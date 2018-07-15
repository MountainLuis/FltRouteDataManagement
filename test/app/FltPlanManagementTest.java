package app;

import bean.FltPlan;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FltPlanManagementTest {

    @Test
    public void changePaths() {
        DataAccessObject dao = new DataAccessObject();
        FltPlanManagement fpm = new FltPlanManagement();
        String time = "20180605";
        List<FltPlan> plans = dao.getFltPlan(time);
//        plans = fpm.changePaths(plans);
        for (FltPlan p : plans) {
            System.out.println(p.flt_no + " " + p.flt_path);
        }

    }

    @Test
    public void storagePlans() {
    }

    @Test
    public void changePath() {
        FltPlanManagement fpm = new FltPlanManagement();
        List<FltPlan> plans = fpm.orm.dao.getFltPlan();
        plans = fpm.changePath(plans);
        for (FltPlan p : plans) {
            System.out.println(p.flt_no + " " + p.flt_path);
        }

    }
}
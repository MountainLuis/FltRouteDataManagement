package flightplan2017manager;

import bean.FltPlan;
import dataManager.DataAccessObject;

import java.util.List;

public class FlightPlanManagement {
    DataAccessObject dao = new DataAccessObject();

    public List<FltPlan> getFltPlan(String time) {
       String table = "fme201706_copy";
       List<FltPlan> fltPlanList = dao.getSelectedFltPlan(table, time);
       return fltPlanList;
    }
    public void storageFltPlan(String time, List<FltPlan> planList) {
        String table = "flightplan_" + time;
        System.out.println(dao.storageFltPlan(table,planList));
    }


}

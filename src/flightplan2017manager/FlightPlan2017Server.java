package flightplan2017manager;

import bada.bean.FixPt;
import bean.FltPlan;
import bean.PointInfo;

import java.util.ArrayList;
import java.util.List;

public class FlightPlan2017Server {

    public void dealWithPlan(FltPlan plan) {
        String pattern = "^Z[A-Z]*$";
        if ((plan.to_ap.matches(pattern))) {
            if (plan.ld_ap.matches(pattern)) {
                //todo 国内起落，检查航路后保存
            }else {
                //todo 国内飞国际，截取航路
            }
        } else {
            if (plan.ld_ap.matches(pattern)){
                //todo 国外飞国内，截取航路，替换起飞时间
            } else {
                //todo 国外飞国外，截断起落；
            }
        }
    }

}

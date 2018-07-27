package flightplan2017manager;

import bada.bean.FixPt;
import bean.FltPlan;
import bean.PointInfo;
import dataManager.DataAccessObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightPlan2017Server {
    DataAccessObject dao = new DataAccessObject();
    public FltPlan dealWithPlan(FltPlan plan) {
        String pattern = "^Z[A-Z]*$";
        if ((plan.to_ap.matches(pattern))) {
            if (plan.ld_ap.matches(pattern)) {
                return plan;
            }else {
                String path = cutPath(plan.flt_path);
                plan.flt_path = path;
                return plan;
            }
        } else {
            if (plan.ld_ap.matches(pattern)){

                return plan;
                //todo 国外飞国内，截取航路，替换起飞时间
            } else {
                //todo 国外飞国外，截断起落；
                return plan;
            }
        }
    }
    public String cutPath(String path) {
        String[] points = path.trim().split("\\s+");
        List<String> pts = new ArrayList(Arrays.asList(points));
        for (int i = 0; i < points.length; i++) {
            if (dao.isBoundriesContainPoint(points[i])) {
                pts = pts.subList(0,i);
            }
        }
        String res = addToString(pts);

        return res;
    }
    public String addToString(List<String> pList){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pList.size(); i++) {
            res.append(pList.get(i));
            if (i != pList.size() - 1) {
                res.append("-");
            }
        }
        return res.toString();
    }



}

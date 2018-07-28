package dataManager;

import bean.FltPlan;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FltPlanSelectorTest {

    @Test
    public void changeFltRoute() {

    }

    @Test
    public void addToString() {
    }

    @Test
    public void splitRoute() {
        FltPlanSelector fps = new FltPlanSelector();
//        List<FltPlan> res = fps.getFltPlans();
        FltPlan plan = new FltPlan();
        plan.flt_no = "CCA9987";
        plan.flt_path = "KLM H73 P221 A368 FKG W188 GOVSA W66 GOBIN B330 XIXAN  H14 HO";
        String path = "KLM-P221-P220-UGPEL-VARMI-FKG-ADPET-DAKPA-PORUP-BATUS-LIKMI-GOVSA-JNQ-P141-GOBIN-P435-YBL-P92-AKMAT-JTA-SUNUV-XIXAN-P16-JIG-VISIN-HO";
        String path2 = "KLM-P221-VARMI-UGPEL-P220-FKG-ADPET-DAKPA-PORUP-BATUS-LIKMI-GOVSA-JNQ-P141-GOBIN-P435-YBL-P92-AKMAT-JTA-SUNUV-XIXAN-P16-JIG-VISIN-HO";
        for (int i = 0; i < 2000; i++) {
//            FltPlan plan = res.get(i);
            String p = fps.addToString(fps.splitRoute(plan));
            if ( !p.equals(path)) {
                System.out.println(i + " " + p);
            }
        }
    }
    @Test
    public void testList() {
       String start = " BATUS", end = "FKG";
        for (int i = 0; i < 1000; i++) {
         List<String> res = getSub(start,end);
         printStrList(res);
        }
    }
    private void printStrList(List<String> res ) {
        for (String str : res) {System.out.print(str + " ");} System.out.println();
    }
    public List<String> getSub(String start, String end) {
        String path = "KLM-P221-P220-UGPEL-VARMI-FKG-ADPET-DAKPA-PORUP-BATUS-LIKMI-GOVSA-JNQ-P141-GOBIN-P435-YBL-P92-AKMAT-JTA-SUNUV-XIXAN-P16-JIG-VISIN-HO";
        String[] tmp = path.split("-");
        List<String> points = Arrays.asList(tmp);
        int o = -1, d = -1;
        for (int i = 0; i < points.size(); i++) {
            if (start.equals(points.get(i))) {
                o = i;
            }
            if (end.equals(points.get(i))) {
                d = i;
            }
        }
        if (o > d) {
            int tp = o;
            o = d;
            d = tp;
            List<String> pList = points.subList(o + 1,d);
            Collections.reverse(pList);
            return pList;
        } else {
            return points.subList(o + 1, d);
        }

    }

}
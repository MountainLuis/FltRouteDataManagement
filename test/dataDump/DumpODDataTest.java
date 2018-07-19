package dataDump;

import bean.FltPlan;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DumpODDataTest {

    @Test
    public void getFltPlanData() {
        DumpODData ddd = new DumpODData();
        List<FltPlan> fltPlanList = ddd.getFltPlanData();
        for (int i = 0; i < 10; i++) {
            System.out.println(fltPlanList.get(i).flt_path);
        }
    }
    @Test
    public void testString() {
        String s = "'SULEP-P374-P322-P40-NSH'";
        String sub = s.substring(1, s.length() - 1);
        System.out.println(sub);
    }
    @Test
    public void testGetPoint() {
        DumpODData ddd = new DumpODData();
        for (String p : ddd.pointMap.keySet()) {
            System.out.println(p);
        }
        System.out.println(ddd.pointMap.size());
    }
}
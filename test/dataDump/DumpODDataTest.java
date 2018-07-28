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
        String s = "ALLEY V31 IDOSI/S789N999 P901 DCT";
        String tar = "IDOSI/*[A-Z]*[0-9]*[A-Z]*[0-9]*";
        String re = "S12 DOOOO HEHE";
        s = s.replaceAll(tar,re);
        String path = "DCT MLT DCT SAMAS A202 SIKOU V571 GAMBA/N0426F200  V571 MAPLE/N0426F200 V571 COMBI/N0421F190 V571 CANTO";
        String res = path.split("V571")[0];
//        String sub = s.substring(1, s.length() - 1);
        System.out.println(s);
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
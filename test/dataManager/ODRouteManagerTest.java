package dataManager;

import bean.PointInfo;
import org.junit.Test;
import util.MysqlHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ODRouteManagerTest {

    @Test
    public void dealWithAllPlans() {
        ODRouteManager om = new ODRouteManager();
        String r = " HLG1B HLG W4 APU L3 VIOLA R596 SULEM/K0858S0890 R596 BZ  A470 CJ/K0841S0840 " +
                "W555 KAKIS W554 LUPVI R343 SASAN";
        List<PointInfo> pList = om.splitRoute(r);
        String path = om.addToString(pList);
        String od = "RCMQ-ZSWX";
        Map<String, String > odMap = new HashMap<>();
        odMap.put(path, od);
//        MysqlHelper.insertIntoSimplePath(path, od);
        MysqlHelper.insertIntoSimplePathWithStringArrayList(odMap);
    }

    @Test
    public void addToString() {
        ODRouteManager om = new ODRouteManager();
        String r = " HLG1B HLG W4 APU L3 VIOLA R596 SULEM/K0858S0890 R596 BZ  A470 CJ/K0841S0840 " +
                "W555 KAKIS W554 LUPVI R343 SASAN";
        List<PointInfo> pList = om.splitRoute(r);
        System.out.println(om.addToString(pList));
    }

    @Test
    public void splitRoute() {
        ODRouteManager om = new ODRouteManager();
        String r = " HLG1B HLG W4 APU L3 VIOLA R596 SULEM/K0858S0890 R596 BZ  A470 CJ/K0841S0840 " +
                "W555 KAKIS W554 LUPVI R343 SASAN";
        List<PointInfo> pList = om.splitRoute(r);
        for (int i = 0; i < pList.size(); i++) {
            System.out.println(pList.get(i).fix_pt);
        }
    }
    @Test
    public void checkPoints() {

    }
}
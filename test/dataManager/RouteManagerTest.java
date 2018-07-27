package dataManager;

import bean.PointInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

public class RouteManagerTest {

    @Test
    public void splitRoute() {
        RouteManager rm = new RouteManager();
        String s = " DCT BEACH Y50 DARTS Y60 ONIKU A593 LAMEN/K0889S1040 A593   PUD A599 PLT/K0900S0780 W19 MABAG W44 IGONO DCT";
        List<PointInfo> pList = rm.splitRoute(s,1);
        System.out.println("========================================================");
        for (PointInfo pi : pList) {
            System.out.println(pi.fix_pt + " " + pi.height_cons);
        }


    }

    @Test
    public void isListEquals() {
        RouteManager rm = new RouteManager();
        ArrayList<PointInfo> pList1 = new ArrayList<>();
        ArrayList<PointInfo> pList2 = new ArrayList<>();
        ArrayList<PointInfo> pList3 = new ArrayList<>();
        ArrayList<PointInfo> pList4= new ArrayList<>();
        PointInfo p1 = new PointInfo("A");
        PointInfo p2 = new PointInfo("B");
        PointInfo p3 = new PointInfo("A1");
        PointInfo p4 = new PointInfo("A2");
        PointInfo p5 = new PointInfo("A3");
        PointInfo p6 = new PointInfo("A4");
        pList1.add(p1);pList1.add(p2);pList1.add(p3);pList1.add(p4);pList1.add(p5);pList1.add(p6);
        pList2.add(p1);pList2.add(p2);pList2.add(p3);pList2.add(p4);pList2.add(p5);pList2.add(p6);
        pList3.add(p1);pList3.add(p2);pList3.add(p3);pList3.add(p4);pList3.add(p5);
        pList4.add(p1);pList4.add(p2);pList4.add(p3);pList4.add(p6);pList4.add(p5);pList4.add(p6);

        System.out.println(rm.isListEquals(pList1, pList2));
        System.out.println(rm.isListEquals(pList1, pList3));
        System.out.println(rm.isListEquals(pList1, pList4));
    }
    @Test
    public void test() {
        String[] strs = new String[]{"A","MMMMM", "SSCDD","SDDD"};
        for (int i = 0 ; i < 4; i++) {
            if (strs[i].equals("SSCDD")) {
                Arrays.fill(strs,i,i+1,"TEST");
            }
        }
//        Arrays.fill(strs,1,2,"TEST");
        for (int i = 0 ; i < 4; i++) {
            System.out.println(strs[i]);
        }
    }
    @Test
    public void testLogger() {
        Logger logger = Logger.getLogger("chapter");
        try {
            FileHandler fileHandler = new FileHandler("d:\\log.txt");
            LogRecord lr = new LogRecord(Level.INFO, "this is a test info");
            SimpleFormatter sf = new SimpleFormatter();
            fileHandler.setFormatter(sf);
            logger.addHandler(fileHandler);
            logger.log(lr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
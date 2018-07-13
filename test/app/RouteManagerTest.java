package app;

import bean.PointInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RouteManagerTest {

    @Test
    public void splitRoute() {
        RouteManager rm = new RouteManager();
        String s = "KAMDA W129 OBLIK A461 LIG/K0888S1130 R473  NNX/K0895S1100 R473 WYN W18 NLG/K0890S1130 W18 " +
                "TAMOT/N0481F370  B330 CH DCT GRUPA/N0484F360 V5 SABNO/N0493F330 A583  MAVRA/N0481F370 A583 AKOTA " +
                "M754 VINIK/M082F370 M522  NODIN/N0479F370 M522 MAMOK/N0480F360 M522 ELANG/N0484F350 M522  GALKO GALKO3D";
        String ss = "DCT ANTRI DCT PATMA W12 PCA G221 ASUKU/N0460F370 G221 BUNTA A1 IKELA P901 IDOSI DCT ENVAR " +
                "M750 ANLOT B1 APU DCT DRAKE Q11 WP900 L4 LIPLO Y741 ATOTI Y722 SAMLO/N0453F390 Y722 OLMEN DCT";
        String sss = "DCT TOPAX Y579 CJU Z82 PANSI Y711 MUGUS Y742 SALMI Q11  GID B591 HCN G86 KAPLI " +
                "DCT RAMUS DCT ARROW DCT IDOSI/M078F320  P901 IKELA/N0475F300 A1 BUNTA/M078F300 A1 DAN DCT";
        String s1 = "YIN A461 ZHO/K0865S0920 B208 PADNO H150 BISAL W175 TYN";
        String s2 = " NOPIK1L NOPIK Y697 AGAVO/K0910S0980 G597 DONVO A326  LADIX B339 ASILA/K0905S0980 B339 " +
                "POLHO/K0901S0980 Y345 UDA A575  DARNO/K0888F340 P982 PELEK/K0886F360 P982 GIKES/K0894F380 P982" +
                "  LIKNU R815 BIKRO N869 RATIN/K0913F380 N869 VTB L999 PINUG T52  RAVOK";
        String s3 = " NOBER W21 CMP R474 TEBAK/K0871S0950 R474 WUY R343 LKO A461 P385/K0825S0950 A461 HG W81 BOBAK";
        String s4 = "DYG W138 LLC H24 P25 H17 BZ/K0844S0780 R596  SULEM/N0448F240 R596 BERBA/N0464F260 B576 BAKER BK1A";
//        List<PointInfo> pList = rm.splitRoute(s1);
        System.out.println("========================================================");
//        for (PointInfo pi : pList) {
//            System.out.println(pi.fix_pt + " " + pi.height_cons);
//        }


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


}
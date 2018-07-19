package dataManager;


 import bean.Point;
 import dataDump.DumpNaipDataToDB;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DumpNaipDataToDBTest {

    @Test
    public void getDataFromFile() {
        String filename = "D://data//PFPX-PRC-NAIP-Expansion-master//NAIP AIRWAYS A.nav";
        DumpNaipDataToDB dn = new DumpNaipDataToDB();
        List<Point> pList = dn.getDataFromFile(filename);
        System.out.println(pList.size());
    }

    @Test
    public void dealWithString() {
        String s1 = "AWY A1    SA10716859008011+16833332+109394996+17301666+110121666 1000     0 6BUNTA    6P97 ";
        String s2 = "AWY A1    FA19008010754101+17301666+110121666+17416668+110300003 1000     0 6P97      6LENKO ";
        String s3 = "AWY A326  FA19005810752151+38974998+118058334+38903332+118470001          1 6P75      6KALBA ";
        String s4 = "AWY A326  FA12237579005811+39080002+117375000+38974998+118058334          1 5CG       6P75  ";
        DumpNaipDataToDB dn = new DumpNaipDataToDB();
        Point[] leg1 = dn.dealWithString(s1);
        Point[] leg2 = dn.dealWithString(s2);
        Point[] leg3 = dn.dealWithString(s3);
        Point[] leg4 = dn.dealWithString(s4);
        System.out.println(  " :" + leg1[0].pid + " " + leg1[1].pid);
        System.out.println(  " :" + leg2[0].pid + " " + leg2[1].pid);
        System.out.println( " :" + leg3[0].pid + " " + leg3[1].pid);
        System.out.println( " :" + leg4[0].pid + " " + leg4[1].pid);
//        System.out.println(leg1[1].equals(leg2[0]));
//        System.out.println(leg1[1].equals(leg2[1]));
        List<Point> pList = new ArrayList<>();
        pList.add(leg1[0]);
        pList.add(leg1[1]);
//        pList.add(leg2[0]);
        pList.add(leg2[1]);
        System.out.println(pList.contains(leg2[0]));
        System.out.println(pList.contains(leg3[0]));
        System.out.println(pList.contains(leg3[1]));
    }


    @Test
    public void getFiles() {
        DumpNaipDataToDB dn = new DumpNaipDataToDB();
        String path = "D:\\data\\PFPX-PRC-NAIP-Expansion-master";
        List<String> files = dn.getFiles(path);
        System.out.println(files.size());
        for (String s : files) {
            if ((s.indexOf("NAIP AIRWAYS ATS.nav")) != -1) {
                System.out.println(s);
            }
        }
    }

    @Test
    public void doGetDataByStep() {
        DumpNaipDataToDB dn = new DumpNaipDataToDB();
        String path = "D:\\data\\PFPX-PRC-NAIP-Expansion-master";
        List<String> files = dn.getFiles(path);
        dn.doGetDataByStep(files);
    }
}
package dataDump;

import bean.Point;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DumpNaipPointsTest {

    @Test
    public void getNaipData() {
        DumpNaipPoints dnp = new DumpNaipPoints();
        List<Point> pointList = dnp.getNaipData();
        for (int i = 0; i < 10; i++) {
            System.out.println(pointList.get(i).pid + " " + pointList.get(i).latitude + " " + pointList.get(i).name);
        }
    }

    @Test
    public void dealWithCoord() {
        DumpNaipPoints dnp = new DumpNaipPoints();
        String t1 = "N303627.46";
        String t2 = "N301718";
        String t3 = "E1164605.45";
        String t4 = "E1161006";
        System.out.println(dnp.dealWithCoord(t1));
        System.out.println(dnp.dealWithCoord(t2));
        System.out.println(dnp.dealWithCoord(t3));
        System.out.println(dnp.dealWithCoord(t4));
//        System.out.println(Double.parseDouble(t3.substring(6, t3.length())));
//        System.out.println(t3.substring(6, t3.length()));

    }
}
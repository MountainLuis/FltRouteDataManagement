package dataDump;

import bean.Point;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DumpPTCorrdToMySqlTest {

    @Test
    public void getDataFromTxt() {
        String p = "C:\\Users\\Administrator\\Documents\\user_Lee\\data\\test.txt";
        DumpPTCorrdToMySql dpct = new DumpPTCorrdToMySql();
        List<Point> pList = dpct.getDataFromTxt(p);
        System.out.println(pList.size());
    }
}
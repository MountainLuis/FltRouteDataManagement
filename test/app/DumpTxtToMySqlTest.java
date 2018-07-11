package app;

import bean.PointInfo;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DumpTxtToMySqlTest {

    @Test
    public void getDataFromTxt() {
        DumpTxtToMySql dttm = new DumpTxtToMySql();
        String filename = "D:\\test.txt";
//        dttm.getDataFromTxt(filename);
        List<PointInfo> pList = dttm.getDataFromTxt(filename);
        System.out.println(pList.size());
        PointInfo p = pList.get(1);
        //Line 1: A1 002 KARTA 33.193211 138.972397
        System.out.println(p.fix_pt + "-" + p.longitude + "-" + p.latitude + "-" + p.enRoute + p.idx);
    }
     @Test
    public void dealWithLine() {
         DumpTxtToMySql dttm = new DumpTxtToMySql();
         String s = "A1 001 HCE 33.114350 139.788483";
         PointInfo p = dttm.dealWithLine(s);
         System.out.println(p.fix_pt + "-" + p.longitude + "-" + p.latitude + "-" + p.enRoute + p.idx);
    }
}
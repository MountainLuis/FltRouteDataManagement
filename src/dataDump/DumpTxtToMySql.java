package dataDump;

import bean.PointInfo;
import util.MysqlHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DumpTxtToMySql {
    String filename = "D:\\wpNavRTE.txt";   //task file

    public static void main(String[] args) {
        DumpTxtToMySql dtm = new DumpTxtToMySql();
        String filename = "D:\\wpNavRTE.txt";
        List<PointInfo> pList = dtm.getDataFromTxt(filename);
        long start = System.currentTimeMillis();
        MysqlHelper.insertIntoMysql(pList);
        System.out.println((System.currentTimeMillis() - start)/1000);
    }
    public List<PointInfo> getDataFromTxt(String filename) {
        List<PointInfo> pList = new ArrayList<>();
        File file = new File(filename);
        BufferedReader reader = null;
        String tmp = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            while((tmp = reader.readLine()) != null){
                if (tmp.startsWith(";")) {
                    continue;
                } else {
                    PointInfo p = dealWithLine(tmp);
                    pList.add(p);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pList;
    }
    public PointInfo dealWithLine(String s) {
        String[] tmp = s.split("\\s+");
        PointInfo pi = new PointInfo();
        pi.fix_pt = tmp[2];
//        pi.longitude = Double.valueOf(tmp[4]);
//        pi.latitude = Double.valueOf(tmp[3]);
        pi.enRoute = tmp[0];
        pi.idx = Integer.valueOf(tmp[1]);
        return pi;
    }

}

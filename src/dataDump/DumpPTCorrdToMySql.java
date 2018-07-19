package dataDump;

import bean.Point;
import bean.PointInfo;
import util.JDBCHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DumpPTCorrdToMySql {


    public static void main(String[] args) {
        String path = "C:\\Users\\Administrator\\Documents\\user_Lee\\data\\wpNavFIX.txt";
        DumpPTCorrdToMySql dpct = new DumpPTCorrdToMySql();
        List<Point> pList = dpct.getDataFromTxt(path);
        String table = "allPoint";
        JDBCHelper.createTable(table, 't');
        JDBCHelper.insertPointList(table, pList);
    }

    public List<Point> getDataFromTxt(String filename) {
        List<Point> pList = new ArrayList<>();
        File file = new File(filename);
        BufferedReader reader = null;
        String tmp = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            while((tmp = reader.readLine()) != null){
                if (tmp.startsWith(";")) {
                    continue;
                } else {
                    Point p = dealWithLine(tmp);
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
    private Point dealWithLine(String s) {
        Point pi = new Point();
        pi.pid = s.substring(0, 6).trim();
        pi.latitude = Double.parseDouble(s.substring(29,39).trim());
        pi.longitude = Double.parseDouble(s.substring(39,s.length()).trim());
        return pi;
    }
}

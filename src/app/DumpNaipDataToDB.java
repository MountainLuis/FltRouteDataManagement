package app;


import bean.PointInfo;
import bean.RoutePoint;
import util.MysqlHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DumpNaipDataToDB {
    public static void main(String[] args) {
        DumpNaipDataToDB dt = new DumpNaipDataToDB();
//        List<RoutePoint> pList = dt.getDataFromFile();
//        MysqlHelper.insertNaipInfoIntoDB(pList);
    }
    public List<String> getFiles(String path) {
        List<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tmp = file.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                files.add(tmp[i].toString());
            }
        }
        return files;
    }
    public void doGetDataByStep(List<String > files){
        String pat = "NAIP AIRWAYS";
        for (String file : files) {
            if (file.indexOf(pat) != -1) {
                System.out.println(file);
            }
        }
    }


    public List<RoutePoint> getDataFromFile(String filename) {
        List<RoutePoint> pList = new ArrayList<>();
        File file = new File(filename);
        BufferedReader reader = null;
        String tmp = null;
        try {
            reader = new BufferedReader(new FileReader(file));
             while((tmp = reader.readLine()) != null) {
                 if (tmp.length() < 2) {
                     continue;
                 }
                RoutePoint[] pts = dealWithString(tmp);
                for (int i = 0; i < pts.length; i++) {
                    if (pList.contains(pts[i])) {
                        continue;
                    } else {
                        pList.add(pts[i]);
                    }
                }
//                System.out.println("Done" + tmp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pList;
    }
    public RoutePoint[] dealWithString(String s){
        if (s.equals("")) {
            System.out.println("Input nothing");
        }
        String[] tmp = s.trim().split("\\s+");
        RoutePoint[]  pts = null;
        if (tmp.length == 7){
            RoutePoint p1 = new RoutePoint(tmp[1],tmp[5].substring(1));
            RoutePoint p2 = new RoutePoint(tmp[1],tmp[6].substring(1));
            pts = new RoutePoint[]{p1, p2};
        } else if (tmp.length == 6) {
            RoutePoint p1 = new RoutePoint(tmp[1],tmp[4].substring(1));
            RoutePoint p2 = new RoutePoint(tmp[1],tmp[5].substring(1));
            pts = new RoutePoint[]{p1, p2};
        } else {
            System.out.println("Check the Record:" + s);
        }
        return pts;
    }

}

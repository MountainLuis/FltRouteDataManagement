package dataDump;


import bean.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 此程序用于将.nav文件中的naip数据导入到数据库中。
 */
public class DumpNaipDataToDB {
    public static void main(String[] args) {
        String path = "D:\\data\\PFPX-PRC-NAIP-Expansion-master";
        DumpNaipDataToDB dt = new DumpNaipDataToDB();
        List<String> fileList = dt.getFiles(path);
        dt.doGetDataByStep(fileList);
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
            if (((file.indexOf("NAIP AIRWAYS ATS.nav")) != -1)
                    || (file.indexOf("NAIP AIRWAYS HVFR.nav") != -1)) {
                continue;
            }else if (file.indexOf(pat) != -1) {
                System.out.println(file);
                List<Point> pList = getDataFromFile(file);
//                MysqlHelper.insertNaipInfoIntoDB(pList);
                System.out.println("Insert finished." + pList.size());
                System.out.println("Insert DB Unused.");
            }
        }
    }


    public List<Point> getDataFromFile(String filename) {
        List<Point> pList = new ArrayList<>();
        File file = new File(filename);
        BufferedReader reader = null;
        String tmp = null;
        try {
            reader = new BufferedReader(new FileReader(file));
             while((tmp = reader.readLine()) != null) {
                 if (tmp.length() < 2) {
                     continue;
                 }
                Point[] pts = dealWithString(tmp);
                for (int i = 0; i < pts.length; i++) {
                    if (pList.contains(pts[i])) {
                        continue;
                    } else {
                        pList.add(pts[i]);
                    }
                }
             }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pList;
    }
    public Point[] dealWithString(String s){
        if (s.equals("")) {
            System.out.println("Input nothing");
        }
        String[] tmp = s.trim().split("\\s+");
        Point[]  pts = null;
        if (tmp.length == 7){
            Point p1 = new Point(tmp[1],tmp[5].substring(1));
            Point p2 = new Point(tmp[1],tmp[6].substring(1));
            pts = new Point[]{p1, p2};
        } else if (tmp.length == 6) {
            Point p1 = new Point(tmp[1],tmp[4].substring(1));
            Point p2 = new Point(tmp[1],tmp[5].substring(1));
            pts = new Point[]{p1, p2};
        } else if (tmp.length == 5) {
            Point p1 = new Point(tmp[1],tmp[3].substring(1));
            Point p2 = new Point(tmp[1],tmp[4].substring(1));
            pts = new Point[]{p1, p2};
        } else {
            System.out.println("Check the Record:" + s);
        }
        return pts;
    }

}

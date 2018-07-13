package app;

import bean.PointInfo;
import util.MysqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RouteManager {
    public Map<String, List<PointInfo>> routeSeq = null;  //这个表用于存放每条城市对和它对应的所有航路点
    DataAccessObject dao = new DataAccessObject();
//    public List<String> exceptOD = new ArrayList<>();
//
//    public static void main(String[] args) {
//        RouteManager rm = new RouteManager();
//        rm.dealWithAll();
//        rm.dealWithAllSimple();
//    }

//    public void dealWithAll() {
//        Map<String, List<List<PointInfo>>> sameOD = new HashMap<>();  //存放相同OD的不同航路
//        routeSeq = new HashMap<>();
//        String sql = "select * from tmp_201806";
//        ResultSet rs = MysqlHelper.getResultSet(sql);
//        try {
//            while (rs.next()) {
//                String path = rs.getString("path");
//                if (path.equals("")) {
//                    continue;
//                } else {
//                        String origAp = rs.getString("orig");
//                        String destAp = rs.getString("dest");
//                        String od = origAp + "-" + destAp;
//                        System.out.println(od);
////                        List<PointInfo> routePts = splitRoute(path);
//
//                        if (routeSeq.keySet().contains(od)) {
//                            if (isListEquals(routeSeq.get(od), routePts)) {
//                                continue;
//                            } else {
//                                if (sameOD.keySet().contains(od)) {
//                                    List<List<PointInfo>> routesList = sameOD.get(od);
//                                    if (!listContains(routesList, routePts)) {
//                                        routesList.add(routePts);
//                                    }
//                                    sameOD.put(od, routesList);
//                                } else {
//                                    List<List<PointInfo>> routesList = new ArrayList<>();
//                                    routesList.add(routePts);
//                                    routesList.add(routeSeq.get(od));
//                                    sameOD.put(od, routesList);
//                                }
//                            }
//                        } else {
//                            routeSeq.put(od, routePts);
//                        }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        MysqlHelper.insertFltPathIntoMysql(routeSeq);
//        for (String r : sameOD.keySet()) {
//            int i = 1;
//            List<List<PointInfo>> rList = sameOD.get(r);
//            for (List<PointInfo> sameList : rList) {
//                MysqlHelper.insertFltPathIntoMysqlSingle(r+i, sameList);
//                i++;
//            }
//        }
//    }

//    public void dealWithAllSimple() {
//        List<String[]> strList = new ArrayList<>();
//        String sql = "select * from tmp_201806";
//        ResultSet rs = MysqlHelper.getResultSet(sql);
//        try {
//            while (rs.next()) {
//                String path = rs.getString("path");
//                if (path.equals("")) {
//                    continue;
//                } else {
//                    String origAp = rs.getString("orig");
//                    String destAp = rs.getString("dest");
//                    String od = origAp + "-" + destAp;
//                    System.out.println(od);
//                    List<PointInfo> routePts = splitRoute(path);
//                    String r = addToString(routePts);
//                    String[] strs = new String[]{od, r};
//                    strList.add(strs);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
////        MysqlHelper.insertIntoSimplePathWithStringArrayList(strList);
//    }

    /**
     * 将飞行计划中的“点-航路-点-航路-点”格式的path转换成全部由点组成的航路。
     * @param path
     * @return
     */
    public String transferFltPath(String path, String toAp, String ldAp){
        int abroad = judgeAbroad(toAp, ldAp);
        String res = addToString(splitRoute(path, abroad));
        return res;
    }
    public String addToString(List<PointInfo> pList){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pList.size(); i++) {
            PointInfo pi = pList.get(i);
            res.append(pi.fix_pt);
            if (i != pList.size() - 1) {
                res.append("-");
            }
        }
        return res.toString();
    }

    /**
     * List包含关系检测
     * @param all
     * @param pl
     * @return
     */
    public boolean listContains(List<List<PointInfo>> all, List<PointInfo> pl) {
        for (int i = 0; i < all.size(); i++) {
            if (isListEquals(all.get(i),pl)) {
                return true;
            }
        }
        return  false;
    }


    /**
     * 这里需要做一个假设，一条Path中，航路点P和航路R，可以有两个连续的P，但是R一定是不连续的。
     * 且R的前后必须是两个P，不能为DCT，两个P都在R上；
     * 国际航路处理；因读取数据为 route_all
     * @param r
     * @return
     */
    public List<PointInfo> splitRoute(String r, int abroad){
        List<PointInfo> fixPts = new ArrayList<>();  //as result
        String[] tmp = r.trim().split("\\s+");
        int[] flag = new int[tmp.length]; //route = 1; point = 0;
        //---------------------------------------
        if (tmp[0].equals("DCT") || tmp[0].equals("L4")) {
            flag[0] = -1;
        } else {
            PointInfo p0 = createPoint(tmp[0]);
            fixPts.add(p0);
            flag[0] = 0;
        }
        //-----------------------------------------
        for(int i = 1; i < tmp.length; i++) {
            if (tmp[i].equals("DCT") || tmp[i].equals("L4")) {
                flag[i] = -1;
                continue;
            }
            if (isRoute(tmp[i], abroad) && (flag[i-1] == 0)) {
                flag[i] = 1;
//                System.out.println("R:" + tmp[i]);
                List<PointInfo> ptsOnR = new ArrayList<>();
                if (i == tmp.length - 1) {
                    System.err.println("不能以航路作为path结尾。" + r);
                } else {
                    ptsOnR = dao.getSubPtSeq(tmp[i], splitPointName(tmp[i - 1]), splitPointName(tmp[i + 1]), abroad);
                }
//                ptsOnR.get(0).addCons( fixPts.get(fixPts.size() -1));
//                fixPts.remove(fixPts.size() -1);
                if (ptsOnR == null) {
                    continue;
                }else {
                    fixPts.addAll(ptsOnR);
                }
            } else {
                flag[i] = 0;
//                System.out.println("Point:" + tmp[i]);
                PointInfo p = createPoint(tmp[i]);
                fixPts.add(p);
//                if (fixPts.contains(p)) {
////                    exceptOD.add(r);
//                    System.out.println("表中已经有这个点" + p.fix_pt);
//                    continue;
//                } else {
//                    fixPts.add(p);
//                }
            }
        }
        return fixPts;
    }
    public boolean isRoute(String s, int i) {
        if (i == 0) {
            if (dao.naipMap.containsKey(s)) {
                return true;
            } else {
                return  false;
            }
        }else if (i == 1) {
            if (dao.routeMap.containsKey(s)) {
                return  true;
            } else {
                return  false;
            }
        }else {
            return false;
        }
    }


    /**
     * checked.
     * @param s
     * @return
     */
    private PointInfo createPoint(String s) {
        PointInfo p = new PointInfo();
        p.fix_pt = splitPointName(s);
//        if (s.contains("/")) {
//            String[] tmp = s.split("/");
//            if (tmp[0].equals("VRK")) {
//               Arrays.fill(tmp,0,1,"VKR");
//            }
//            p.fix_pt = tmp[0];
////            String[] cons = splitCons(tmp[1]);
////            p.speed_cons = cons[0];
////            p.height_cons = cons[1];
//        } else {
//            p.fix_pt = s;
////            p.height_cons = "";
////            p.speed_cons = "";
//        }

        return p;
    }

    /**
     * checked.
     * @param s
     * @return
     */
    private String[] splitCons(String s) {
        int b = 0;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c>='A' && c <= 'Z') {
                b = i;
                break;
            }
        }
        String spd = s.substring(0,b);
        String alt = s.substring(b);
        String[] res = {spd, alt};
        return res;
    }
    private String splitPointName(String s) {
        if (s.contains("/")) {
            String[] t = s.split("/");
            if (t[0].equals("VRK")) {
                return "VKR";
            }
            return s.split("/")[0];
        } else {
            return s;
        }
    }
    public boolean isListEquals(List<PointInfo> pList1, List<PointInfo> pList2){
        if (pList1.size() != pList2.size()) {
            return false;
        } else {
            for(int i = 0; i < pList1.size();i++) {
                if (!pList1.get(i).fix_pt.equals(pList2.get(i).fix_pt)) {
                    return false;
                }
            }
            return true;
        }
    }
    public int judgeAbroad(String toAp, String ldAp) {
            String pattern = "^Z[A-Z]*$";
            if ((toAp.matches(pattern)) &&
                    (ldAp.matches(pattern))) {
                return 0;
            } else {
                return 1;
            }
    }
}

package app;

import bean.PointInfo;
import util.MysqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ODRouteManager {
    DataAccessObject dao = new DataAccessObject();

    public void dealWithAllPlans() {
        Map<String, String> odMap = new HashMap<>();
        String sql = "select * from tmp_201806";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try {
            while (rs.next()) {
                String path = rs.getString("P_ROUTE");
                if (!path.equals("")) {
                    String od = rs.getString("P_DEPAP") + "-" + rs.getString("P_ARRAP");
                    String pStr = addToString(splitRoute(path));
                    odMap.put(pStr, od);
                } else {
                    continue;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MysqlHelper.insertIntoSimplePathWithStringArrayList(odMap);
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
    public List<PointInfo> splitRoute(String r){
        List<PointInfo> fixPts = new ArrayList<>();  //as result
        String[] tmp = r.trim().split("\\s+");
        int[] flag = new int[tmp.length]; //route = 1; point = 0;
        if (tmp[0].equals("DCT") || tmp[0].equals("L4")) {
            flag[0] = -1;
        } else {
            PointInfo p0 = new PointInfo(splitPointName(tmp[0]));
            fixPts.add(p0);
            flag[0] = 0;
        }
        for(int i = 1; i < tmp.length; i++) {
            if (tmp[i].equals("DCT") || tmp[i].equals("L4")) {
                flag[i] = -1;
                continue;
            }
            if (dao.routeMap.containsKey(tmp[i]) && (flag[i-1] == 0)) {
                flag[i] = 1;
//                System.out.println("R:" + tmp[i]);
                List<PointInfo> ptsOnR = new ArrayList<>();
                if (i == tmp.length - 1) {
                    System.err.println("不能以航路作为path结尾。" + r);
                } else {
                    ptsOnR = dao.getSubPtSeq(tmp[i], splitPointName(tmp[i - 1]), splitPointName(tmp[i + 1]));
                }
                if (ptsOnR == null) {
                    continue;
                }else {
                    fixPts.addAll(ptsOnR);
                }
            } else {
                flag[i] = 0;
//                System.out.println("Point:" + tmp[i]);
                PointInfo p = new PointInfo(splitPointName(tmp[i]));
                fixPts.add(p);

            }
        }
        return fixPts;
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
}

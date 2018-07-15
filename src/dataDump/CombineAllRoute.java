package dataDump;

import app.DataAccessObject;
import bean.PointInfo;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombineAllRoute {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);

    DataAccessObject dao = new DataAccessObject();
    public void conbine(){
        Map<String, List<PointInfo>> res = new HashMap<>();
        for (String r : dao.routeMap.keySet()) {
            if (!dao.naipMap.containsKey(r)) {
                res.put(r, dao.routeMap.get(r));
            } else {
                List<PointInfo> points = null; //todo
                res.put(r, points);
            }
        }
    }
    public List<PointInfo> compare2List(List<PointInfo> pList1, List<PointInfo> pList2) {
        if (pList1.size() == pList2.size()) {
            if (pList1.get(0).fix_pt.equals(pList2.get(pList2.size() - 1).fix_pt)) {
                Collections.reverse(pList2);
            }
            if (isListEquals(pList1, pList2)) {
                return pList1;
            }
        } else if (pList1.size() > pList2.size()) {

        }



        return null;//todo
    }
    public boolean isListEquals(List<PointInfo> pList1, List<PointInfo> pList2) {
        for (int i = 0; i < pList1.size(); i++) {
            if (!pList1.get(i).fix_pt.equals(pList2.get(i).fix_pt)) {
                LOGGER.info( pList1.get(i).enRoute + ": "+ pList1.get(i).fix_pt + " " + pList2.get(i).fix_pt +
                " 数据点一样多，但数据点不一致");
                return false;
            }
        }
        return true;
    }
    public boolean isListContained(List<PointInfo> pList1, List<PointInfo> pList2) {
        for (int i = 0 ; i < pList2.size(); i++) {
            PointInfo pi = pList2.get(i);
            if (!pList1.contains(pi)) {
                LOGGER.info(pi.enRoute + "不包括NAIP数据点" + pi.fix_pt);
                return false;
            }
        }
        return  true;
    }

}

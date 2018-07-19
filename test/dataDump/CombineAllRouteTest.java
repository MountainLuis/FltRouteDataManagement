package dataDump;

import dataManager.DataAccessObject;
import org.apache.log4j.Logger;
import org.junit.Test;

public class CombineAllRouteTest {
    private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
    @Test
    public void conbine() {
//        CombineAllRoute car = new CombineAllRoute();
//        LOGGER.debug("routeMap:" +car.dao.routeMap.size() + "  naipMap:" + car.dao.naipMap.size());
//         for (String s : car.dao.naipMap.keySet()) {
//            if (!car.dao.routeMap.containsKey(s)) {
//                LOGGER.debug(s + " " + " " + car.dao.naipMap.get(s).size());
////                if (car.dao.routeMap.get(s).size() < car.dao.naipMap.get(s).size()) {
////                    LOGGER.info(s + " "+ car.dao.routeMap.get(s).size() + " " + car.dao.naipMap.get(s).size());
//                }
             }
//        }
//        LOGGER.info("以上是Naip点比net点更多的航路。");




    @Test
    public void isListEquals() {
        CombineAllRoute car = new CombineAllRoute();
//        List<PointInfo> pList1 = car.dao.getPtSeq("B221");
//        List<PointInfo> pList2 = car.dao.getPtSeqNaip("B221");
//        LOGGER.debug(car.isListEquals(pList1, pList2));
//        int i = 0;
//        for (String s : car.dao.routeMap.keySet()) {
//            if (car.dao.naipMap.containsKey(s)) {
//                if (car.dao.routeMap.get(s).size() == car.dao.naipMap.get(s).size()) {
//                    if (car.isListEquals(car.dao.routeMap.get(s),car.dao.naipMap.get(s))) {
//                        System.out.println(s + "=====" + i++);
//                    }
//                }
//            }
////        }
////        LOGGER.info("长度相等但包含点不同的航路");
    }


    @Test
    public void isListContained() {
//        CombineAllRoute car = new CombineAllRoute();
//        for (String s : car.dao.routeMap.keySet()) {
//            if (car.dao.naipMap.containsKey(s)) {
//                if (car.dao.routeMap.get(s).size() > car.dao.naipMap.get(s).size()) {
//                    LOGGER.debug(s +  " : " + car.isListContained(car.dao.routeMap.get(s), car.dao.naipMap.get(s)));
//                }
//            }
//        }
    }
}
package bada.util;

import bada.bean.FixPt;
import bada.bean.Route;
import bada.exception.IllegalRouteException;

import java.util.List;

public class RouteUtil {

    public static boolean isPathEmpty(List<FixPt> l){
        return l == null || l.size() < 2;
    }

    public static boolean isPathNotEmpty(List<FixPt> l){
        return l != null && l.size() >= 2;
    }

    public static boolean isLinkedUp(List<FixPt> l1, List<FixPt> l2){
        FixPt pt1 = l1.get(l1.size()-1);
        FixPt pt2 = l2.get(0);
        return pt1.ID.equals(pt2.ID);
    }

    public static int pathSize(List<FixPt> pts){
        if(pts == null){
            return 0;
        }
        return pts.size();
    }

    public static void checkRoute(Route route) throws IllegalRouteException {
        if(route.startEnRouteAltitude > route.cruiseAltitude
                || route.endEnRouteAltitude > route.cruiseAltitude){
            throw new IllegalRouteException("起始巡航高度或者结束巡航高度比巡航高度高！");
        }
        if(isPathEmpty(route.enRoute)){
            if(isPathEmpty(route.SID) && isPathEmpty(route.STAR)){
                throw new IllegalRouteException("航路上没有足够的航路点!");
            }
            if(isPathNotEmpty(route.SID) && isPathNotEmpty(route.STAR)) {
                throw new IllegalRouteException("只有进港和离港阶段！");
            }
        }else{
            if(isPathNotEmpty(route.SID)
                    && !isLinkedUp(route.SID, route.enRoute)){
                throw new IllegalRouteException("离港点和EnRoute阶段第一个航路点不是同一个点");
            }
            if(isPathNotEmpty(route.STAR)
                    && !isLinkedUp(route.enRoute, route.STAR)){
                throw new IllegalRouteException("进港点和EnRoute阶段最后一个航路点不是同一个点");
            }
        }
    }
}

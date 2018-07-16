package apply.montecarlo;

import code.apply.util.DataTransform;
import code.bada.bean.FixPt;
import code.bada.bean.Route;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestRouteManagement {

    @Test
    public void testGetRouteName(){
        Set<String> result = RouteManagement.getRouteName();
        Iterator it = result.iterator();
        int i = 0;
        while(it.hasNext()) {
            if (i % 5 == 0) {
                System.out.println();
            }
            System.out.print(it.next() + " ");
            i++;
        }
        System.out.println("共计" + i + "条航路。");
    }
    @Test
    public void testGetRoutePts() {
        List<FixPt> pts = RouteManagement.getRoutePts("A1");
        for (FixPt pt: pts) {
            System.out.println(pt.ID + " " + pt.longitude + " " + pt.latitude);
        }
    }
    @Test
    public void testGetRoute() {
        Map<String,Route> routes = RouteManagement.getRoute(RouteManagement.getRouteName());
        Iterator iter = routes.keySet().iterator();
        int i = 0;
        while(iter.hasNext()) {
            if (i > 50) {
                break;
            }
            String name = (String) iter.next();
            System.out.println(name + ": " + routes.get(name).enRoute.size() +" "
                    + routes.get(name).endEnRouteAltitude);
        }
    }
    @Test
    public void testGetLinkedRoutes() throws SQLException {
        Map<String, List<String>> linkedRoutes = RouteManagement.getLinkedRoutes();
//        Iterator<String> iter = linkedRoutes.keySet().iterator();
//        while(iter.hasNext()) {
//            String key = iter.next();
//            if (linkedRoutes.get(key).size() == 1) {
//                iter.remove();
//            }
//        }


        for(String pt: linkedRoutes.keySet()) {
            System.out.print(pt + " : ");
            for (String route : linkedRoutes.get(pt)) {
                System.out.print(route + " ");
            }
            System.out.println();
        }
        System.out.println(linkedRoutes.keySet().size());
    }
    @Test
    public void testMap2Json() throws SQLException {
        Map<String, List<String>> linkedRoutes =RouteManagement.getLinkedRoutes();
        DataTransform.map2json(linkedRoutes);
    }

    @Test
    public void testGetAllPts() throws SQLException {
        List<String> pts = RouteManagement.getAllPts();
        for(int i = 0; i < pts.size(); i++) {
            System.out.print(pts.get(i) + " ");
            if(i % 10 == 0) {
                System.out.println();
            }
        }
    }
}

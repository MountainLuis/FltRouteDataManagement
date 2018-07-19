package apply.montecarlo;

import apply.util.DataTransform;
import apply.util.MysqlHelper;
import bada.bean.FixPt;
import bada.bean.Route;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 */
public class RouteManagement {

    public static Set<String> routeName = null;
    public static Map<String,Route> routes = null;

    public RouteManagement() {
        routeName = getRouteName();
        routes = getRoute(routeName);
    }

    /**
     * 取得所有的航路名称
     * @return
     */
    public static Set<String> getRouteName() {
        Set<String> routeName = new LinkedHashSet<>();
        String sql = "select * from route";
        try{
            ResultSet rs = MysqlHelper.getResultSet(sql);
            while(rs.next()) {
                routeName.add(rs.getString("route"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeName;
    }

    /**
     * 取出指定航路的所有航路点List
     * @param route
     * @return
     */
    public static List<FixPt> getRoutePts(String route) {
        List<FixPt> pts = new ArrayList<>();
        String sql = "select * from route where route ='" + route +"'";
        try{
            ResultSet rs = MysqlHelper.getResultSet(sql);
            while(rs.next()) {
                FixPt pt = new FixPt();
                pt.ID = rs.getString("id");
                pt.longitude = DataTransform.latLongString2Double(rs.getString("longitude"));
                pt.latitude = DataTransform.latLongString2Double(rs.getString("latitude"));
                pts.add(pt);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return pts;
    }

    /**
     * 将所有航路信息取出，构建对应的Route对象，并存在Map。
     * @param routeName
     * @return
     */
    public static Map<String, Route> getRoute(Set<String> routeName) {
        Map<String, Route> routes = new HashMap<>();
        Iterator iter =routeName.iterator();
        while(iter.hasNext()) {
            String name = (String) iter.next();
            Route r = new Route();
            r.enRoute = getRoutePts(name);
            r.cruiseAltitude = 6000;
            r.endEnRouteAltitude = 6000;
            r.startEnRouteAltitude = 6000;
            r.SID = new ArrayList<>();
            r.STAR = new ArrayList<>();
            routes.put(name, r);
        }
        return routes;
    }

    /**
     * 对于每一个航路点，取得所有与之相关的航路存入List,并对应放入Map。
     * @return
     * @throws SQLException
     */
    public static Map<String, List<String>> getLinkedRoutes() throws SQLException {
       List<String> allPts = getAllPts();
       Map<String, List<String>> linkedRoutes = new HashMap<>();
       for (String pt: allPts) {
           String sql = "select * from route where id='" + pt + "'";
           List<String> routes = new ArrayList<>();
           ResultSet rs = MysqlHelper.getResultSet(sql);
           while(rs.next()) {
               routes.add(rs.getString("route"));
           }
           linkedRoutes.put(pt, routes);
       }
        Iterator<String> iter = linkedRoutes.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            if (linkedRoutes.get(key).size() == 1) {
                iter.remove();
            }
        }
       return linkedRoutes;
    }



    /**
     * 取得所有的航路点（无重复）
     * @return
     * @throws SQLException
     */
    public static List<String> getAllPts() throws SQLException {
        Set<String> pts = new HashSet<>();
        String sql = "select * from route";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        while (rs.next()) {
            pts.add(rs.getString("id"));
        }
        List<String> allPts = new ArrayList<>(pts);
        return allPts;
    }

    //todo
    public static Map<String, List<String>> getLinkedRoutesFromJson() {
        Map<String, List<String>> linkedRoutes = null;
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = RouteManagement.class.getResourceAsStream("/sample/data/linkedRoute.json");


        return linkedRoutes;
    }
}

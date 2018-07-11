package bean;

/**
 * 转储NAIP航路数据用。
 */
public class RoutePoint {
    public String route;
    public String pt;

    public RoutePoint(String r, String p) {
        route = r;
        pt = p;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RoutePoint)) {
            return false;
        } else {
            RoutePoint p = (RoutePoint) obj;
            if (p.route.equals(this.route) && p.pt.equals(this.pt)) {
                return true;
            } else {
                return false;
            }
        }
    }
    @Override
    public int hashCode() {
        return this.route.hashCode() + this.pt.hashCode();
    }
}

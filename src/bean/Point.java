package bean;

public class Point {
    public String pid;
    public String name;
    public double latitude;
    public double longitude;
    public int seq;

    public Point(){

    }
    public Point(String s1, String s2) {
        name = s1;
        pid = s2;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        } else {
            Point p = (Point) obj;
            if (p.pid.equals(this.pid)) {
                return true;
            } else {
                return false;
            }
        }
    }
    public int hashCode(){
        return pid.hashCode();
    }
    public String toString() {
     return this.pid + " : " + this.name + this.longitude;
    }
}

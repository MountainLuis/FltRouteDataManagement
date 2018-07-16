package apply.bean;

public class FlightConflict {
    public String pt;
    public String acft1;
    public int time1;
    public String route1;
    public String acft2;
    public int time2;

    public String toString() {
        return pt+ " : " + acft1 + ":" + time1 + " " + acft2 + ":" + time2;
    }

}

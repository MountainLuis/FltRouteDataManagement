package bean;

public class PointInfo {
    public String fix_pt;
//    public double longitude;
//    public double latitude;
    public String pt_name;
    public int idx;
    public String enRoute;
    public String height_cons;
    public String speed_cons;
    public PointInfo(){

    }
    public PointInfo(String fix_pt) {
        this.fix_pt = fix_pt;
    }
//    public boolean isCompleted() {
//        return !(this.longitude == 0 && this.latitude == 0);
//    }
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof PointInfo)) {
            return false;
        } else {
            PointInfo p = (PointInfo) obj;
            if (p.fix_pt.equals(this.fix_pt)) {
                return true;
            }else {
                return false;
            }
        }
    }
    @Override
    public int hashCode() {
        return this.fix_pt.hashCode();
    }
    public void addCons(PointInfo p) {
        if (p.fix_pt.equals(this.fix_pt)) {
            this.height_cons = p.height_cons;
            this.speed_cons = p.speed_cons;
        } else {
            System.out.println("Check The Point." + this.fix_pt + ":" + p.fix_pt);
        }
    }



}

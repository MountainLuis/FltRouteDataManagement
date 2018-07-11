package bean;

public class FltPlan {
    public String flt_no;
    public String regitration_num;
    public String acft_type;
    public String to_ap;
    public String ld_ap;
    public String dep_time;
    public String arr_time;
    public String flt_path;

    public FltPlan() {
    }
    public FltPlan(String flt_no, String to_ap, String ld_ap) {
        this.flt_no = flt_no;
        this.to_ap = to_ap;
        this.ld_ap = ld_ap;
    }
    @Override
    public String toString() {
        return flt_no + "-" + to_ap + "-" + ld_ap;
    }
}

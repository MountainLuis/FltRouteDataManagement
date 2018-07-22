package bean;

public class AcftPtTime {
    public String flt_no;
    public String fix_pt;
    public long time;
    public String reg_num;
    public String dep_time;
    public AcftPtTime(String flt_no, String fix_pt, long time) {
        this.flt_no = flt_no;
        this.fix_pt = fix_pt;
        this.time = time;
    }
    public String toString() {
        return flt_no + "-" + fix_pt + "-" + time;
    }
}

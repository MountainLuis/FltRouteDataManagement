package sectordata;

import java.util.List;

public class ReigionData {
    public String fir;
    public String area;
    public String sector;
    public List<Pt> boundries;

    public ReigionData(String fir, String area, String sector, List<Pt> boundries) {
        this.fir = fir;
        this.area = area;
        this.sector = sector;
        this.boundries = boundries;
    }
}

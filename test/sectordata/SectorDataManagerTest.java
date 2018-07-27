package sectordata;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SectorDataManagerTest {

    @Test
    public void makeFirRegionData() {
    }

    @Test
    public void makeAreaRegionData() {
    }

    @Test
    public void makeSectorRegionData() {
    }

    @Test
    public void getPtList() {
    }

    @Test
    public void getPoints() throws IOException {
        SectorDataManager sdm = new SectorDataManager();
        List<PointData> pointDataList = sdm.getPointData();
        for(PointData pd : pointDataList) {
            System.out.println(pd.regionSeq + " " + pd.ptSeq + " " + pd.ptName + pd.lat);
        }
    }

    @Test
    public void getSectorData() {
    }

    @Test
    public void getFirData() {
    }

    @Test
    public void getAreaData() {
    }

    @Test
    public void getPointData() {
    }

    @Test
    public void storageRegionData() throws IOException {
        SectorDataManager sdm = new SectorDataManager();
        List<ReigionData> res = new ArrayList<>();
        sdm.storageRegionData(res);
        }

    @Test
    public void createRegionDataTable() {
    }

    @Test
    public void insertRegionData() {

    }

    @Test
    public void dealWithCoord() {
    }

    @Test
    public void getSectorName() throws IOException {
        SectorDataManager sdm = new SectorDataManager();
        Map<String, String> mapp = sdm.getSectorName();
        for (String s : mapp.keySet()) {
            System.out.println(s + "   " + mapp.get(s));
        }
    }
}
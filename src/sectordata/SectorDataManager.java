package sectordata;

import bada.bean.AcftPerformance;
import bean.FltPlan;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import simulation.BaseTrajectory;
import util.JDBCHelper;

import java.awt.geom.Area;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectorDataManager {
    List<FirData> firDataList = null;
    List<AreaData> areaDataList = null;
    List<SectorData> sectorDataList = null;
    List<PointData> pointDataList = null;
    HashMap<String,String> sectorName = null;
    Map<String, String> firName = null;

    public SectorDataManager() throws IOException {
        firDataList = getFirData();
        areaDataList = getAreaData();
        sectorDataList = getSectorData();
        pointDataList = getPointData();
        sectorName = getSectorName();
        firName = getFirName();
    }
    public static void main(String[] args) throws IOException {
        SectorDataManager sdm = new SectorDataManager();
        List<ReigionData> result = new ArrayList<>();
        result.addAll(sdm.makeFirRegionData());
        result.addAll(sdm.makeAreaRegionData());
        result.addAll(sdm.makeSectorRegionData());
        sdm.storageRegionData(result);
        System.out.println(result.size());
        for(ReigionData rd : result) {
            System.out.println(rd.fir + " " + rd.area + " " + rd.sector + " " + rd.boundries.size());
        }

    }


    public List<ReigionData> makeFirRegionData() {
        List<ReigionData> firRegion = new ArrayList<>();
        for (FirData fd : firDataList) {
            List<Pt> subRegion = getPtList(fd.regionSeq);
            ReigionData rd = new ReigionData(fd.firID,"","",subRegion);
            firRegion.add(rd);
        }
        return firRegion;
    }
    public List<ReigionData> makeAreaRegionData(){
        List<ReigionData> areaRegion = new ArrayList<>();
        for (AreaData ad : areaDataList) {
            List<Pt> subRegion = getPtList(ad.areaSeq);
            ReigionData rd = new ReigionData(ad.firID,sectorName.get(ad.areaName),"",subRegion);
            areaRegion.add(rd);
        }
        return areaRegion;
    }
    public List<ReigionData> makeSectorRegionData(){
        List<ReigionData> sectorRegion = new ArrayList<>();
        for (SectorData sd : sectorDataList) {
            List<Pt> subRegion = getPtList(sd.sectorSeq);
            String secName = sectorName.get(sd.area) +"_" + sd.sector.substring(0,2);
            ReigionData rd = new ReigionData(firName.get(sd.fir),sectorName.get(sd.area),secName,subRegion);
            sectorRegion.add(rd);
        }
        return sectorRegion;
    }
    public List<Pt> getPtList(int seq) {
        List<Pt> subRegion = new ArrayList<>();
        List<PointData> bound = getPoints(seq);
        for (PointData pd : bound) {
            Pt p = new Pt();
            p.seq = pd.ptSeq;
            p.lat = dealWithCoord(pd.lat);
            p.lng = dealWithCoord(pd.lng);
            subRegion.add(p);
        }
        return subRegion;
    }

    public List<PointData> getPoints(int regionSeq) {
        List<PointData> res = new ArrayList<>();
        for (int i = 0; i < pointDataList.size(); i++) {
            PointData pd = pointDataList.get(i);
            if (pd.regionSeq == regionSeq) {
                res.add(pd);
            }
        }
        return res;
    }

    public List<SectorData> getSectorData() {
        List<SectorData> res = new ArrayList<>();
        String table = "region_sector";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try {
            while (rs.next()) {
                SectorData sd = new SectorData();
                sd.sectorSeq = Integer.valueOf(rs.getString("区域序号"));
                sd.fir = rs.getString("情报区名");
                sd.area = rs.getString("管制区名");
                sd.sector = rs.getString("扇区名");
                sd.altHigh = Integer.valueOf(rs.getString("高度上限"));
                sd.altLow = Integer.valueOf(rs.getString("高度下限"));
                res.add(sd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public List<FirData> getFirData() {
        List<FirData> res = new ArrayList<>();
        String table = "region_fir";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try {
            while (rs.next()) {
                FirData fd = new FirData();
                fd.regionSeq = Integer.valueOf(rs.getString("区域序号"));
                fd.firID = rs.getString("情报区代号");
                fd.firName = rs.getString("名称");
                res.add(fd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public List<AreaData> getAreaData() {
        List<AreaData> res = new ArrayList<>();
        String table = "region_area";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try {
            while (rs.next()) {
                AreaData ad = new AreaData();
                ad.areaSeq = Integer.valueOf(rs.getString("区域序号"));
                ad.fir = rs.getString("所属情报区");
                ad.firID = rs.getString("代码");
                ad.areaName = rs.getString("管制区名称");
                res.add(ad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public List<PointData> getPointData() {
        List<PointData> res = new ArrayList<>();
        String table = "region_points";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try {
            while (rs.next()) {
                PointData pd = new PointData();
                pd.regionSeq = Integer.valueOf(rs.getString("区域序号"));
                pd.ptSeq = Integer.valueOf(rs.getString("点序号"));
                pd.ptName = rs.getString("点名称");
                pd.lat = rs.getString("纬度");
                pd.lng = rs.getString("经度");
                res.add(pd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void storageRegionData(List<ReigionData> rdList) {
        String table = "sector_2018_id";
        createRegionDataTable(table);
        insertRegionData(rdList, table);
        System.out.println("Done");
    }
    public void createRegionDataTable(String table) {
        String createSql = "CREATE TABLE " + table +" (id int(10)," +
                "artcc varchar(20), area varchar(20), name varchar(20), seq int(10), longitude double, latitude double" +
                ")charset=utf8;";
        JDBCHelper.create(createSql);

    }
    public void insertRegionData(List<ReigionData> rdList, String table) {
        String key = "MySQL";
        String sql = "INSERT INTO " + table +  " (" +
                "artcc,area,name,seq,longitude,latitude)" +
                "VALUES (?,?,?,?,?,?)";
        Connection conn = JDBCHelper.getConnection(key);
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < rdList.size(); i++) {
                ReigionData fd = rdList.get(i);
                for (int j = 0; j < fd.boundries.size(); j++) {
                    pstmt.setString(1, fd.fir);
                    pstmt.setString(2, fd.area);
                    pstmt.setString(3, fd.sector);
                    pstmt.setInt(4, fd.boundries.get(j).seq);
                    pstmt.setDouble(5, fd.boundries.get(j).lng);
                    pstmt.setDouble(6, fd.boundries.get(j).lat);
                    pstmt.addBatch();
                    if (i % 1000 == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                    }
                }
            }
            pstmt.executeBatch();
            conn.commit();
            pstmt.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double dealWithCoord(String s) {
        if (s.length() < 3) {
            return -1;
        }
        double res = 0;
        String sub = s.substring(1, s.length());
        String dgr = null;
        String min = null;
        String sec = null;
        if (s.contains("N")) {
            dgr = sub.substring(0,2);
            min = sub.substring(2,4);
            sec = sub.substring(4,sub.length());
        }else if (s.contains("E")) {
            dgr = sub.substring(0,3);
            min = sub.substring(3,5);
            sec = sub.substring(5,sub.length());
        }
        res = Double.parseDouble(dgr) + (Double.parseDouble(min) * 60 + Double.parseDouble(sec))/3600;
        return res;
    }
    public  HashMap<String,String>  getSectorName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = BaseTrajectory.class.getResourceAsStream("/sample/data/region.json");
//        HashMap sectorMap = mapper.readValues(is, new TypeReference<HashMap>(){});
        HashMap<String,String> secMap = mapper.readValue(is, HashMap.class);
        return secMap;
    }
    public Map<String, String> getFirName() {
        Map<String, String> firName = new HashMap<>();
        String table = "region_fir";
        ResultSet rs = JDBCHelper.getResultSet(table,"MySQL");
        try{
            while (rs.next()) {
                String fir = rs.getString("名称");
                String firID = rs.getString("情报区代号");
                firName.put(fir, firID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firName;
    }

}

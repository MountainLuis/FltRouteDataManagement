package apply.impl;

import code.apply.bean.FlightPlan;
import code.apply.util.DataTransform;
import code.apply.util.MysqlHelper;
import code.bada.bean.FixPt;
import code.bada.bean.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类用于航班计划已知的情况下，从数据库中读取航班计划。
 */
public class FlightPlanManagement {

    public List<FlightPlan> getPlanData () {
        List<FlightPlan> fltPlans = new ArrayList<>();
        String sql = "select * from flight_plan";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try {
            while (rs.next()) {
             FlightPlan fltPlan = new FlightPlan();
             fltPlan.setFltID(rs.getString("flt_no"));
             fltPlan.setAcftType(rs.getString("acft_type"));
             fltPlan.setToTime(DataTransform.timeString2Int(rs.getString("appear_time")));
             fltPlan.setSidPath(rs.getString("sid_path"));
             fltPlan.setStarPath(rs.getString("star_path"));
             fltPlan.setFltPath(rs.getString("flt_path"));
             fltPlans.add(fltPlan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return fltPlans;
    }
    public Route getRoutes(FlightPlan plan) {
            Route r = new Route();
            if (plan.getSidPath() != null) {
                r.SID = getSidStarData(plan.getSidPath());
            }else {
                r.SID = new ArrayList<>();
            }
            if (plan.getStarPath() != null) {
                r.STAR = getSidStarData(plan.getStarPath());
            } else {
                r.STAR = new ArrayList<>();
            }
            r.enRoute = getRouteData(plan.getFltPath());
            String sql = "select * from flight_level where FLT_PATH='" + plan.getFltPath() + "'";
            ResultSet rs = MysqlHelper.getResultSet(sql);
            try {
              if (rs.next()) {
                r.startEnRouteAltitude = rs.getDouble("StartAltitude");
                r.cruiseAltitude = rs.getDouble("CruiseAlititude");
                r.endEnRouteAltitude = rs.getDouble("EndAltitude");
              }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        return r;
    }
    public List<FixPt> getRouteData(String fltPath) {
        List<FixPt> Pts = new ArrayList<>();
        String sql = "select * from navigation_control where FLT_PATH= '" + fltPath + "'";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try {
            while (rs.next()) {
                FixPt fPt = new FixPt();
                fPt.setID(rs.getString("FixPt_ID"));
                fPt.setLongitude(DataTransform.latLongString2Double(rs.getString("FixPt_Long")));
                fPt.setLatitude(DataTransform.latLongString2Double(rs.getString("FixPt_Lat")));
                fPt.setSegDist(rs.getDouble("Seg_Dist"));
                fPt.setHeightCons(rs.getInt("Height_Cons"));
                fPt.setSpeedCons(rs.getInt("Speed_Cons"));
                fPt.setHeading(rs.getInt("Heading"));
                fPt.setTurnPt(rs.getString("IsTurnPt").equals("Y") ? true : false);
                fPt.setPointSeq(rs.getInt("Point_Seq"));
                Pts.add(fPt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Pts;
    }
    public List<FixPt> getSidStarData(String fltPath) {
        List<FixPt> Pts = new ArrayList<>();
        String sql = "select * from sid_star where FLT_PATH='" + fltPath + "'";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try{
            while (rs.next()) {
                FixPt fPt = new FixPt();
                fPt.setID(rs.getString("FixPt_ID"));
                fPt.setLongitude(DataTransform.latLongString2Double(rs.getString("FixPt_Long")));
                fPt.setLatitude(DataTransform.latLongString2Double(rs.getString("FixPt_Lat")));
                fPt.setSegDist(rs.getDouble("Seg_Dist"));
                fPt.setHeightCons(rs.getInt("Height_Cons"));
                fPt.setSpeedCons(rs.getInt("Speed_Cons"));
                fPt.setHeading(rs.getInt("Heading"));
                fPt.setTurnPt(rs.getString("IsTurnPt").equals("Y") ? true : false);
                fPt.setPointSeq(rs.getInt("Point_Seq"));
                Pts.add(fPt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Pts;
    }
}

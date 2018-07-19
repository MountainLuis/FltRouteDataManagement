package bada.impl;

import bada.bean.AcftState;
import bada.bean.FixPt;
import bada.bean.FltPathType;
import bada.bean.Route;
import bada.core.Global;
import bada.core.PerformanceModel;
import bada.exception.IllegalRouteException;
import bada.util.GeoUtil;
import bada.util.RouteUtil;

import java.util.ArrayList;
import java.util.List;

public class RouteNavigator {

    public static final double NominalBankAngle=35;
    public static final double MaxBankAngle=45;

    private List<FixPt> ptList;
    private double[] remainingDistEnRoute;
    private double[] remainingDistSTAR;
    private double[] remainingDistAirport;

    private int sidIdx=-1;
    private int enRouteIdx=-1;
    private int starIdx=-1;
    private int idx = -1;
    private int routeSize = 0;
    FltPathType fltPathType;
    FixPt target;

    public void onNewState(AcftState state){
        checkFixPtPassed(state);
    }

    public double calBankAngle(AcftState state){
        if(target == null){
            return 0;
        }
        double heading = GeoUtil.calHeading(state.longitude, state.latitude, target.longitude, target.latitude);
        double angle = GeoUtil.calIntersectionAngle(heading, state.heading);
        double c = angle>0?-1.0:1.0;
        angle = Math.abs(angle);
        if(angle < 1){
            return 0;
        }else if(angle <15){
            return  15;
        }if(angle < 30){
            return c * NominalBankAngle;
        }else{
            return c * MaxBankAngle;
        }
    }

    public FltPathType fltPathType(){
        return fltPathType;
    }

    public boolean finished(){
        return target == null;
    }

    public FixPt firstFixPt(){
        return ptList.get(0);
    }

    public FixPt secondFixPt(){
        return ptList.get(1);
    }

    public void setRoute(Route r) {
        makePtList(r);
        routeSize = ptList.size();
        calRemainingDist();
        idx = 1;
        target = ptList.get(idx);
    }

    private void makePtList(Route r){
        ptList = new ArrayList<>();
        sidIdx = enRouteIdx = starIdx  = -1;
        boolean sidEmpty = RouteUtil.isPathEmpty(r.SID);
        boolean enRouteEmpty = RouteUtil.isPathEmpty(r.enRoute);
        boolean starEmpty = RouteUtil.isPathEmpty(r.STAR);

        if(!sidEmpty){
            ptList.addAll(r.SID);
            sidIdx=0;
            enRouteIdx= ptList.size()-1;
            starIdx = ptList.size()-1;
            fltPathType = FltPathType.SID;
        }

        if(!enRouteEmpty){
            if(sidEmpty){
                fltPathType = FltPathType.ENROUTE;
                enRouteIdx = 0;
                ptList.addAll(r.enRoute);
            }else{
                enRouteIdx = ptList.size()-1;
                ptList.addAll(r.enRoute.subList(1, r.enRoute.size()));
            }
            starIdx = ptList.size()-1;
        }

        if(!starEmpty){
            if(enRouteEmpty){
                fltPathType = FltPathType.STAR;
                starIdx = 0;
                ptList.addAll(r.STAR);
            }else{
                starIdx = ptList.size() - 1;
                ptList.addAll(r.STAR.subList(1, r.STAR.size()));
            }
        }
    }

    private void calRemainingDist(){
        remainingDistAirport = new double[routeSize];
        remainingDistEnRoute = new double[routeSize];
        remainingDistSTAR = new double[routeSize];

        for(int i = routeSize-2; i >= 0; i--){
            FixPt p1 = ptList.get(i);
            FixPt p2 = ptList.get(i+1);
            double dist = GeoUtil.calDist(p1.longitude, p1.latitude, p2.longitude, p2.latitude);
            remainingDistAirport[i] = remainingDistAirport[i+1]+dist;
            if(i < starIdx){
                remainingDistSTAR[i] = remainingDistSTAR[i+1]+dist;
            }
            if(i < enRouteIdx){
                remainingDistEnRoute[i] = remainingDistEnRoute[i+1]+dist;
            }
        }

    }

    public double remainingDistToEnroute(AcftState state){
        if(fltPathType != FltPathType.SID || idx >= routeSize){
            return 0;
        }
        double dist = GeoUtil.calDist(state.longitude, state.latitude, target.longitude, target.latitude);
        return dist + remainingDistEnRoute[idx];
    }

    public double remainingDistToSTAR(AcftState state){
        if(idx >= routeSize){
            return 0;
        }
        if(fltPathType != FltPathType.SID && fltPathType != FltPathType.ENROUTE ){
            return 0;
        }
        double dist = GeoUtil.calDist(state.longitude, state.latitude, target.longitude, target.latitude);
        return dist + remainingDistSTAR[idx];
    }

    public double remainingDistToAirport(AcftState state){
        if(idx >= routeSize){ return 0; }
        double dist = GeoUtil.calDist(state.longitude, state.latitude, target.longitude, target.latitude);
        return dist + remainingDistAirport[idx];
    }

    private void checkFixPtPassed(AcftState state){
        if(idx >= routeSize || target == null){
            return;
        }
        boolean passover = false;
        double dLat = target.latitude-state.latitude;
        double dLng = target.longitude-state.longitude;
        double kmPerLng = Global.KMPerLat*Math.cos(state.latitude*Global.Deg2Rad);
        double d1 = Math.abs(dLat*Global.KMPerLat);
        double d2 = Math.abs(dLng*kmPerLng);

        if(d1 < 0.3 && d2 < 0.3){
            passover = true;
        }else if(d1 < 6 && d2 < 6 && idx < routeSize -1){
            FixPt nextPt = ptList.get(idx+1);
            double heading = GeoUtil.calHeading(state.longitude, state.latitude, nextPt.longitude, nextPt.latitude);
            double angle = GeoUtil.calIntersectionAngle(heading, state.heading);
            angle = Math.abs(angle);
            if(angle > 1){
                double dist = GeoUtil.calDist(state.longitude, state.latitude, target.longitude, target.latitude);
                double prediction = calTurnPrediction(state.TAS, NominalBankAngle, angle);
                if(prediction > dist/1.3){
                    passover = true;
                }
            }
        }

        if(!passover){
            if(state.heading>0&&state.heading<=90&&dLat<0&&dLng<=0){
                passover=true;
            }else if(state.heading>90&&state.heading<=180&&dLat>=0&&dLng<0){
                passover=true;
            }else if(state.heading>-90&state.heading<=0&&dLat<=0&&dLng>0){
                passover=true;
            }else if(state.heading>-180&&state.heading<=-90&&dLat>0&&dLng>=0){
                passover=true;
            }
        }

        if(passover){
            idx++;
            if(idx>=routeSize){
                target =null;
                fltPathType = null;
            }else{
                target = ptList.get(idx);
                if(fltPathType == FltPathType.SID){
                    if(idx >= enRouteIdx){
                        fltPathType = FltPathType.ENROUTE;
                    }
                }else if(fltPathType == FltPathType.ENROUTE){
                    if(idx >= starIdx){
                        fltPathType = FltPathType.STAR;
                    }
                }
            }
        }
    }

    private double calTurnPrediction(double Vtas, double bankAngle, double turnAngle){
        double turnRadianRate = PerformanceModel.calRateOfTurnRadian(Vtas, bankAngle*Global.Deg2Rad);
        double turnRadian = turnAngle*Global.Deg2Rad;
        double turnDuration = turnRadian/turnRadianRate;
        double s = Vtas*turnDuration*Global.M2KM;
        double turnRadius = s/turnRadian;
        return turnRadius*Math.tan(turnRadian/2);
    }

    public String dumpRouteInfo(){
        StringBuilder sb  = new StringBuilder();
        sb.append("size:");
        sb.append(routeSize);
        sb.append("\nSID:");
        sb.append(sidIdx);
        sb.append("\nEnRoute:");
        sb.append(enRouteIdx);
        sb.append("\nSTART:");
        sb.append(starIdx);
        sb.append("\n---------\n");
        for(FixPt pt: ptList){
            sb.append(pt.longitude);
            sb.append(",");
            sb.append(pt.latitude);
            sb.append("\n");
        }
        return sb.toString();
    }


//    public void setRoutes(List<FixPt> sid, List<FixPt> enRoute, List<FixPt> star) throws IllegalArgumentException{
//        ptList = new ArrayList<>();
//        sidIdx = enRouteIdx = starIdx  = -1;
//        idx = -1;
//
//        if(isNotEmpty(sid)){
//            ptList.addAll(sid);
//            sidIdx=0;
//            enRouteIdx= ptList.size()-1;
//            starIdx = ptList.size()-1;
//            fltPathType = FltPathType.SID;
//        }
//
//        if(isNotEmpty(enRoute)){
//            if(isPathEmpty(sid)){
//                fltPathType = FltPathType.ENROUTE;
//                enRouteIdx = 0;
//                ptList.addAll(enRoute);
//            }else{
//                FixPt lastSidPt = sid.get(sid.size()-1);
//                if(!enRoute.get(0).ID.equals(lastSidPt.ID)){
//                    throw new IllegalArgumentException("离港点和EnRoute阶段第一个航路点不是同一个点！");
//                }
//                enRouteIdx = ptList.size()-1;
//                ptList.addAll(enRoute.subList(1, enRoute.size()));
//            }
//            starIdx = ptList.size()-1;
//        }
//
//        if(isNotEmpty(star)){
//            if(isPathEmpty(enRoute)){
//                if(isNotEmpty(sid)){
//                    throw new IllegalArgumentException("只有进港和离港阶段！");
//                }
//                fltPathType = FltPathType.STAR;
//                starIdx = 0;
//                ptList.addAll(star);
//            }else{
//                FixPt lastEnRoutePt = enRoute.get(enRoute.size() -1);
//                if(!star.get(0).ID.equals(lastEnRoutePt.ID)){
//                    throw new IllegalArgumentException("进港点和EnRoute阶段最后一个航路点不是同一个点！");
//                }
//                starIdx = ptList.size() - 1;
//                ptList.addAll(star.subList(1, star.size()));
//            }
//        }
//
//        routeSize = ptList.size();
//
//        if(routeSize < 2){
//            throw new IllegalArgumentException("");
//        }
//
//        remainingDistAirport = new double[routeSize];
//        remainingDistEnRoute = new double[routeSize];
//        remainingDistSTAR = new double[routeSize];
//
//        for(int i = routeSize-2; i >= 0; i--){
//            FixPt p1 = ptList.get(i);
//            FixPt p2 = ptList.get(i+1);
//            double dist = GeoUtil.calDist(p1.longitude, p1.latitude, p2.longitude, p2.latitude);
//            remainingDistAirport[i] = remainingDistAirport[i+1]+dist;
//            if(i < starIdx){
//                remainingDistSTAR[i] = remainingDistSTAR[i+1]+dist;
//            }
//            if(i < enRouteIdx){
//                remainingDistEnRoute[i] = remainingDistEnRoute[i+1]+dist;
//            }
//        }
//
//        idx = 1;
//        prevPt = ptList.get(idx-1);
//        target = ptList.get(idx);
//    }

}

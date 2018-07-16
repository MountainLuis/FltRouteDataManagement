package bada.impl;


import code.bada.bean.*;
import code.bada.core.AtmosphereModel;
import code.bada.core.BaseAcftTrajectory;
import code.bada.core.Global;
import code.bada.core.vintent.VIntent;
import code.bada.exception.IllegalRouteException;
import code.bada.util.GeoUtil;
import code.bada.util.RouteUtil;

public class AcftTrajectory extends BaseAcftTrajectory {

    public static final double DefaultTransAlt = 3000;

    private AirlineProcedureModel apModel;
    private RouteNavigator navigator;
    private FltStage stage;
    private long startTime;
    private double transAlt = DefaultTransAlt;
    private double startEnRouteAlt, cruiseAlt, endEnRouteAlt;

    public AcftTrajectory(AcftPerformance performance) {
        super(performance);
        apModel = new AirlineProcedureModel(getPerformanceModel());
        navigator = new RouteNavigator();
    }

    public void setStartTime(long st){
        this.startTime = st;
    }

    public void setRoute(Route r) throws IllegalRouteException {
        RouteUtil.checkRoute(r);
        this.startEnRouteAlt = r.startEnRouteAltitude;
        this.cruiseAlt = r.cruiseAltitude;
        this.endEnRouteAlt = r.endEnRouteAltitude;
        navigator.setRoute(r);
    }

    public FixPt getTarget(){
        return navigator.target;
    }

    @Override
    protected void initState(AcftState state) {
        FixPt pt1 = navigator.firstFixPt();
        FixPt pt2 = navigator.secondFixPt();
        FltPathType pathType = navigator.fltPathType();
        state.longitude = pt1.longitude;
        state.latitude = pt1.latitude;
        state.time = startTime;
        if(pathType == FltPathType.SID){
            state.pAltitude = 2000 * Global.Ft2M;
            state.TAS = apModel.getClimbTASByAltitude(state.pAltitude);
            stage = FltStage.Climb;
        }else if(pathType == FltPathType.ENROUTE){
            state.pAltitude = startEnRouteAlt;
            state.TAS = apModel.getClimbTASByAltitude(state.pAltitude);
            stage = FltStage.Climb;
        }else{
            state.pAltitude = endEnRouteAlt;
            state.TAS = apModel.getDescentTASByAltitude(state.pAltitude);
            stage = FltStage.Descent;
        }
        aModel.setAirInfo(state);
        state.CAS = AtmosphereModel.TAS2CAS(state.TAS, state.airPressure, state.airDensity);
        state.mach = AtmosphereModel.TAS2Mach(state.TAS, state.soundSpeed);
        state.heading = GeoUtil.calHeading(pt1.longitude, pt1.latitude, pt2.longitude, pt2.latitude);
        state.heading = GeoUtil.fixAngle(state.heading);
        state.acftHeading = state.heading;
        state.mass = performance.Mass_ref * Global.T2Kg;
    }

    @Override
    protected FltStage onNewState(AcftState state) {
        navigator.onNewState(state);
        stage = determineStage(state, stage);
        return stage;
    }

    @Override
    protected double horizontalIntention(AcftState state) {
        return navigator.calBankAngle(state);
    }

    @Override
    protected VIntent verticalIntention(AcftState state) {

        FltPathType fltPathType = navigator.fltPathType();
        if(fltPathType == FltPathType.SID){
            return apModel.makeClimbVIntent(state, transAlt, startEnRouteAlt);
        }
        if(fltPathType == FltPathType.STAR){
            return apModel.makeDescentVIntent(state, transAlt, 3000*Global.Ft2M);
        }

        //EnRoute
        if(stage == FltStage.Climb){
            return apModel.makeClimbVIntent(state, transAlt, cruiseAlt);
        }else if(stage == FltStage.Descent){
            return apModel.makeDescentVIntent(state, transAlt, endEnRouteAlt);
        }else{
            return apModel.makeCruiseVIntent(state, transAlt);
        }
    }


    private FltStage determineStage(AcftState state, FltStage prevStage){
        if(navigator.finished()){
            return FltStage.Finished;
        }
        FltPathType fltPathType = navigator.fltPathType();
        if(fltPathType == FltPathType.SID){
            return FltStage.Climb;
        }
        if(fltPathType == FltPathType.STAR){
            return FltStage.Descent;
        }
        if(stage == null){
            return FltStage.Climb;
        }
        if(stage == FltStage.Climb && state.pAltitude >= cruiseAlt){
            return FltStage.Cruise;
        }
        if(stage == FltStage.Cruise){
            double vDist = state.pAltitude - endEnRouteAlt;
            vDist *= Global.M2KM;
            double dist = navigator.remainingDistToSTAR(state);
            if(vDist <= 0 || dist <= 0){
                return FltStage.Descent;
            }
            if(vDist/dist > 0.028){
//            if(vDist/dist > 10/state.TAS){
//                System.out.println("================================= Descent =======================");
//                System.out.println(dist + "," + vDist + "," + state.TAS);
                return FltStage.Descent;
            }
        }
        return prevStage;
    }

}

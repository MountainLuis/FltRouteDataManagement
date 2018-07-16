package bada.core.vintent;


import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;

public class VIntentUtil {


    static AcftStateDetail resolve(AcftStateDetail curState, double thr, double ROCD, double acc, AtmosphereModel aModel, long timeStep){
        curState.thrust = thr;
        curState.ROCD = ROCD;
        AcftStateDetail nextState = new AcftStateDetail();
        nextState.time = curState.time + timeStep;
        nextState.TAS = curState.TAS+acc*timeStep;
        nextState.pAltitude = curState.pAltitude +curState.ROCD*timeStep;
        aModel.setAirInfo(nextState);
        nextState.CAS = AtmosphereModel.TAS2CAS(nextState.TAS, nextState.airPressure, nextState.airDensity);
        nextState.mach = AtmosphereModel.TAS2Mach(nextState.TAS, nextState.soundSpeed);
        return nextState;
    }

    public static VIntent makeAccelerateClimbVIntent(double thr){
        return new ThrFMVIntent(thr, 0.3);
    }
    public static VIntent makeDecelerateDescentVIntent(double thr){
        return new ThrFMVIntent(thr, 0.3);
    }

    public static VIntent makeDecelerateClimbVIntent(double thr){
        return new ThrFMVIntent(thr, 1.7);
    }
    public static VIntent makeAccelerateDescentVIntent(double thr){
        return new ThrFMVIntent(thr, 1.7);
    }

    public static VIntent makeConstLevelVIntent(){
        return new AccROCDVIntent(0,0);
    }

    public static VIntent makeLevelVIntentWithAcc(double acc){
        return new AccROCDVIntent(acc, 0);
    }

    public static VIntent makeLevelVIntentWithThrust(double thr){
        return new ThrROCDVIntent(thr, 0);
    }
}

package bada.core.vintent;


import bada.bean.AcftStateDetail;
import bada.core.AtmosphereModel;
import bada.core.Global;
import bada.core.PerformanceModel;

public class ConstCASVIntent implements VIntent {

    double Thr;

    public ConstCASVIntent(double Thr){
        this.Thr = Thr;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {

        double m2 = curState.mach*curState.mach;
        double x = 1+(AtmosphereModel.K-1)*m2;
        double tmp = 1+Math.pow(x, AtmosphereModel.U2)*(Math.pow(x, AtmosphereModel.U1)-1);
        if(curState.pAltitude <AtmosphereModel.HPtrop){
            tmp=tmp+AtmosphereModel.F2*m2;
        }
        double fM=1/tmp;
        curState.ROCD = (Thr-curState.drag)*curState.TAS*fM/curState.mass/Global.G0;
        curState.thrust = Thr;

        AcftStateDetail nextState = new AcftStateDetail();
        nextState.time = curState.time + timeStep;
        nextState.pAltitude =curState.pAltitude +curState.ROCD*timeStep;
        aModel.setAirInfo(nextState);
        nextState.CAS = curState.CAS;
        nextState.TAS = AtmosphereModel.CAS2TAS(nextState.CAS, nextState.airPressure, nextState.airDensity);
        nextState.mach = AtmosphereModel.TAS2Mach(nextState.TAS, nextState.soundSpeed);

        return nextState;
    }

//    @Override
//    public Result resolve(AcftState state, AirInfo ainfo, AtmosphereModel aModel, double drag, long timeStep) {
//        double m2 = state.mach*state.mach;
//        double x = 1+(AtmosphereModel.K-1)*m2;
//        double tmp = 1+Math.pow(x, AtmosphereModel.U2)*(Math.pow(x, AtmosphereModel.U1)-1);
//        if(state.pAltitude <AtmosphereModel.HPtrop){
//            tmp=tmp+AtmosphereModel.F2*m2;
//        }
//        double fM=1/tmp;
//        AcftState nextState = new AcftState();
//        nextState.time = state.time + timeStep;
//        nextState.ROCD = (Thr-drag)*state.TAS*fM/state.mass/Global.G0;
//        nextState.pAltitude =state.pAltitude +nextState.ROCD*timeStep;
//        AirInfo nextInfo = aModel.calInfo(nextState.pAltitude);
//        nextState.CAS = state.CAS;
//        nextState.TAS = AtmosphereModel.CAS2TAS(nextState.CAS, nextInfo.Pressure, nextInfo.Density);
//        nextState.mach = AtmosphereModel.TAS2Mach(nextState.TAS, nextInfo.SoundSpeed);
//
//        Result r = new Result();
//        r.thrust = Thr;
//        r.nextState = nextState;
//        r.nextAirInfo = nextInfo;
//        return r;
//    }
}

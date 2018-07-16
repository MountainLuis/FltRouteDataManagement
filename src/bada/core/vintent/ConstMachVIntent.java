package bada.core.vintent;

import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;

public class ConstMachVIntent implements VIntent {

    double Thr;
    public ConstMachVIntent(double thr){
        this.Thr = thr;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {

        double fM=1;
        if(curState.pAltitude < AtmosphereModel.HPtrop){
            fM=1+AtmosphereModel.F2*curState.mach*curState.mach*(curState.airTemp-aModel.deltaT)/curState.airTemp;
            fM=1/fM;
        }
        curState.ROCD = (Thr-curState.drag)*curState.TAS*fM/curState.mass/Global.G0;
        curState.thrust = Thr;

        AcftStateDetail nextState = new AcftStateDetail();
        nextState.time = timeStep+curState.time;
        nextState.pAltitude =curState.pAltitude +curState.ROCD*timeStep;
        aModel.setAirInfo(nextState);
        nextState.mach = curState.mach;
        nextState.TAS = AtmosphereModel.mach2TAS(nextState.mach, nextState.soundSpeed);
        nextState.CAS = AtmosphereModel.TAS2CAS(nextState.TAS, nextState.airPressure, nextState.airDensity);
        return nextState;
    }

}

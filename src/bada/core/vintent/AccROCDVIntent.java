package bada.core.vintent;

import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;

public class AccROCDVIntent implements VIntent{

    protected double Acc;
    protected double ROCD;

    public AccROCDVIntent(){}

    public AccROCDVIntent(double Acc, double ROCD){
        this.Acc = Acc;
        this.ROCD = ROCD;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {
        double thr = curState.mass*Global.G0*ROCD/curState.TAS+curState.mass*Acc+curState.drag;
        return VIntentUtil.resolve(curState, thr, ROCD, Acc, aModel, timeStep);
    }

//    @Override
//    public Result resolve(AcftState state, AirInfo ainfo, AtmosphereModel aModel, double drag, long timeStep) {
//        double thr = state.mass*Global.G0*ROCD/state.TAS+state.mass*Acc+drag;
//        return VIntentUtil.makeResult(state, aModel, thr, Acc, ROCD, timeStep);
//    }
}

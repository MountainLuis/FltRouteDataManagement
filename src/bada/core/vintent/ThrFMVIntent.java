package bada.core.vintent;

import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;

public class ThrFMVIntent implements VIntent {

    double Thr;
    double fM;
    public ThrFMVIntent(double thr, double fm){
        this.Thr = thr;
        this.fM = fm;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {
        double power = (this.Thr-curState.drag)*curState.TAS;  //剩余功率
        double g = curState.mass*Global.G0;
        double ROCD = power*fM/g;
        double Acc =  (power-g*ROCD)/curState.mass/curState.TAS;
        return VIntentUtil.resolve(curState, Thr, ROCD, Acc, aModel, timeStep);
    }

}

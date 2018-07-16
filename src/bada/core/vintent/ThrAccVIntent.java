package bada.core.vintent;

import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;

public class ThrAccVIntent implements VIntent {
    double Thr;
    double Acc;
    public ThrAccVIntent(double thr, double acc){
        this.Thr = thr;
        this.Acc = acc;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {
        double ROCD = (Thr-curState.drag-curState.mass*Acc)*curState.TAS/curState.mass/Global.G0;
        return VIntentUtil.resolve(curState, Thr, ROCD, Acc, aModel, timeStep);
    }
}
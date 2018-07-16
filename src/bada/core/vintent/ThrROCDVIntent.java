package bada.core.vintent;

import code.bada.bean.AcftStateDetail;
import code.bada.core.AtmosphereModel;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;

public class ThrROCDVIntent implements VIntent {

    double Thr;
    double ROCD;
    public ThrROCDVIntent(double thr, double ROCD){
        this.Thr = thr;
        this.ROCD = ROCD;
    }

    @Override
    public AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep) {
        double Acc = (Thr-curState.drag)/curState.mass-Global.G0*ROCD/curState.TAS;
        return VIntentUtil.resolve(curState, Thr, ROCD, Acc, aModel, timeStep);
    }

}

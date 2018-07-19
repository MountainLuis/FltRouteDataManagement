package bada.core.vintent;

import bada.bean.AcftStateDetail;
import bada.core.AtmosphereModel;
import bada.core.PerformanceModel;

/**
 * 对应BADA Chapter3 的TotalEnergyModel的计算。
 * 不同的VIntent子类代表不同的输入变量，比如 推力，加速度，fm 等等。resolve函数代表解析过程。
 */
public interface VIntent {

    AcftStateDetail resolve(AcftStateDetail curState, AtmosphereModel aModel, PerformanceModel pModel, long timeStep);

}

package bada.core;


import code.bada.bean.AcftState;
import code.bada.bean.AirInfo;

/**
 * Created by dddda212 on 18-5-9.
 */
public class ISAModel extends AtmosphereModel {

    public static final ISAModel instance = new ISAModel();

    public ISAModel() {
        super(0, 0);
    }


    @Override
    public void setAirInfo(AcftState state){
        double HP = state.pAltitude;
        if(HP < HPtrop){
            state.airTemp = this.T0+BetaT*HP;
            state.airPressure = ISAP0*Math.pow(state.airTemp /ISAT0, F3);
        }else{
            state.airTemp = this.Ttrop;
            state.airPressure = this.Ptrop;
        }
        state.airDensity = state.airPressure /R/state.airTemp;
        state.soundSpeed = Math.pow(KR*state.airTemp, 0.5);
    }


    @Override
    public AirInfo calInfo(double HP){  //通过压高获取所有的信息
        AirInfo ainfo = new AirInfo();
        if(HP < HPtrop){
            ainfo.Temp = this.T0+BetaT*HP;
            ainfo.Pressure = ISAP0*Math.pow(ainfo.Temp /ISAT0, F3);
        }else{
            ainfo.Temp = this.Ttrop;
            ainfo.Pressure = this.Ptrop;
        }
        ainfo.Density = ainfo.Pressure /R/ainfo.Temp;
        ainfo.SoundSpeed = Math.pow(KR*ainfo.Temp, 0.5);
        return ainfo;
    }

    //下面两个函数参考<Revision_of_BADA_atmosphere_model>, 可以通过压高算出高度
    //geopotential altitude
    @Override
    public double calGeoAlt(double HP){
        return HP;  //ISA模型下,压高和位势高度是一样的.
    }
    //geopotential altitude
    @Override
    public double calGeoAltBelowTropWithTemp(double T){
        return (T-T0)/BetaT;
    }

}

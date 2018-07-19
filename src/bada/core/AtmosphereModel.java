package bada.core;

import bada.bean.AcftState;
import bada.bean.AirInfo;

/**
 * 对应BADA Chapter3 的 AtmosphereModel
 * Created by dddda212 on 18-5-8.
 */
public class AtmosphereModel {

    public static final double HPtrop = 11000; //所有大气模型下面的对流层顶层的压高都是一样的.

    public static final double BetaT = -0.0065; //温度随着[压高]变化梯度,[K/m].注意,是压高,不是测量高度
    public static final double R = 287.05287; //空气的气体常数 [m 2 /(K · s 2 )]
    public static final double K = 1.4; //空气的绝热指数, 等熵膨胀系数


    //定义的算子,包括BADA论文提到的和自己定义的
    public static final double U = (K-1)/K;
    public static final double U1 = K/(K-1);
    public static final double U2 = -1/(K-1);
    public static final double U3 = (K-1)/2;
    public static final double F1 = BetaT*R/Global.G0; //这里非常多的计算都要用到这个算子,所以直接定义了这个一个算子.
    public static final double F2 = K*BetaT*R/Global.G0/2;
    public static final double F3 = -Global.G0/BetaT/R;
    public static final double KR = K*R;
    public static final double G0dR = Global.G0/R;
    public static final double DeltaTtrop = HPtrop*BetaT;

    //国际标准大气,标准海平面
    public static final double ISAP0 = 101325; //ISA, MSL, Pressure, [Pa]
    public static final double ISAT0 = 288.15; //ISA, MSL, Temperature, [K]
    public static final double ISAAD0 = 1.225; //ISA, MSL, Density, [Kg/m3]
    public static final double ISAA0 = 340.294; //ISA, MSL, Sound Speed, [m/s]
    public static final double ISATtrop = ISAT0 + HPtrop * BetaT;


    //海平面相关数值, 注意,这里的大气模型的海平面不一定等于标准大气的海平面
    public double deltaT;
    public double deltaP;
    public double Tmsl;
    public double Pmsl;
    public double HPmsl; //当前海平面压力高度,

    public double Tisa_msl; //ISA模型在当前海平面压高下的温度

    //HP=0, 压高为0的情况下
    public double T0;  //

    //HP = HPtrop, 对流层顶层的情况
    public double Ttrop;
    public double Ptrop;



    public AtmosphereModel(double deltaT, double  deltaP){
        this.deltaP = deltaP;
        this.deltaT = deltaT;
        this.Pmsl = deltaP + ISAP0;
        this.HPmsl = ISAT0/BetaT*(Math.pow(Pmsl/ISAP0, F1)-1);
        this.T0 = ISAT0 + deltaT;
        this.Tisa_msl = ISAT0 + BetaT*this.HPmsl;
//        this.Tmsl = this.T0 + BetaT*this.HPmsl; 这个公式同下面公式计算结果是一样的
        this.Tmsl = Tisa_msl + deltaT;
        this.Ttrop = this.T0 + BetaT*HPtrop;
        this.Ptrop = ISAP0 * Math.pow((this.Ttrop-deltaT)/ISAT0, F3);
    }

    public void setAirInfo(AcftState state){
        double HP = state.pAltitude;
        if(HP < HPtrop){
            state.airTemp = this.T0+BetaT*HP;
            state.airPressure = ISAP0*Math.pow((state.airTemp -deltaT)/ISAT0, F3);
        }else{
            state.airTemp = this.Ttrop;
            state.airPressure = this.Ptrop*Math.exp(G0dR/ISATtrop*(HPtrop-HP));
        }
        state.airDensity = state.airPressure /R/state.airTemp;
        state.soundSpeed = Math.pow(KR*state.airTemp, 0.5);
    }

    public AirInfo calInfo(double HP){  //通过压高获取所有的信息
        AirInfo ainfo = new AirInfo();
        if(HP < HPtrop){
            ainfo.Temp = this.T0+BetaT*HP;
            ainfo.Pressure = ISAP0*Math.pow((ainfo.Temp -deltaT)/ISAT0, F3);
        }else{
            ainfo.Temp = this.Ttrop;
            ainfo.Pressure = this.Ptrop*Math.exp(G0dR/ISATtrop*(HPtrop-HP));
        }
        ainfo.Density = ainfo.Pressure /R/ainfo.Temp;
        ainfo.SoundSpeed = Math.pow(KR*ainfo.Temp, 0.5);
        return ainfo;
    }

    //下面两个函数参考<Revision_of_BADA_atmosphere_model>, 可以通过压高算出高度
    //geopotential altitude
    public double calGeoAlt(double HP){
        if(HP <= HPtrop){
            double tmp = Math.log((ISAT0+BetaT*HP)/Tisa_msl);
            return HP-HPmsl+deltaT*tmp/BetaT;
        }
        return  HPtrop+(T0+DeltaTtrop)*(HP-HPtrop)/(ISAT0+DeltaTtrop);
    }
    //geopotential altitude
    public double calGeoAltBelowTropWithTemp(double T){
        double tmp = Math.log((T-deltaT)/Tisa_msl)*deltaT;
        return (T-T0+tmp)/BetaT;
    }

    public static double TAS2Mach(double Vtas, double soundSpeed){
        return Vtas/soundSpeed;
    }

    public static double mach2TAS(double mach, double soundSpeed){
        return mach*soundSpeed;
    }

    public static double CAS2TAS(double Vcas, double P, double AD){
        double tmp = 1+U*ISAAD0*Vcas*Vcas/2/ISAP0;
        tmp = Math.pow(tmp, U1);
        tmp = 1+ISAP0*(tmp-1)/P;
        tmp = Math.pow(tmp,U)-1;
        tmp = 2*P*tmp/U/AD;
        return Math.pow(tmp, 0.5);
    }

    public static double TAS2CAS(double Vtas, double P, double AD){
        double tmp = 1+U*AD*Vtas*Vtas/2/P;
        tmp = Math.pow(tmp, U1);
        tmp = 1+P*(tmp-1)/ISAP0;
        tmp = Math.pow(tmp, U)-1;
        tmp = 2*ISAP0*tmp/U/ISAAD0;
        return Math.pow(tmp, 0.5);
    }

    public static double calTransAlt(double Vcas, double mach){
        double f = (K-1)/2.0;
        double tmp1 = (1+f*Vcas*Vcas/ISAA0/ISAA0);
        tmp1 = Math.pow(tmp1, U1)-1;
        double tmp2 = (1+f*mach*mach);
        tmp2 = Math.pow(tmp2, U1)-1;
        double theta = Math.pow((tmp1/tmp2), -F1);
        return (1000/6.5)*(ISAT0*(1-theta));
    }


}

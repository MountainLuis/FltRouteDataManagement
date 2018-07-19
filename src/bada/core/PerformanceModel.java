package bada.core;

import bada.bean.AcftPerformance;
import bada.bean.AcftState;
import bada.bean.FltStage;




/**
 * 对应BADA Chapter3 的推力，阻力和油耗计算公式
 */
public class PerformanceModel {


    public static class Config{
        public double Vstall;
        public double Vmin;
        public double Cd0;
        public double Cd2;
        public double detalCd0;
        public double Ctdes;
    }
    private Config toCfg, icCfg, crCfg, apCfg, ldCfg;
    private Config cfg;

    public AcftPerformance performance;
    public AtmosphereModel aModel;
    double Fclimb=0;

    public PerformanceModel(AcftPerformance performance){
        this.performance = performance;
        initCfg();
    }

    public PerformanceModel(AcftPerformance performance, AtmosphereModel aModel){
        this.performance = performance;
        setAtmosphereModel(aModel);
        initCfg();
    }

    public void setAtmosphereModel(AtmosphereModel aModel){
        this.aModel = aModel;
        double Ctc5 = performance.Thrust_C_TC5>0?performance.Thrust_C_TC5:0;
        double tmp = aModel.deltaT-performance.Thrust_C_TC4;
        tmp *= Ctc5;
        if(tmp<0){
            tmp = 0;
        }else if(tmp > 0.4){
            tmp = 0.4;
        }
        Fclimb = 1-tmp;
    }



    public void updateConfig(FltStage stage, double Vtas, double pAltitude){
        if(stage == FltStage.Climb){
            if(pAltitude < Global.Hmax_to){
                cfg = toCfg;
            }else if(pAltitude < Global.Hmax_ic){
                cfg = icCfg;
            }else{
                cfg = crCfg;
            }
        }else if(stage == FltStage.Descent){
            if(pAltitude > Global.Hmax_ap){
                cfg = crCfg;
            }else if(Vtas >= crCfg.Vmin+Global.TenKt2MPS){
                cfg = crCfg;
            }else if(pAltitude > Global.Hmax_ld){
                cfg = apCfg;
            }else if(Vtas >= apCfg.Vmin+Global.TenKt2MPS){
                cfg = apCfg;
            }else{
                cfg = ldCfg;
            }
        }else{
            cfg = crCfg;
        }
    }

    public double calMaxClimbThrust(AcftState state){
        double tmp = 1-state.pAltitude /performance.Thrust_C_TC2;
        tmp += performance.Thrust_C_TC3*state.pAltitude *state.pAltitude;
        tmp *= performance.Thrust_C_TC1;
        return tmp*Fclimb;
    }

    public static final double Ct_cr = 0.95;
    public double calMaxCruiseThrust(AcftState state){
        return calMaxClimbThrust(state)*Ct_cr;
    }

    public double calDescentThrust(AcftState state){
        double maxThrClimb = calMaxClimbThrust(state);
        double Hpdes = performance.Thrust_H_des;
        if(Hpdes < Global.Hmax_ap){
            Hpdes = Global.Hmax_ap;
        }
        if(state.pAltitude >Hpdes){
            return maxThrClimb*performance.Thrust_CTdes_hi;
        }
        return maxThrClimb*cfg.Ctdes;
    }


    //转弯率（弧度）
    public static double calRateOfTurnRadian(double Vtas, double bankRadian){
        return Global.G0*Math.tan(bankRadian)/Vtas;
    }

    public double calDrag(double bankRadian, AcftState state){
        //f1为提前算好的一个算子，减少计算量
        double f1 = state.TAS*state.TAS*state.airDensity *performance.Drag_S/2.0;

        double Cl = state.mass*Global.G0/f1/Math.cos(bankRadian);
        double Cd = cfg.Cd0+cfg.Cd2*Cl*Cl + cfg.detalCd0;
        return Cd*f1;
    }

    //消耗油量 [kg/min]
    public double calFuelFow(FltStage stage, double thrust, double Vtas, double pAltitude){
        Vtas = Vtas/Global.Kt2MPS;
        thrust = thrust*Global.N2KN;
        double n = performance.F_flow_Cf1 * (1+Vtas/performance.F_flow_Cf2);
        double fnom = n * thrust;
        double fmin = performance.F_flow_Cf3*(1-pAltitude/performance.F_flow_Cf4);
        if(cfg == apCfg || cfg == ldCfg){
            return Math.max(fnom, fmin);
        }
        if(stage == FltStage.Cruise){
            return fnom*performance.F_flow_C_fCR;
        }
        if(stage == FltStage.Descent){
            return fmin;
        }
        return fnom;
    }

    public double getMinimumSpeed(){
        return cfg.Vmin;
    }


    private void initCfg(){
        toCfg = new Config();
        toCfg.Vstall = performance.Drag_Vstall_TO*Global.Kt2MPS;
        toCfg.Vmin = Global.Cvmin_to * toCfg.Vstall;
        toCfg.Cd0 = performance.Drag_CD0_CR;
        toCfg.Cd2 = performance.Drag_CD2_CR;
        toCfg.Ctdes = performance.Thrust_CTdes_lo;

        icCfg = new Config();
        icCfg.Vstall = performance.Drag_Vstall_IC*Global.Kt2MPS;
        icCfg.Vmin = Global.Cvmin*icCfg.Vstall;
        icCfg.Cd0 = performance.Drag_CD0_CR;
        icCfg.Cd2 = performance.Drag_CD2_CR;
        icCfg.Ctdes = performance.Thrust_CTdes_lo;

        crCfg = new Config();
        crCfg.Vstall = performance.Drag_Vstall_CR*Global.Kt2MPS;
        crCfg.Vmin = Global.Cvmin*crCfg.Vstall;
        crCfg.Cd0 = performance.Drag_CD0_CR;
        crCfg.Cd2 = performance.Drag_CD2_CR;
        crCfg.Ctdes = performance.Thrust_CTdes_lo;

        apCfg = new Config();
        apCfg.Vstall = performance.Drag_Vstall_AP*Global.Kt2MPS;
        apCfg.Vmin = Global.Cvmin*apCfg.Vstall;
        apCfg.Cd0 = performance.Drag_CD0_AP;
        apCfg.Cd2 = performance.Drag_CD2_AP;
        apCfg.Ctdes = performance.Thrust_CTdes_ap;
        if(apCfg.Cd0 == 0){
            apCfg.Cd0 = crCfg.Cd0;
        }
        if(apCfg.Cd2 == 0){
            apCfg.Cd2 = crCfg.Cd2;
        }

        ldCfg = new Config();
        ldCfg.Vstall = performance.Drag_Vstall_LD*Global.Kt2MPS;
        ldCfg.Vmin = Global.Cvmin*ldCfg.Vstall;
        ldCfg.Cd0 = performance.Drag_CD0_LD;
        ldCfg.Cd2 = performance.Drag_CD2_LD;
        if(ldCfg.Cd0 == 0){
            ldCfg.Cd0 = crCfg.Cd0;
        }
        if(ldCfg.Cd2 == 0){
            ldCfg.Cd2 = crCfg.Cd2;
        }
        ldCfg.detalCd0 = performance.Drag_CD0_LDG;

    }
}

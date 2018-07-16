package bada.impl;


import code.bada.bean.AcftPerformance;
import code.bada.bean.AcftState;
import code.bada.bean.AirlineProcedure;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;
import code.bada.core.vintent.ConstCASVIntent;
import code.bada.core.vintent.ConstMachVIntent;
import code.bada.core.vintent.VIntent;
import code.bada.core.vintent.VIntentUtil;

/**
 * 对应BADA Chatper4
 */
public class AirlineProcedureModel {

    public static final double Vd_cl1 = 5;
    public static final double Vd_cl2 = 10;
    public static final double Vd_cl3 = 30;
    public static final double Vd_cl4 = 60;
    public static final double Vd_cl5 = 80;

    public static final double Vd_des1 = 5;
    public static final double Vd_des2 = 10;
    public static final double Vd_des3 = 20;
    public static final double Vd_des4 = 50;

    double[] Vcl, Vcr, Vdes;
    double Mcl, Mcr, Mdes;
    volatile static double[] Hcl, Hcr, Hdes;
    PerformanceModel pModel;

    public AirlineProcedureModel(PerformanceModel performanceModel){
        this.pModel = performanceModel;
        init(new AirlineProcedure(performanceModel.performance));
    }

    public AirlineProcedureModel(PerformanceModel performanceModel, AirlineProcedure ap){
        this.pModel = performanceModel;
        init(ap);
    }

    private void init(AirlineProcedure ap){
        AcftPerformance performance = pModel.performance;
        double Vmin = Global.Cvmin*performance.Drag_Vstall_TO;
        Vcl = new double[]{
                (Vmin+Vd_cl1)*Global.Kt2MPS,
                (Vmin+Vd_cl2)*Global.Kt2MPS,
                (Vmin+Vd_cl3)*Global.Kt2MPS,
                (Vmin+Vd_cl4)*Global.Kt2MPS,
                (Vmin+Vd_cl5)*Global.Kt2MPS,
                Math.min(ap.Vcl_1, 250*Global.Kt2MPS),
                ap.Vcl_2
        };
        Mcl = ap.Mcl;
        Vcr = new double[]{
                Math.min(ap.Vcr_1, 170*Global.Kt2MPS),
                Math.min(ap.Vcr_1, 220*Global.Kt2MPS),
                Math.min(ap.Vcr_1, 250*Global.Kt2MPS),
                ap.Vcr_2
        };
        Mcr = ap.Mcr;
        Vmin = Global.Cvmin*performance.Drag_Vstall_LD;
        Vdes = new double[]{
                (Vmin+Vd_des1)*Global.Kt2MPS,
                (Vmin+Vd_des2)*Global.Kt2MPS,
                (Vmin+Vd_des3)*Global.Kt2MPS,
                (Vmin+Vd_des4)*Global.Kt2MPS,
                Math.min(ap.Vdes_1, 220*Global.Kt2MPS),
                Math.min(ap.Vdes_1, 250*Global.Kt2MPS),
                ap.Vdes_2
        };
        Mdes = ap.Mdes;

        if(Hcl == null){
            synchronized (AirlineProcedureModel.class){
                if(Hcl == null){
                    Hcl = new double[]{
                            1500*Global.Ft2M,3000*Global.Ft2M,4000*Global.Ft2M,
                            5000*Global.Ft2M,6000*Global.Ft2M,10000*Global.Ft2M,
                            Double.MAX_VALUE
                    };
                    Hcr = new double[]{
                            3000*Global.Ft2M,6000*Global.Ft2M,
                            14000*Global.Ft2M,Double.MAX_VALUE
                    };
                    Hdes = new double[]{
                            1000*Global.Ft2M,1500*Global.Ft2M,2000*Global.Ft2M,
                            3000*Global.Ft2M,6000*Global.Ft2M,10000*Global.Ft2M,
                            Double.MAX_VALUE
                    };
                }
            }

        }
    }

    public double getCruiseTASByAltitude(double alt){
        for(int i = 0 ; i < Hcr.length;i++){
            if(alt<Hcr[i]){
                return Vcr[i];
            }
        }
        return Vcr[Vcr.length-1];
    }

    public double getClimbTASByAltitude(double alt){
        for(int i = 0 ; i < Hcl.length; i++){
            if(alt<Hcl[i]){
                return Vcl[i];
            }
        }
        return Vcl[Vcl.length-1];
    }


    public double getDescentTASByAltitude(double alt){
        for(int i = 0; i<Hdes.length; i++){
            if(alt < Hdes[i]){
                return Vdes[i];
            }
        }
        return Vdes[Vdes.length-1];
    }


    // 爬升阶段
    // 使用等CAS爬升。如果速度不够，则先平飞加速。
    public VIntent makeClimbVIntent(AcftState state, double transAlt, double maxAlt){
        if(state.TAS < pModel.getMinimumSpeed()){
            return VIntentUtil.makeLevelVIntentWithThrust(pModel.calMaxClimbThrust(state));
        }
        boolean lvlFlt = state.pAltitude >= maxAlt;  //平飞
        boolean acc = false;  //加速
        if(state.pAltitude > transAlt){
            acc = state.mach < Mcl;
        }else{
            for(int i = 0 ; i < Hcl.length; i++){
                if(state.pAltitude<Hcl[i]){
                    acc = state.TAS < Vcl[i];
                }
            }
        }

        if(lvlFlt&&!acc){
            return VIntentUtil.makeConstLevelVIntent();
        }
        double thr = pModel.calMaxClimbThrust(state);
        if(lvlFlt&&acc){
            return VIntentUtil.makeLevelVIntentWithThrust(thr);
        }
        if(acc){
//            if(state.pAltitude > transAlt){
//                System.out.println("Climb Acc Mach");
//            }
            return VIntentUtil.makeAccelerateClimbVIntent(pModel.calMaxClimbThrust(state));
        }
        if(state.pAltitude > transAlt){
            return new ConstMachVIntent(pModel.calMaxClimbThrust(state));
        }
        return new ConstCASVIntent(pModel.calMaxClimbThrust(state));
    }


    // 巡航阶段
    // 如果速度达不到，则加速平飞。否则等速平飞。
    public VIntent makeCruiseVIntent(AcftState state, double transAlt){
        boolean constLvl = false;
        if(state.pAltitude >transAlt){
            constLvl = (state.mach >= Mcr);
        }else{
            for(int i = 0; i < Hcl.length; i++){
                if(state.pAltitude <Hcl[i]){
                    constLvl = (state.TAS >= Vcl[i]);
                    break;
                }
            }
        }
        if(constLvl){
            return VIntentUtil.makeConstLevelVIntent();
        }
        return VIntentUtil.makeLevelVIntentWithThrust(
                pModel.calMaxCruiseThrust(state)
        );
    }

    // 下降阶段
    // 上一个高度层的速度为最大速度，当前高度层的速度为最小速度。
    // 如果大于最大速度，则平飞减速
    // 如果小于最小速度，则等速下降
    // 否则边减速边下降
    public VIntent makeDescentVIntent(AcftState state, double transAlt, double minAlt){
        if(state.pAltitude >= transAlt){
            if(state.pAltitude < minAlt){
                if(state.mach > Mdes){
                    double thr = pModel.calDescentThrust(state);
                    return VIntentUtil.makeLevelVIntentWithThrust(thr);  //下降推力基本上小于阻力，所以会减速，除非参数错误
                }
                return VIntentUtil.makeConstLevelVIntent();
            }
            double thr = pModel.calDescentThrust(state);
            if(state.mach > Mdes){
                //边减速边下降
                return VIntentUtil.makeDecelerateDescentVIntent(thr); //下降推力基本上小于阻力，所以会减速，除非参数错误
            }
            return new ConstMachVIntent(thr);  //等速下降
        }

        double maxV=Double.MAX_VALUE;
        double minV=0;
        for(int i = 1; i<Hdes.length; i++){
            if(state.pAltitude <Hdes[i]){
                maxV = Vdes[i];
                minV = Vdes[i]-1;
                break;
            }
        }


        if(state.pAltitude <minAlt){ //无需下降
            if(state.TAS<=minV){
                return VIntentUtil.makeConstLevelVIntent(); //等速平飞，不用减速
            }
            //平飞减速
            return VIntentUtil.makeLevelVIntentWithThrust(pModel.calDescentThrust(state)); //下降推力基本上小于阻力，所以会减速，除非参数错误
        }

        double thr = pModel.calDescentThrust(state);
        if(state.TAS> maxV){
            //先减到小于最大速度再下降
            return VIntentUtil.makeLevelVIntentWithThrust(thr); //下降推力基本上小于阻力，所以会减速，除非参数错误
        }else if(state.TAS > minV){
            //边减速边下降
            return VIntentUtil.makeDecelerateDescentVIntent(thr);
        }
        //等速下降
        return new ConstCASVIntent(thr);
    }

}

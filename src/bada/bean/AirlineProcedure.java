package bada.bean;


import bada.core.Global;

public class AirlineProcedure {

    public double Vcl_1;
    public double Vcl_2;
    public double Mcl;
    public double Vcr_1;
    public double Vcr_2;
    public double Mcr;
    public double Vdes_1;
    public double Vdes_2;
    public double Mdes;

    public AirlineProcedure(){

    }

    public AirlineProcedure(AcftPerformance performance){
        Vcl_1 = performance.V_Vclimb1*Global.Kt2MPS;
        Vcl_2 = performance.V_Vclimb2*Global.Kt2MPS;
        Mcl = performance.V_Mclimb;
        Vcr_1  = performance.V_Vcruise1*Global.Kt2MPS;
        Vcr_2 = performance.V_Vcruise2*Global.Kt2MPS;
        Mcr = performance.V_Mcruise;
        Vdes_1 = performance.V_Vdes1*Global.Kt2MPS;
        Vdes_2 = performance.V_Vdes2*Global.Kt2MPS;
        Mdes = performance.V_Mdes;
    }

}

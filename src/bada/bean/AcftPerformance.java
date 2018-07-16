package bada.bean;

public class AcftPerformance {
//    public double S;
//    public double Ctc1, Ctc2, Ctc3, Ctc4, Ctc5, Ctcr, Ctdes_high;
//    public double Hp_des;

    public  int id;
    public  String ACFT_ID;
    public  int Thrust_C_TC1;
    public  int Thrust_C_TC2;
    public  double Thrust_C_TC3;
    public  double Thrust_C_TC4;
    public  double Thrust_C_TC5;
    public  double Thrust_CTdes_lo;
    public  double Thrust_CTdes_hi;
    public  double Thrust_CTdes_ap;
    public  double Thrust_CTdes_ld;
    public  int Thrust_H_des;
    public  int Thrust_Vdes_ref;
    public  double Thrust_Mdes_ref;
    public  double Drag_Clbo;
    public  double Drag_K;
    public  double Drag_S;
    public  int Drag_Vstall_CR;
    public  double Drag_CD0_CR;
    public  double Drag_CD2_CR;
    public  int Drag_Vstall_IC;
    public  double Drag_CD0_IC;
    public  double Drag_CD2_IC;
    public  int Drag_Vstall_TO;
    public  double Drag_CD0_TO;
    public  double Drag_CD2_TO;
    public  int Drag_Vstall_AP;
    public  double Drag_CD0_AP;
    public  double Drag_CD2_AP;
    public  int Drag_Vstall_LD;
    public  double Drag_CD0_LD;
    public  double Drag_CD2_LD;
    public  double Drag_CD0_LDG;
    public  double F_flow_Cf1;
    public  double F_flow_Cf2;
    public  double F_flow_Cf3;
    public  double F_flow_Cf4;
    public  double F_flow_C_fCR;
    public  double Mass_ref;
    public  double Mass_min;
    public  double Mass_max;
    public  double Mass_pal;
    public  int Cons_VMO;
    public  double Cons_MMO;
    public  int Cons_hMO;
    public  int Cons_hmax;
    public  double V_Mdes;
    public  int V_Vdes1;
    public  int V_Vdes2;
    public  double V_Mclimb;
    public  int V_Vclimb1;
    public  int V_Vclimb2;
    public  double V_Mcruise;
    public  int V_Vcruise1;
    public  int V_Vcruise2;



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getACFT_ID() {
        return ACFT_ID;
    }
    public void setACFT_ID(String aCFT_ID) {
        ACFT_ID = aCFT_ID;
    }
    public int getThrust_C_TC1() {
        return Thrust_C_TC1;
    }
    public void setThrust_C_TC1(int thrust_C_TC1) {
        Thrust_C_TC1 = thrust_C_TC1;
    }
    public int getThrust_C_TC2() {
        return Thrust_C_TC2;
    }
    public void setThrust_C_TC2(int thrust_C_TC2) {
        Thrust_C_TC2 = thrust_C_TC2;
    }
    public double getThrust_C_TC3() {
        return Thrust_C_TC3;
    }
    public void setThrust_C_TC3(double thrust_C_TC3) {
        Thrust_C_TC3 = thrust_C_TC3;
    }
    public double getThrust_C_TC4() {
        return Thrust_C_TC4;
    }
    public void setThrust_C_TC4(double thrust_C_TC4) {
        Thrust_C_TC4 = thrust_C_TC4;
    }
    public double getThrust_C_TC5() {
        return Thrust_C_TC5;
    }
    public void setThrust_C_TC5(double thrust_C_TC5) {
        Thrust_C_TC5 = thrust_C_TC5;
    }
    public double getThrust_CTdes_lo() {
        return Thrust_CTdes_lo;
    }
    public void setThrust_CTdes_lo(double thrust_CTdes_lo) {
        Thrust_CTdes_lo = thrust_CTdes_lo;
    }
    public double getThrust_CTdes_hi() {
        return Thrust_CTdes_hi;
    }
    public void setThrust_CTdes_hi(double thrust_CTdes_hi) {
        Thrust_CTdes_hi = thrust_CTdes_hi;
    }
    public double getThrust_CTdes_ap() {
        return Thrust_CTdes_ap;
    }
    public void setThrust_CTdes_ap(double thrust_CTdes_ap) {
        Thrust_CTdes_ap = thrust_CTdes_ap;
    }
    public double getThrust_CTdes_ld() {
        return Thrust_CTdes_ld;
    }
    public void setThrust_CTdes_ld(double thrust_CTdes_ld) {
        Thrust_CTdes_ld = thrust_CTdes_ld;
    }
    public int getThrust_H_des() {
        return Thrust_H_des;
    }
    public void setThrust_H_des(int thrust_H_des) {
        Thrust_H_des = thrust_H_des;
    }
    public int getThrust_Vdes_ref() {
        return Thrust_Vdes_ref;
    }
    public void setThrust_Vdes_ref(int thrust_Vdes_ref) {
        Thrust_Vdes_ref = thrust_Vdes_ref;
    }
    public double getThrust_Mdes_ref() {
        return Thrust_Mdes_ref;
    }
    public void setThrust_Mdes_ref(double thrust_Mdes_ref) {
        Thrust_Mdes_ref = thrust_Mdes_ref;
    }
    public double getDrag_Clbo() {
        return Drag_Clbo;
    }
    public void setDrag_Clbo(double drag_Clbo) {
        Drag_Clbo = drag_Clbo;
    }
    public double getDrag_K() {
        return Drag_K;
    }
    public void setDrag_K(double drag_K) {
        Drag_K = drag_K;
    }
    public double getDrag_S() {
        return Drag_S;
    }
    public void setDrag_S(double drag_S) {
        Drag_S = drag_S;
    }
    public int getDrag_Vstall_CR() {
        return Drag_Vstall_CR;
    }
    public void setDrag_Vstall_CR(int drag_Vstall_CR) {
        Drag_Vstall_CR = drag_Vstall_CR;
    }
    public double getDrag_CD0_CR() {
        return Drag_CD0_CR;
    }
    public void setDrag_CD0_CR(double drag_CD0_CR) {
        Drag_CD0_CR = drag_CD0_CR;
    }
    public double getDrag_CD2_CR() {
        return Drag_CD2_CR;
    }
    public void setDrag_CD2_CR(double drag_CD2_CR) {
        Drag_CD2_CR = drag_CD2_CR;
    }
    public int getDrag_Vstall_IC() {
        return Drag_Vstall_IC;
    }
    public void setDrag_Vstall_IC(int drag_Vstall_IC) {
        Drag_Vstall_IC = drag_Vstall_IC;
    }
    public double getDrag_CD0_IC() {
        return Drag_CD0_IC;
    }
    public void setDrag_CD0_IC(double drag_CD0_IC) {
        Drag_CD0_IC = drag_CD0_IC;
    }
    public double getDrag_CD2_IC() {
        return Drag_CD2_IC;
    }
    public void setDrag_CD2_IC(double drag_CD2_IC) {
        Drag_CD2_IC = drag_CD2_IC;
    }
    public int getDrag_Vstall_TO() {
        return Drag_Vstall_TO;
    }
    public void setDrag_Vstall_TO(int drag_Vstall_TO) {
        Drag_Vstall_TO = drag_Vstall_TO;
    }
    public double getDrag_CD0_TO() {
        return Drag_CD0_TO;
    }
    public void setDrag_CD0_TO(double drag_CD0_TO) {
        Drag_CD0_TO = drag_CD0_TO;
    }
    public double getDrag_CD2_TO() {
        return Drag_CD2_TO;
    }
    public void setDrag_CD2_TO(double drag_CD2_TO) {
        Drag_CD2_TO = drag_CD2_TO;
    }
    public int getDrag_Vstall_AP() {
        return Drag_Vstall_AP;
    }
    public void setDrag_Vstall_AP(int drag_Vstall_AP) {
        Drag_Vstall_AP = drag_Vstall_AP;
    }
    public double getDrag_CD0_AP() {
        return Drag_CD0_AP;
    }
    public void setDrag_CD0_AP(double drag_CD0_AP) {
        Drag_CD0_AP = drag_CD0_AP;
    }
    public double getDrag_CD2_AP() {
        return Drag_CD2_AP;
    }
    public void setDrag_CD2_AP(double drag_CD2_AP) {
        Drag_CD2_AP = drag_CD2_AP;
    }
    public int getDrag_Vstall_LD() {
        return Drag_Vstall_LD;
    }
    public void setDrag_Vstall_LD(int drag_Vstall_LD) {
        Drag_Vstall_LD = drag_Vstall_LD;
    }
    public double getDrag_CD0_LD() {
        return Drag_CD0_LD;
    }
    public void setDrag_CD0_LD(double drag_CD0_LD) {
        Drag_CD0_LD = drag_CD0_LD;
    }
    public double getDrag_CD2_LD() {
        return Drag_CD2_LD;
    }
    public void setDrag_CD2_LD(double drag_CD2_LD) {
        Drag_CD2_LD = drag_CD2_LD;
    }
    public double getDrag_CD0_LDG() {
        return Drag_CD0_LDG;
    }
    public void setDrag_CD0_LDG(double drag_CD0_LDG) {
        Drag_CD0_LDG = drag_CD0_LDG;
    }
    public double getF_flow_Cf1() {
        return F_flow_Cf1;
    }
    public void setF_flow_Cf1(double f_flow_Cf1) {
        F_flow_Cf1 = f_flow_Cf1;
    }
    public double getF_flow_Cf2() {
        return F_flow_Cf2;
    }
    public void setF_flow_Cf2(double f_flow_Cf2) {
        F_flow_Cf2 = f_flow_Cf2;
    }
    public double getF_flow_Cf3() {
        return F_flow_Cf3;
    }
    public void setF_flow_Cf3(double f_flow_Cf3) {
        F_flow_Cf3 = f_flow_Cf3;
    }
    public double getF_flow_Cf4() {
        return F_flow_Cf4;
    }
    public void setF_flow_Cf4(double f_flow_Cf4) {
        F_flow_Cf4 = f_flow_Cf4;
    }
    public double getF_flow_C_fCR() {
        return F_flow_C_fCR;
    }
    public void setF_flow_C_fCR(double f_flow_C_fCR) {
        F_flow_C_fCR = f_flow_C_fCR;
    }
    public double getMass_ref() {
        return Mass_ref;
    }
    public void setMass_ref(double mass_ref) {
        Mass_ref = mass_ref;
    }
    public double getMass_min() {
        return Mass_min;
    }
    public void setMass_min(double mass_min) {
        Mass_min = mass_min;
    }
    public double getMass_max() {
        return Mass_max;
    }
    public void setMass_max(double mass_max) {
        Mass_max = mass_max;
    }
    public double getMass_pal() {
        return Mass_pal;
    }
    public void setMass_pal(double mass_pal) {
        Mass_pal = mass_pal;
    }
    public int getCons_VMO() {
        return Cons_VMO;
    }
    public void setCons_VMO(int cons_VMO) {
        Cons_VMO = cons_VMO;
    }
    public double getCons_MMO() {
        return Cons_MMO;
    }
    public void setCons_MMO(double cons_MMO) {
        Cons_MMO = cons_MMO;
    }
    public int getCons_hMO() {
        return Cons_hMO;
    }
    public void setCons_hMO(int cons_hMO) {
        Cons_hMO = cons_hMO;
    }
    public int getCons_hmax() {
        return Cons_hmax;
    }
    public void setCons_hmax(int cons_hmax) {
        Cons_hmax = cons_hmax;
    }
    public double getV_Mdes() {
        return V_Mdes;
    }
    public void setV_Mdes(double v_Mdes) {
        V_Mdes = v_Mdes;
    }
    public int getV_Vdes1() {
        return V_Vdes1;
    }
    public void setV_Vdes1(int v_Vdes1) {
        V_Vdes1 = v_Vdes1;
    }
    public int getV_Vdes2() {
        return V_Vdes2;
    }
    public void setV_Vdes2(int v_Vdes2) {
        V_Vdes2 = v_Vdes2;
    }
    public double getV_Mclimb() {
        return V_Mclimb;
    }
    public void setV_Mclimb(double v_Mclimb) {
        V_Mclimb = v_Mclimb;
    }
    public int getV_Vclimb1() {
        return V_Vclimb1;
    }
    public void setV_Vclimb1(int v_Vclimb1) {
        V_Vclimb1 = v_Vclimb1;
    }
    public int getV_Vclimb2() {
        return V_Vclimb2;
    }
    public void setV_Vclimb2(int v_Vclimb2) {
        V_Vclimb2 = v_Vclimb2;
    }
    public double getV_Mcruise() {
        return V_Mcruise;
    }
    public void setV_Mcruise(double v_Mcruise) {
        V_Mcruise = v_Mcruise;
    }
    public int getV_Vcruise1() {
        return V_Vcruise1;
    }
    public void setV_Vcruise1(int v_Vcruise1) {
        V_Vcruise1 = v_Vcruise1;
    }
    public int getV_Vcruise2() {
        return V_Vcruise2;
    }
    public void setV_Vcruise2(int v_Vcruise2) {
        V_Vcruise2 = v_Vcruise2;
    }




}

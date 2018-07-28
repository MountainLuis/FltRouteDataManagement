package dataManager;

import org.junit.Test;

import static org.junit.Assert.*;

public class FltPlanSplitTest {

    @Test
    public void isDomesticAP() {
        FltPlanSplit fps = new FltPlanSplit();
        String ap0 = "ZMUB";
        String ap1 = "ZBAA";
        String ap2 = "ZSPD";
        String ap3 = "ZKPY";
        String ap4 = "RCAA";
        System.out.println(fps.isDomesticAP(ap0));
        System.out.println(fps.isDomesticAP(ap1));
        System.out.println(fps.isDomesticAP(ap2));
        System.out.println(fps.isDomesticAP(ap3));
        System.out.println(fps.isDomesticAP(ap4));
    }
}
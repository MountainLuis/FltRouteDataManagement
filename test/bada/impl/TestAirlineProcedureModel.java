package bada.impl;

import code.bada.bean.AcftPerformance;
import code.bada.core.ISAModel;
import code.bada.core.PerformanceModel;
import org.junit.Test;

import java.io.IOException;

public class TestAirlineProcedureModel extends TestBase {

    private AirlineProcedureModel apm;
    private AcftPerformance performance;
    public TestAirlineProcedureModel() throws IOException {
        performance = acftPerformances.get("A321");
        PerformanceModel pModel = new PerformanceModel(performance, ISAModel.instance);
        apm = new AirlineProcedureModel(pModel);
    }

    @Test
    public void TestClimbTAS(){
        System.out.println("Test Climb TAS =========");
        double hs[] = new double[]{
              400, 800,  1100, 1400, 1700, 2500, 4000
        };

        for(double h: hs){
            double v = apm.getClimbTASByAltitude(h);
            System.out.println("Alt:" + h + "\tV:" + v);
        }

        System.out.println(performance.V_Mclimb);
    }

    @Test
    public void TestCruiseTAS(){
        System.out.println("Test Cruise TAS ============");
        double alts[] = new double[]{
            500, 1500, 4000, 4300
        };

        for(double h: alts){
            double v = apm.getCruiseTASByAltitude(h);
            System.out.println("Alt:" + h + "\tV:" + v);
        }
        System.out.println(performance.V_Mcruise);
    }


    @Test
    public void TestDescentTAS(){
        System.out.println("Test Descent TAS ==========");
        double alts[] = new double[]{
                250, 400, 580, 800, 1700, 2900, 3500
        };

        for(double h: alts){
            double v = apm.getDescentTASByAltitude(h);
            System.out.println("Alt:" + h + "\tV:" + v);
        }
        System.out.println(performance.V_Mdes);
    }

}

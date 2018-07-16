package apply.montecarlo;

import code.apply.bean.FlightPlan;
import org.junit.Test;

import java.util.List;

public class TestAcftGenerator {

    @Test
    public void testRoutePlanGenerator() {
        List<FlightPlan> plans = AcftGenerator.routeAcftGenerator("A593");
        for (FlightPlan plan: plans) {
            System.out.println(plan);
        }
    }




    @Test
    public void testAcftplanGenerator() {
        String path = "A593";
        String path2 = "G20";
        int time = 233;
        for (int i = 0; i < 10; i++) {
            FlightPlan p = null;
            if (i % 2 == 0) {
                p = AcftGenerator.acftPlanGenerator(path ,time, i);
            }else {
                p = AcftGenerator.acftPlanGenerator(path2, time, i);
            }
            System.out.println(p);
        }
    }

    @Test
    public void testAcftTypeGenerator() {
        for (int i = 0; i < 10; i++) {
            System.out.println(AcftGenerator.acftTypeGenerator());
        }
    }
    @Test
    public void testGetPossionVariable() {
        for (int i = 0; i < 10; i++) {
            System.out.print(AcftGenerator.getPossionVariable(600) + " ");
//            System.out.print(AcftGenerator.TimeInterval(600) + " \n" );

        }
    }

}

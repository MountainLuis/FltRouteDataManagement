package bada.impl;

import bada.bean.AcftPerformance;
import bada.bean.AcftState;
import bada.bean.AcftStateDetail;
import bada.bean.Route;
import bada.exception.IllegalRouteException;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestTrajALL extends TestBase{
    public TestTrajALL() throws IOException {
    }
    @Test
    public void Test() {
        for (int i = 0; i < 10; i++) {
            String callSign = "CCA" + "210" + i;
            try {
                TestTrajectory(callSign, i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalRouteException e) {
                e.printStackTrace();
            }
        }

    }
    public void TestTrajectory(String callSign, int time) throws IOException, IllegalRouteException {

        AcftPerformance performance = acftPerformances.get("A320");
        Route r = routes.get(0);
        r.startEnRouteAltitude = 6500;
        r.cruiseAltitude = 10200;
        r.endEnRouteAltitude = 7000;

        List<AcftState> result = new LinkedList<>();

        AcftTrajectory trajectory = new AcftTrajectory(performance);

        trajectory.setStartTime(time);
        trajectory.setRoute(r);

        while(true){
            AcftStateDetail state = (AcftStateDetail) trajectory.nextStep();
            if(state == null){
                break;
            }
            result.add(state);
        }
        System.out.println(callSign + " " + time + " size" + result.size());
//        output("trajectory_test.csv", result);
    }
 }

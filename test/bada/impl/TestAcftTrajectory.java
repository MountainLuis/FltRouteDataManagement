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

public class TestAcftTrajectory extends TestBase {

    public TestAcftTrajectory() throws IOException {
    }

    @Test
    public void TestTrajectory() throws IOException, IllegalRouteException {
        AcftPerformance performance = acftPerformances.get("A320");
        Route r = routes.get(0);
        r.startEnRouteAltitude = 6500;
        r.cruiseAltitude = 10200;
        r.endEnRouteAltitude = 7000;

        List<AcftState> result = new LinkedList<>();

        AcftTrajectory trajectory = new AcftTrajectory(performance);
        trajectory.setStartTime(0);
        trajectory.setRoute(r);

        while(true){
            AcftStateDetail state = (AcftStateDetail) trajectory.nextStep();
            if(state == null){
                break;
            }
            result.add(state);
            System.out.println(state.time + ",\t"+ state.longitude + ","+state.latitude + ", " + state.pAltitude + ", " + state.fltStage + "," + state.mach);
        }
        output("trajectory_test.csv", result);
    }
}

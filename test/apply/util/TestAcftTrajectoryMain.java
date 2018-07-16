package apply.util;

import code.apply.bean.FlightPlan;
import code.apply.impl.AcftTrajectoryMain;
import code.bada.bean.AcftState;
import code.bada.exception.IllegalRouteException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestAcftTrajectoryMain {

    @Test
    public void testTrajectoryAcft() throws IOException, IllegalRouteException {
        AcftTrajectoryMain acftTraj = new AcftTrajectoryMain();
        FlightPlan fltPlan = new FlightPlan();
        fltPlan.setFltID("CDG4817");
        fltPlan.setAcftType("B738");
        fltPlan.setToTime(40346);
        fltPlan.setSidPath("ZSJN01-SID-WFG_61X");
        fltPlan.setStarPath(null);
        fltPlan.setFltPath("zsjn-zsyt");
        long tic = System.currentTimeMillis();
        List<AcftState> tracks = acftTraj.TrajectoryAcft(fltPlan);
        for (int i = 0; i < tracks.size(); i++) {
            AcftState state = tracks.get(i);
            if (i % 100 == 0) {
                System.out.println(i + " : " + state.longitude + " : " + state.latitude + " : " + state.time
                        + " : " + state.pAltitude + " : " + state.heading + "");
            }
        }
//        acftTraj.insertData(tracks, "CDG4817");
        System.out.println(tracks.size());
        long time = System.currentTimeMillis() - tic;
        System.out.println(time);
    }

}

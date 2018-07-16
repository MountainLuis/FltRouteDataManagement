package apply.montecarlo;

import code.apply.bean.AcftPtTime;
import code.apply.bean.FlightPlan;
import code.apply.impl.AcftTrajectoryMain;
import code.bada.bean.AcftState;
import code.bada.exception.IllegalRouteException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestAcftTrajectoryMain {

    @Test
    public void test() throws IOException, IllegalRouteException {
        AcftTrajectoryMain atm = new AcftTrajectoryMain();

        List<FlightPlan> plans = AcftGenerator.routeAcftGenerator("H24");
        List<AcftState> tracks = atm.TrajectoryAcftForMonteCarlo(plans.get(0));
        System.out.println(plans.get(0).getFltID() + " : " + tracks.size());
        System.out.println(tracks.get(tracks.size()-1).time);
    }
    @Test
    public void testRecord() throws IOException, IllegalRouteException {
        AcftTrajectoryMain atm = new AcftTrajectoryMain();

        List<FlightPlan> plans = AcftGenerator.routeAcftGenerator("H24");
        List<AcftPtTime> ptTimes = atm.TrajectoryForRecordTime(plans.get(0));
        for (int i = 0; i < ptTimes.size(); i++) {
            System.out.println(ptTimes.get(i).acftID + " " + ptTimes.get(i).ptID
                    + " " + ptTimes.get(i).time + " " + ptTimes.get(i).route);
        }

    }
    @Test
    public void testMoreAcftPtTimes() throws IOException, IllegalRouteException {
        AcftTrajectoryMain atm = new AcftTrajectoryMain();
        List<FlightPlan> plans = AcftGenerator.routeAcftGenerator("A1");
        List<AcftPtTime> allPtTimes = new ArrayList<>();
        for (FlightPlan plan: plans) {
            allPtTimes.addAll(atm.TrajectoryForRecordTime(plan));
        }
        for(int i = 0; i < allPtTimes.size(); i++) {
            System.out.println(allPtTimes.get(i).acftID + " "
                    + allPtTimes.get(i).ptID + " " + allPtTimes.get(i).time
                    + " " + allPtTimes.get(i).route);
        }
    }


    @Test
    /**
     * 这里得到的结论是，同一条航路，不同机型飞行所用时间不同，生成的轨迹点的个数也就不同。
     */
    public void testMoreAcft() throws IOException, IllegalRouteException {
        AcftTrajectoryMain atm = new AcftTrajectoryMain();
        List<FlightPlan> plans = AcftGenerator.routeAcftGenerator("A1");
        for (FlightPlan plan: plans) {
            List<AcftState> tracks = atm.TrajectoryAcftForMonteCarlo(plan);
            System.out.println(plan.getFltID() + " : " + tracks.size() + " : "
                    + plan.getAcftType() + " " + plan.getToTime());
        }
    }

}

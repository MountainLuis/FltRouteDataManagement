package simulation;

import bada.bean.AcftState;
import bada.bean.FixPt;
import bada.exception.IllegalRouteException;
import bean.FltPlan;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class AbroadFlightSimulationTest {

    @Test
    public void dealWithAllData() {
    }

    @Test
    public void trajectoryAcft() throws IOException {
        AbroadFlightSimulation afs = new AbroadFlightSimulation();
        FltPlan plan = new FltPlan("CCA1207", "RCMQ","ZSWX");
        plan.acft_type = "A321";
        plan.regitration_num = "B16215";
        plan.dep_time = "201806011535";
        plan.flt_path = "HLG1B-HLG-APU-PIANO-BYWAY-CUBIT-VIOLA-OLIVE-SULEM-OKATO-DST-BZ-OSPAM-ENLET-UGAGO-" +
                "TOL-CJ-KAKIS-NIVIK-TAPEN-GOSRO-LUPVI-P282-ESBAG-SASAN";
        try {
//            List<AcftState> states = afs.TrajectoryAcft(plan);
            Set<FixPt> states = afs.TrajectoryAcft(plan);
            System.out.println(states.size());
            Iterator iter = states.iterator();
            while (iter.hasNext()) {
                FixPt fp = (FixPt) iter.next();
                System.out.println(fp);
            }
            System.out.println(afs.ptTimes.size());
            for (int i = 0; i < afs.ptTimes.size(); i++) {
                System.out.println(afs.ptTimes.get(i));
            }
//            for (int i = 0; i < states.size(); i++) {
//                System.out.println(states.get(i).time + " " + states.get(i).latitude);
//            }
        } catch (IllegalRouteException e) {
            e.printStackTrace();
        }
    }
}
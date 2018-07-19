package simulation;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BaseTrajectoryTest {

    @Test
    public void output() throws IOException {
        BaseTrajectory bt = new BaseTrajectory();
        for (String acft : bt.acftPerformances.keySet()) {
            System.out.println(acft);
        }
    }

    @Test
    public void insertData() {
    }
}
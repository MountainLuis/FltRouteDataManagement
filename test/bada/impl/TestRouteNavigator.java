package bada.impl;

import code.bada.bean.AcftState;
import code.bada.bean.FixPt;
import code.bada.core.Global;
import code.bada.core.PerformanceModel;
import code.bada.exception.IllegalRouteException;
import code.bada.util.GeoUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestRouteNavigator extends TestBase {

    private RouteNavigator navigator;

    public TestRouteNavigator() throws IOException, IllegalRouteException {
        navigator = new RouteNavigator();
        navigator.setRoute(routes.get(0));
    }


    @Test
    public void TestRun() throws IOException {
        List<AcftState> result = new LinkedList<>();
        AcftState state = initState();
//        System.out.println(navigator.dumpRouteInfo());

        long start = System.currentTimeMillis();
        int count =0;
        while(!navigator.finished()){
            navigator.onNewState(state);
            double bankAngle = navigator.calBankAngle(state);
            state = nextState(state, bankAngle);
            result.add(state);
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println("Duration: " + (end-start) + ", " + count);
        output("route_navigator_test.csv", result);
    }



    private AcftState nextState(AcftState state, double bankAngle){
        AcftState nextState  = new AcftState();
        long timeStep = 1;
        nextState.time = state.time+timeStep;
        nextState.TAS = 150.0;
        double v = 150.0;
        double s = v * timeStep*Global.M2KM;
        if(bankAngle == 0){
            GeoUtil.calDestPt(state, nextState, state.heading, s);
            nextState.heading = state.heading;
        }else{
            double bankRadian = bankAngle*Global.Deg2Rad;
            double turnRadian = PerformanceModel.calRateOfTurnRadian(state.TAS, bankRadian) * timeStep;
            double turnAngle = turnRadian*Global.Rad2Deg;
//            System.out.println("bank: " + bankRadian + ", bank:" + bankAngle + ", turn:" + turnRadian + ", turn:" + turnAngle);
            double turnRadius = s/turnRadian;  //转弯半径=走过路程/转过的弧度
            double dist = Math.sin(turnRadian/2)*turnRadius*2;  //走过的直线距离
            double angle = state.heading+turnAngle/2;  //两点之间的夹角
            GeoUtil.calDestPt(state, nextState, angle, dist);
            nextState.heading = GeoUtil.fixAngle(state.heading+turnAngle);
            nextState.acftHeading = nextState.heading;
        }
        return nextState;
    }

    private AcftState initState(){
        AcftState state = new AcftState();
        state.time = 0;
        FixPt pt1 = navigator.firstFixPt();
        FixPt pt2 = navigator.secondFixPt();
        state.TAS = 150;
        state.longitude = pt1.longitude;
        state.latitude = pt1.latitude;
        state.heading = GeoUtil.calHeading(pt1.longitude, pt1.latitude, pt2.longitude, pt2.latitude);
        return state;
    }



}

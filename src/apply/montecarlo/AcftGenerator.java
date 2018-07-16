package apply.montecarlo;

import code.apply.bean.FlightPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AcftGenerator {
    private static Random rand = new Random(47);

    /**
     * 为每条航路生成间隔时间按泊松分布的30架航班的航班计划List
     * @param path
     * @return
     */
    public static List<FlightPlan> routeAcftGenerator(String path) {
        List<FlightPlan> plans = new ArrayList();
        int index = 0;
        int time = rand.nextInt(100);
        for (int i = 0; i < 30; i++) {  //todo
            FlightPlan plan = acftPlanGenerator(path, time, index++);
            plans.add(plan);
            time += getPossionVariable(300);  //todo
         }
         return plans;
    }

    /**
     * 根据传入的航路/时间和次序生成对应的随机航班的航班计划
     * @param path
     * @param time
     * @param index
     * @return
     */
    public static FlightPlan acftPlanGenerator(String path, int time, int index) {
        FlightPlan plan = new FlightPlan();
        plan.setFltID(path + "-" + index);
        plan.setAcftType(acftTypeGenerator());
        plan.setToTime(time);
        plan.setFltPath(path);
        plan.setSidPath(null);
        plan.setStarPath(null);
        return plan;
    }
    /**
     * 按概率生成不同机型
     */
    public static String acftTypeGenerator() {
        String  AircraftType = null;
        double rnd=Math.random();
        if (rnd<0.3)
            AircraftType="A320";
        else if (rnd>=0.3 && rnd<0.5)
            AircraftType="B738";
        else if (rnd>=0.5 && rnd<0.62)
            AircraftType="A321";
        else if (rnd>=0.62 && rnd<0.72)
            AircraftType="B737";
        else if (rnd>=0.72 && rnd<0.8)
            AircraftType="A332";
        else if (rnd>=0.8 && rnd<0.88)
            AircraftType="B772";
        else if (rnd>=0.88 && rnd<0.94)
            AircraftType="A319";
        else if (rnd>=0.94 && rnd<=1)
            AircraftType="B744";
        return AircraftType;
    }

    /**
     * 生成服从泊松分布的随机数
     * @param lambda
     * @return
     */
    public static int getPossionVariable(double lambda) {
        int x = 0;
        double y = Math.random();
        double cdf = getPossionProbability(x, lambda);
        while (cdf < y) {
            x++;
            cdf += getPossionProbability(x, lambda);
        }
        return x;
    }
    private static double getPossionProbability(int k, double lambda) {
        double c = Math.exp(-lambda), sum = 1;
        for (int i = 1; i < k; i++) {
            sum *= lambda / i;
        }
        return sum * c;
    }

}

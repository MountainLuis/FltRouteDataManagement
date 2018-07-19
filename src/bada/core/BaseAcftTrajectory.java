package bada.core;


import bada.bean.AcftPerformance;
import bada.bean.AcftState;
import bada.bean.AcftStateDetail;
import bada.bean.FltStage;
import bada.core.vintent.VIntent;
import bada.util.GeoUtil;

/**
 * Created by dddda212 on 18-5-9.
 */
public abstract class BaseAcftTrajectory {

    public static final long DefaultTimeStep = 4;

    protected AcftPerformance performance;
    protected AtmosphereModel aModel;
    protected PerformanceModel pModel;
    long timeStep = DefaultTimeStep;
    double timeStepMin = ((double)(DefaultTimeStep))/60.0;
    private AcftStateDetail curState, nextState;
    private boolean done;


    public BaseAcftTrajectory(AcftPerformance performance){
        this.performance = performance;
        this.aModel = ISAModel.instance;
        pModel = new PerformanceModel(performance);
        setAtmosphereModel(aModel);
    }

    protected abstract void initState(AcftState state);
    protected abstract FltStage onNewState(AcftState state);
    protected abstract double horizontalIntention(AcftState state);
    protected abstract VIntent verticalIntention(AcftState state);


    public AcftState nextStep(){
        if(done){
            return null;
        }
        if(nextState == null){
            nextState = new AcftStateDetail();
            initState(nextState);
        }
        curState = nextState;
        nextState = null;
        FltStage stage = onNewState(curState);
        if(stage == FltStage.Finished){
            done = true;
            return curState;
        }
        curState.fltStage = stage;
        pModel.updateConfig(stage, curState.TAS, curState.pAltitude);
        nextState = calNextState();
        return curState;
    }

    private AcftStateDetail calNextState(){

        double bankAngle = horizontalIntention(curState);
        double bankRadian = bankAngle*Global.Deg2Rad;
        //根据当前的翻滚角度, 计算当前的阻力
        curState.drag = pModel.calDrag(bankRadian, curState);
        VIntent vIntent = verticalIntention(curState);

        //根据TEM模型,计算垂直分量,包括:
        //当前状态(curState)的ROCD(爬升率), thrust(推力)
        //下一个状态(nextState)的pAltitude(压力高度), TAS(真空速), CAS(表速), mach(马赫), 空气密度, 温度,压力,音速
        AcftStateDetail nextState = vIntent.resolve(curState, aModel, pModel, timeStep);
        //计算水平分量,包括:
        //当前状态的水平速度,下一个状态的经纬度
        calNextCoordinate(curState, nextState, bankRadian);
        //计算下一状态的质量
        double fuelfow = pModel.calFuelFow(curState.fltStage, curState.thrust, curState.TAS, curState.pAltitude);
        nextState.mass = curState.mass - fuelfow*timeStepMin;

        return nextState;
    }

    //计算水平方向
    void calNextCoordinate(AcftStateDetail state, AcftStateDetail nextState, double bankRadian){
        state.HTAS = 0;
        if(Math.abs(state.ROCD) < Math.abs(state.TAS)){
            state.HTAS = Math.sqrt(state.TAS*state.TAS-state.ROCD*state.ROCD);
        }
        double hS = state.HTAS*timeStep*Global.M2KM;
        if(bankRadian==0){
            nextState.heading = state.heading;
            nextState.acftHeading = state.heading;
            GeoUtil.calDestPt(state, nextState, state.heading, hS);
        }else{
            double turnRadian = PerformanceModel.calRateOfTurnRadian(state.TAS, bankRadian) * timeStep;
            double turnAngle = turnRadian*Global.Rad2Deg;
            double turnRadius = hS/turnRadian;  //转弯半径=走过路程/转过的弧度
            double dist = Math.sin(turnRadian/2)*turnRadius*2;  //走过的直线距离
            double angle = state.heading+turnAngle/2;  //两点之间的夹角
            GeoUtil.calDestPt(state, nextState, angle, dist);
            nextState.heading = GeoUtil.fixAngle(state.heading+turnAngle);
            nextState.acftHeading = nextState.heading;
        }
    }


    public void setAtmosphereModel(AtmosphereModel aModel){
        this.aModel = aModel;
        pModel.setAtmosphereModel(aModel);
    }

    public AtmosphereModel getAtmosphereModel(){
        return aModel;
    }

    public AcftPerformance getAcftPerformance(){
        return performance;
    }

    public PerformanceModel getPerformanceModel(){
        return pModel;
    }

    public void setTimeStep(long step){
        this.timeStep = step;
        this.timeStepMin = ((double)(timeStep))/60.0;
    }

    public AcftState getCurState(){return curState;}


}

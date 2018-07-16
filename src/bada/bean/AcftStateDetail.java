package bada.bean;

public class AcftStateDetail extends AcftState {
    public double HTAS;     //水平方向速度分量
    public double ROCD;     //爬升率,速度方向的分量
    public double thrust;   //推理
    public double drag;     //阻力

    public FltStage fltStage;
}

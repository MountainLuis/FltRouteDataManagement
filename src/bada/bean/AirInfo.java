package bada.bean;

public class AirInfo {
    public double Pressure;
    public double Temp;
    public double Density;
    public double SoundSpeed;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Pressure: ");
        sb.append(Pressure);
        sb.append("\nTemp:");
        sb.append(Temp);
        sb.append("\nDensity:");
        sb.append(Density);
        sb.append("\nSoundSpeed:");
        sb.append(SoundSpeed);
        return sb.toString();
    }

    public String toCSV(){
        return String.format("%f,%f,%f,%f", Temp, Pressure, Density, SoundSpeed);
    }


}

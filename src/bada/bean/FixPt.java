package bada.bean;

public class FixPt {

    public String ID;
    public double longitude;
    public double latitude;
    public double segDist;
    public int heightCons;
    public int speedCons;
    public int heading;
    public boolean isTurnPt;
    public int pointSeq;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSegDist() {
        return segDist;
    }

    public void setSegDist(double segDist) {
        this.segDist = segDist;
    }

    public int getHeightCons() {
        return heightCons;
    }

    public void setHeightCons(int heightCons) {
        this.heightCons = heightCons;
    }

    public int getSpeedCons() {
        return speedCons;
    }

    public void setSpeedCons(int speedCons) {
        this.speedCons = speedCons;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public boolean isTurnPt() {
        return isTurnPt;
    }

    public void setTurnPt(boolean turnPt) {
        isTurnPt = turnPt;
    }

    public int getPointSeq() {
        return pointSeq;
    }

    public void setPointSeq(int pointSeq) {
        this.pointSeq = pointSeq;
    }
    public String toString() {
        return ID + " " +longitude;
    }
}

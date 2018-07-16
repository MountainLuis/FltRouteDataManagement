package bada.util;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestGeoUtil {

    //北京密云
    double lng1 = 116.85;
    double lat1 = 40.37;

    //天津
    double lng2 = 117.2;
    double lat2 = 39.13;

    double heading = 167.64381301423586;
    double dist = 141.2485818846804;

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        TestGeoUtil obj = TestGeoUtil.class.newInstance();
        Method method = TestGeoUtil.class.getMethod("TestCalHeading");
        method.invoke(obj);
    }

    @Test
    public void TestCalHeading(){
        System.out.println("test cal heading-------");
        double heading = GeoUtil.calHeading(lng1, lat1, lng2, lat2);
        assert Math.abs(heading-this.heading) < 0.0001;
    }

    @Test
    public void TestCalDist(){
        System.out.println("test cal dist -------");
        double dist = GeoUtil.calDist(lng1, lat1, lng2, lat2);
        double dist1 = GeoUtil.calDist(lng2, lat2, lng1, lat1);
        assert Math.abs(dist- this.dist) < 0.0001;
        assert Math.abs(dist1- this.dist) < 0.0001;
    }

    @Test
    public void TestCalDestPt(){
        System.out.println("test cal coord -------");
        double heading = this.heading;
        heading = -360+heading;
        double[] c = GeoUtil.calDestPt(lng1, lat1, heading, dist);
        System.out.println(c[0]+","+ c[1]);
        assert Math.abs(c[0]-lng2) < 0.0001;
        assert Math.abs(c[1]-lat2) < 0.0001;
    }

    @Test
    public void TestParseLngLat(){
        double d = Double.parseDouble("ABC");
        System.out.println(d);
    }
}

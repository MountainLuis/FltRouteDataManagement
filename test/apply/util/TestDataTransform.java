package apply.util;

import org.junit.Test;

public class TestDataTransform {

    @Test
    public void testTimeString2Int() {
        String appear = "20120304010101";
        System.out.println(DataTransform.timeString2Int(appear));
    }
    @Test
    public void testLonLatString2Double() {
        String lon = "1232848";
        String lat = "413825";
        System.out.println(DataTransform.latLongString2Double(lon));
        System.out.println(DataTransform.latLongString2Double(lat));
    }
}

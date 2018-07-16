package bada.core;

//import code.bada.bean.AirInfo;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

public class TestISA {

    @Test
    public void TestCalInfo(){

        double hp = 0;
        while(hp < 14000){
            System.out.println("Alt: " + hp);
//            System.out.println(ISAModel.instance.calInfo(hp));
            System.out.println("============");
            hp += 1000;
        }
    }


    @Test
    public void TestOutput() throws IOException {
        double hp = 0;
        while(hp < 14000){
//            AirInfo airInfo = ISAModel.instance.calInfo(hp);
//            String str = airInfo.toCSV();
//            str = hp + "," + str;
//            System.out.println(str);
            hp += 500;
        }
    }
}

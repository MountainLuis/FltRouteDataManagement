package util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DBPropertiesUtilTest {
    static public String getPath()
    {

               File file=new File("");
               String abspath=file.getAbsolutePath();
             return abspath;
    }

    @Test
    public void getDriverProperties() {
//        String path = DBPropertiesUtil.class.getResource("DBPropertiesUtil.class").toString();
        System.out.println(DBPropertiesUtil.getDriverProperties("MySQL"));
//        System.out.println(getPath());
    }

    @Test
    public void getUrlProperties() {
        System.out.println(DBPropertiesUtil.getUrlProperties("MySQL"));
    }

    @Test
    public void getUsernameProperties() {
    }

    @Test
    public void getPasswordProperties() {
    }
}
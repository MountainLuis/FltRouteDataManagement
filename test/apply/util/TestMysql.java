package apply.util;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMysql {
    @Test
    public void test() {
        String sql = "select * from flight_plan";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try {
            for (int i = 0; i < 10 && rs.next(); i++) {
                System.out.println(rs.getString("FLT_NO"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testInsert2() {
        MysqlHelper.insert2();
    }
    @Test
    public void testInsert() {
        StringBuffer suffix = new StringBuffer();
        for (int j = 1; j <= 10000; j++) {
            // 构建sql后缀
            suffix.append("(" + j + "," + "'HELLO'" + "," + j * Math.random() + "),");
        }
        MysqlHelper.insert(suffix);
    }


}

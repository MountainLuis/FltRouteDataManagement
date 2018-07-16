package apply.util;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class TestMysqlData {
    @Test
    public void test() throws SQLException {
        Set<String> pts = new HashSet<>();
        String sql = "select * from route";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        int i = 0;
        while (rs.next()) {
//            System.out.println(rs.getString("id"));
            pts.add(rs.getString("id"));
            i++;
        }
        System.out.println(i);
        System.out.println(pts.size());
    }

}

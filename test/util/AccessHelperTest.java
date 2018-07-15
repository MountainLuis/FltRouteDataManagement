package util;

import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;

import static org.junit.Assert.*;

public class AccessHelperTest {

    @Test
    public void getConnection() {
       AccessHelper.getConnection();
       System.out.println("Done");
    }

    @Test
    public void getResultSet() throws SQLException, IOException {
        Logger logger = Logger.getLogger("chapter");
        FileHandler fileHandler = new FileHandler("d:\\log.txt");
        SimpleFormatter sf = new SimpleFormatter();
        fileHandler.setFormatter(sf);
        logger.addHandler(fileHandler);
        String time = "20180601";
        ResultSet rs = AccessHelper.getResultSet(time);
        while (rs.next()) {
            LogRecord lr = new LogRecord(Level.INFO, rs.getString("FLIGHTID"));
            logger.log(lr);
         }
    }


}
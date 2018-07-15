package logtest;

import org.apache.log4j.Logger;

public class Log4JTestWithProperties {
    private static final Logger LOGGER = Logger.getLogger(Log4JTestWithProperties.class);
    public static void main(String[] args) {
        LOGGER.debug("This is a debug message");
        LOGGER.info("This is a info Message");
        LOGGER.warn("This is a warn");
        LOGGER.error("error");
    }
}

package co.mm.util;


import co.mm.logs.PortalLogger;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {


    private static final PortalLogger logService = (PortalLogger) PortalLogger.getInstance("OTP_Service");

    public static Logger getLogService() {
        return logService;
    }

    public static void loadConfigLog4j(String pathLogConfig) {
        PropertyConfigurator.configure(pathLogConfig);
    }
}

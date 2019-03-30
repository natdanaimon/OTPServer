package co.mm.main;

import co.mm.bank.services.BankService;

import co.mm.util.LogUtil;
import co.mm.util.PropertiesConfig;

public class Jobscheduler {

    public int errorCount = 0;
    public String currentStep;

    public void initialResource(String pathConfig, String pathConfigLog4j, String logPath) {
        System.setProperty("app.path.log", logPath);
        PropertiesConfig.loadConfig(pathConfig, pathConfigLog4j);
        LogUtil.getLogService().info("Start Process ..... OTPServer SMS Auto");
        LogUtil.getLogService().info("Config Load Success.");

    }

    public void process(int startPort, int endPort, String fixPort) {

        BankService s = new BankService(startPort, endPort, fixPort);
        s.InsertOTP();

    }

    public static void main(String[] args) {

        System.out.println("Start Process ..... OTPServer SMS Auto");

        String _pathConfig = "D:\\Work Public\\Batch\\OTPServer\\src\\resources\\config.properties";
        String _pathConfigLog4j = "D:\\Work Public\\Batch\\OTPServer\\src\\resources\\log4j.properties";
        String _logPath = "D:\\logs";
        String _startPort = "10";
        String _endPort = "10";
        String _fixPort = "10";
        //6-OK ,10-OK , 11-NO , 12-OK , 9-OK , 14-OK , 4-OK , 3-OK , 13-OK , 5-OK , 7-OK , 8-OK , 17-OK , 15-OK , 16-NO , 18-NO
        //11,16,18
        switch (args.length) {
            case 6:
                _fixPort = args[5];
            case 5:
                _endPort = args[4];
            case 4:
                _startPort = args[3];
            case 3:
                _logPath = args[2];
            case 2:
                _pathConfigLog4j = args[1];
            case 1:
                _pathConfig = args[0];
        }
        final String pathConfig = _pathConfig;
        final String pathConfigLog4j = _pathConfigLog4j;
        final String logPath = _logPath;
        final int startPort = Integer.parseInt(_startPort);
        final int endPort = Integer.parseInt(_endPort);
        final String fixPort = _fixPort;

        Jobscheduler jb = new Jobscheduler();

        jb.initialResource(pathConfig, pathConfigLog4j, logPath);
        jb.process(startPort, endPort, fixPort);
    }

}

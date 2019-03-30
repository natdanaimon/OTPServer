/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.bank.services;



import co.mm.sms.CallNotification;
import co.mm.sms.GatewayStatusNotification;
import co.mm.sms.InboundNotification;
import co.mm.sms.OrphanedMessageNotification;
import co.mm.util.FormatUtil;
import co.mm.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author
 */
public final class BankService {


    public int portStart = 0;
    public int portEnd = 0;
    public String fixPort = "";

  

    public BankService(int _portStart, int _portEnd, String _fixPort) {
        RemoveHistoryLogs(System.getProperty("app.path.log"));
        this.portStart = _portStart;
        this.portEnd = _portEnd;
        this.fixPort = (_fixPort != null ? _fixPort : "");
    }

    public void InsertOTP() {
        List<InboundMessage> msgList;
        InboundNotification inboundNotification = new InboundNotification();
        CallNotification callNotification = new CallNotification();
        GatewayStatusNotification statusNotification = new GatewayStatusNotification();
        OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
        try {

            Service.getInstance().setInboundMessageNotification(inboundNotification);
            Service.getInstance().setCallNotification(callNotification);
            Service.getInstance().setGatewayStatusNotification(statusNotification);
            Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);

            for (SerialModemGateway g : listGateWayPortActive()) {
                Service.getInstance().removeGateway(g);
                Service.getInstance().addGateway(g);
            }
            Service.getInstance().startService();

            msgList = new ArrayList<InboundMessage>();

            Service.getInstance().readMessages(msgList, InboundMessage.MessageClasses.ALL);
            for (InboundMessage msg : msgList) {
                AGateway agw = Service.getInstance().getGateway(msg.getGatewayId());
                inboundNotification.process(agw, Message.MessageTypes.INBOUND, msg);
            }

            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
            System.in.read();
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            LogUtil.getLogService().error("Exception : " + e.getMessage());
        } finally {

            try {
                Service.getInstance().stopService();
            } catch (SMSLibException | IOException | InterruptedException ex1) {
                Logger.getLogger(BankService.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

    public List<SerialModemGateway> listGateWayPortActive() {
        List<SerialModemGateway> ls = new ArrayList<SerialModemGateway>();

        if (!fixPort.equals("")) {

            String[] port = fixPort.split(",");
            for (String p : port) {
                SerialModemGateway gateway = new SerialModemGateway("", "COM" + p, 115200, "", "");
                gateway.setProtocol(AGateway.Protocols.PDU);
                gateway.setInbound(true);
                gateway.setOutbound(true);
                
//                gateway.setSimPin("000" + p);
                ls.add(gateway);
                System.out.println("COM" + p + " : Add to List");
            }

        } else {

            for (int i = portStart; i <= portEnd; i++) {
                SerialModemGateway gateway = new SerialModemGateway("", "COM" + i, 115200, "", "");
                try {

                    gateway.setProtocol(AGateway.Protocols.PDU);
                    gateway.setInbound(true);
                    gateway.setOutbound(true);
                    gateway.setSimPin("000" + i);
                    Service.getInstance().addGateway(gateway);

                    Service.getInstance().S.SERIAL_TIMEOUT = 1000;
                    Service.getInstance().startService();
                    ls.add(gateway);
                    System.out.println("COM" + i + " : Success");
                    LogUtil.getLogService().info("COM" + i + " : Success");
                    
                } catch (IOException | InterruptedException | SMSLibException e) {
                    
                    System.out.println("COM" + i + " : " + e.getMessage());
                    LogUtil.getLogService().error("COM" + i + " : " + e.getMessage());
                    
                } finally {
                    
                    try {
                        Service.getInstance().stopService();
                    } catch (SMSLibException | IOException | InterruptedException ex1) {
                        LogUtil.getLogService().error("SMSLibException | IOException | InterruptedException ex1 : " + ex1.getMessage());
                    }

                    try {
                        Service.getInstance().removeGateway(gateway);
                    } catch (GatewayException gex) {
                        LogUtil.getLogService().error("GatewayException : " + gex.getMessage());
                    }
                }
            }
        }
        return ls;
    }

    public void RemoveHistoryLogs(String logPath) {

        File folder = new File(logPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {

                String fType = FilenameUtils.getExtension(listOfFiles[i].getName()).toLowerCase();
                String fName = listOfFiles[i].getName();

                try {
                    if (!fType.equals("log")
                            && !fType.equals(FormatUtil.DateDelFileTH(1)) && !fType.equals(FormatUtil.DateDelFileTH(2))
                            && !fType.equals(FormatUtil.DateDelFileEN(1)) && !fType.equals(FormatUtil.DateDelFileEN(2))) {
                        File fDel = new File(logPath + "/" + fName);
                        LogUtil.getLogService().info(fType + " / " + fName);
                        LogUtil.getLogService().info(FormatUtil.DateDelFileTH(1) + " | " + FormatUtil.DateDelFileTH(2));
                        LogUtil.getLogService().info(FormatUtil.DateDelFileEN(1) + " | " + FormatUtil.DateDelFileEN(2));
                        if (fDel.delete()) {
                            LogUtil.getLogService().info(fDel.getName() + " is del!");
                            System.out.println(fDel.getName() + " is del!");
                        } else {
                            LogUtil.getLogService().info("Del (" + fDel.getName() + ") oper is failed.");
                            System.out.println("Del (" + fDel.getName() + ") oper is failed.");
                        }
                    }
                } catch (Exception e) {
                }

            } else if (listOfFiles[i].isDirectory()) {

            }

        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.sms;

import co.mm.bean.BankBean;
import co.mm.bean.BankDetailBean;
import co.mm.bean.BankSaveBean;
import co.mm.bean.SMSDetail;
import co.mm.constant.BankConstant;
import co.mm.db.jdbc.BankDBJDBCImpl;
import co.mm.db.jdbc.DBConnectionPoolManager;
import co.mm.service.SCBService;
import co.mm.util.FormatUtil;
import co.mm.util.LogUtil;
import java.io.IOException;
import org.apache.commons.pool.ObjectPool;
import org.jsoup.helper.StringUtil;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.TimeoutException;

/**
 *
 * @author
 */
public class InboundNotification implements IInboundMessageNotification {

    public DBConnectionPoolManager db = null;
    public int portStart = 0;
    public int portEnd = 0;

    public ObjectPool connPool;

    public void initialDBConnectPoolManager() {
        if (connPool == null) {
            db = new DBConnectionPoolManager();
            connPool = db.getConnectPoolDB();
        }

    }

    public InboundNotification() {

    }

    @Override
    public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
        if (msgType == MessageTypes.INBOUND) {

            String senderName = msg.getOriginator().toUpperCase();
            initialDBConnectPoolManager();
            String smsDate = msg.getDate().toString();
            BankDBJDBCImpl dbImpl = new BankDBJDBCImpl(connPool);
            SMSDetail d = getDetailSMS(msg.getText(), smsDate, senderName);
            if (dbImpl.inquiryCheckDupplicate(d)) {
                deleteMessage(gateway, msg);
            } else {
                boolean flgInsert = dbImpl.insertSMS(d);
                if (flgInsert) {
                    deleteMessage(gateway, msg);
                }

            }

        } else {
            System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
            System.out.println("Delete Message | " + msg.getText());
            deleteMessage(gateway, msg);
        }

    }

    public void deleteMessage(AGateway gateway, InboundMessage msg) {
        try {
            System.out.println("UID : " + msg.getUuid() + " | ID : " + msg.getId() + " | Date : " + msg.getDate());//            System.out.println("Delete Message " + "0" + msg.getSmscNumber().substring(2) + " | " + msg.getText());
            gateway.deleteMessage(msg);
            LogUtil.getLogService().info("Delete Message " + "0" + msg.getSmscNumber().substring(2) + " | " + msg.getText());
        } catch (TimeoutException ex) {
            LogUtil.getLogService().error("TimeoutException : " + ex);
        } catch (GatewayException ex) {
            LogUtil.getLogService().error("GatewayException : " + ex);
        } catch (IOException ex) {
            LogUtil.getLogService().error("IOException : " + ex);
        } catch (InterruptedException ex) {
            LogUtil.getLogService().error("InterruptedException : " + ex);

        }
    }

    public SMSDetail getDetailSMS(String txt, String smsDate, String senderName) {
        SMSDetail b = new SMSDetail();
        b.setSenderName(senderName);
        String bankId = (senderName.equals(BankConstant.SENDER_BANK_KBANK) ? BankConstant.INDEX_KBANK : "0");
        b.setBankId(bankId);
        b.setAccountNo("");
        b.setMessage(txt);
        b.setSmsDatetime(smsDate);

        String otp = getOTP(txt);
        String refNo = getRefNo(txt);

        b.setOtp(otp);
        b.setRefNo(refNo);

        if (!FormatUtil.isEmpty(otp) && !FormatUtil.isEmpty(refNo)) {
            b.setFlgOTP("Y");
        } else {
            b.setFlgOTP("N");
        }

        return b;

    }

    public String getRefNo(String message) {

        String code = "";

        if (message != null && message.length() > 0) {
            String[] text = message.split(" ");
            for (int i = 0; i < text.length; i++) {
                if (text[i].indexOf(BankConstant.COMMON_MESSAGE_REF_NO) >= 0) {
                    code = text[i].replace(BankConstant.COMMON_MESSAGE_REF_NO, "");
                }
            }
        }

        return code;
    }

    public String getOTP(String message) {

        String code = "";

        if (message != null && message.length() > 0) {
            String[] text = message.split(" ");
            for (int i = 0; i < text.length; i++) {
                if (text[i].indexOf(BankConstant.COMMON_MESSAGE_OTP) >= 0) {
                    code = text[i].replace(BankConstant.COMMON_MESSAGE_OTP, "");
                }
            }
        }

        return code;
    }

}

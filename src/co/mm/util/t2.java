/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;

import co.mm.bean.BankDetailBean;
import co.mm.bean.SMSDetail;
import co.mm.db.jdbc.BankDBJDBCImpl;
import co.mm.db.jdbc.DBConnectionPoolManager;
import co.mm.sms.InboundNotification;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool.ObjectPool;
import org.smslib.InboundMessage;
import org.smslib.Message;

/**
 *
 * @author natdanaimon
 */
public class t2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        t2 t = new t2();
        t.run();
    }

    public String wordIn_01 = "รับโอนจาก";
    public String wordIn_02 = "เงินเข้า";
    public String wordTotal_01 = "คงเหลือ";
    public String wordCurency = "บ";
    public String wordBlank = " ";
    public String wordOut_01 = "หักบช";
    public String wordOut_02 = "เข้า";
    public String wordZero = "0";

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

    public void initialResource(String pathConfig, String pathConfigLog4j, String logPath) {
        System.setProperty("app.path.log", logPath);
        PropertiesConfig.loadConfig(pathConfig, pathConfigLog4j);
        LogUtil.getLogService().info("Start Process ..... OTPServer SMS Auto");
        LogUtil.getLogService().info("Config Load Success.");

    }

    public void run() {

        String _pathConfig = "D:\\Work Public\\Batch\\OTPServer\\src\\resources\\config.properties";
        String _pathConfigLog4j = "D:\\Work Public\\Batch\\OTPServer\\src\\resources\\log4j.properties";
        String _logPath = "D:\\logs";

        initialResource(_pathConfig, _pathConfigLog4j, _logPath);

        String tx1 = "โอนให้บ/ช0221321707 นายdsfdsf934";
        String tx2 = "29/10/61 02:32 บชX341626X เงินเข้า750.00";
        String tx3 = "28/10/61 18:23 บชX249514X รับโอนจากX386392X 10000.00บ คงเหลือ17359.41บ";
        String tx4 = "30/10/61 14:42 บชX341626X เงินเข้า1.00 คงเหลือ3087.29บ";

        String tx5 = "29/10/61 10:03 หักบชX341626X เข้าX180745X 1000.00 คงเหลือ1285.29บ";
        String tx6 = "25/10/61 05:00 หักบชX341626X เข้าX357282X 1050.00บ";
        String msg = tx1;

        InboundNotification in = new InboundNotification();
        String senderName = "KBANK";
        initialDBConnectPoolManager();
        String smsDate = msg;
        BankDBJDBCImpl dbImpl = new BankDBJDBCImpl(connPool);
        SMSDetail d = in.getDetailSMS(msg, smsDate, senderName);
        if (dbImpl.inquiryCheckDupplicate(d)) {
//            deleteMessage(1, msg);
        } else {
            boolean flgInsert = dbImpl.insertSMS(d);

        }
    }

    public BankDetailBean getDetailSMS(String txt) {
        BankDetailBean b = new BankDetailBean();
        b.setDatetimeDeposit(getDateDepositFromSMS(txt));
        b.setIn(getInAmount(txt));
        b.setOut(getOutAmount(txt));
        b.setRef(getRef(txt));
        b.setTotal(getTotal(txt));
        return b;

    }

    public String getDateDepositFromSMS(String txt) {
        String res = null;
        if (txt != null) {
            if (txt.length() > 14) {
                String tmp = txt.substring(0, 14);
                res = FormatUtil.convertDateSMSKBank(tmp);
            }
        }
        return res;
    }

    public String getInAmount(String txt) {
        String res = null;
        if (txt != null) {
            int lastIndex = 0;
            int firstIndex = 0;
            if (txt.contains(wordIn_01)) {
                lastIndex = txt.lastIndexOf(wordIn_01) + wordIn_01.length();
                firstIndex = txt.lastIndexOf(wordIn_01);
                String tmp = txt.substring(lastIndex + 9);
                res = tmp.substring(0, tmp.indexOf(wordCurency));
            } else if (txt.contains(wordIn_02)) {
                lastIndex = txt.lastIndexOf(wordIn_02) + wordIn_02.length();
                firstIndex = txt.lastIndexOf(wordIn_02);
                String tmp = txt.substring(lastIndex);
                if (tmp.contains(wordBlank)) {
                    res = tmp.substring(0, tmp.indexOf(wordBlank));
                } else {
                    res = tmp;
                }
            } else if (txt.contains(wordOut_01)) {
                res = wordZero;
            }

        }
        return res;
    }

    public String getOutAmount(String txt) {
        String res = null;
        if (txt != null) {
            int lastIndex = 0;
            int firstIndex = 0;
            if (txt.contains(wordIn_01) || txt.contains(wordIn_02)) {
                res = wordZero;
            } else {
                lastIndex = txt.lastIndexOf(wordOut_02) + wordOut_02.length();
                firstIndex = txt.lastIndexOf(wordOut_02);
                String tmp = txt.substring(lastIndex + 9);

                if (tmp.contains(wordTotal_01)) {
                    res = tmp.substring(0, tmp.indexOf(wordBlank));
                } else {
                    res = tmp.substring(0, tmp.indexOf(wordCurency));
                }

            }

        }
        return res;
    }

    public String getRef(String txt) {
        String res = "";
        if (txt != null) {
            int lastIndex = 0;
            int firstIndex = 0;
            if (txt.contains(wordIn_01)) {
                lastIndex = txt.lastIndexOf(wordIn_01) + wordIn_01.length();
                firstIndex = txt.lastIndexOf(wordIn_01);
                res = txt.substring(lastIndex, lastIndex + 8).replaceAll("X", "");
            } else if (txt.contains(wordIn_02)) {

            }
        }
        return res;
    }

    public String getTotal(String txt) {
        String res = null;
        if (txt != null) {
            int lastIndex = 0;
            int firstIndex = 0;
            if (txt.contains(wordTotal_01)) {
                lastIndex = txt.lastIndexOf(wordTotal_01) + wordTotal_01.length();
                firstIndex = txt.lastIndexOf(wordTotal_01);
                res = txt.substring(lastIndex, lastIndex + 8).replaceAll(wordCurency, "");
            } else if (txt.contains(wordTotal_01)) {
                lastIndex = txt.lastIndexOf(wordTotal_01) + wordTotal_01.length();
                firstIndex = txt.lastIndexOf(wordTotal_01);
                res = txt.substring(lastIndex, lastIndex + 8).replaceAll(wordCurency, "");
            }
        }
        return res;
    }

}

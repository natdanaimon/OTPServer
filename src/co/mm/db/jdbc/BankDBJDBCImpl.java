package co.mm.db.jdbc;

import co.mm.bean.BankBean;
import co.mm.bean.BankDetailBean;
import co.mm.bean.BankSaveBean;
import co.mm.bean.SMSDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import co.mm.constant.BankConstant;
import co.mm.util.FormatUtil;
import co.mm.util.LogUtil;
import java.sql.Statement;
import org.apache.commons.pool.ObjectPool;

public class BankDBJDBCImpl {

    private ObjectPool connPool = null;

    public BankDBJDBCImpl(ObjectPool _connPool) {
        this.connPool = _connPool;
    }

    public boolean inquiryCheckDupplicate(SMSDetail d) {
        boolean flgDup = false;
        StringBuilder varname1 = new StringBuilder();
        varname1.append(" SELECT count(*) cnt from tb_otp ");
        varname1.append(" where  1=1 ");
        varname1.append(" and  s_sender = ? ");
        varname1.append(" and  s_sms_datetime = ? ");
        varname1.append(" and  s_message = ? ");

        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = (Connection) connPool.borrowObject();
            pre = conn.prepareStatement(varname1.toString());
            pre.setString(1, d.getSenderName());
            pre.setString(2, d.getSmsDatetime());
            pre.setString(3, d.getMessage());

            rs = pre.executeQuery();

            while (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    flgDup = true;
                }
            }
        } catch (SQLException e) {
            LogUtil.getLogService().error("SQLException : " + e);
        } catch (Exception inne) {
            LogUtil.getLogService().error("Exception : " + inne);
        } finally {
            safeClose(rs);
            safeClose(pre);
            safeClose(conn);

        }

        return flgDup;

    }

    public boolean insertSMS(SMSDetail d) {
        boolean responseSave = false;
        boolean flgDup = false;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = (Connection) connPool.borrowObject();
            conn.setAutoCommit(false);
            StringBuilder sqlSMSCheck = new StringBuilder();
            sqlSMSCheck.append(" SELECT count(*) cnt from tb_otp ");
            sqlSMSCheck.append(" where  1=1 ");
            sqlSMSCheck.append(" and  s_sender = ? ");
            sqlSMSCheck.append(" and  s_sms_datetime = ? ");
            sqlSMSCheck.append(" and  s_message = ? ");

            pre = conn.prepareStatement(sqlSMSCheck.toString());
            pre.setString(1, d.getSenderName());
            pre.setString(2, d.getSmsDatetime());
            pre.setString(3, d.getMessage());

            rs = pre.executeQuery();

            while (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    flgDup = true;
                }
            }

            if (!flgDup) {

                StringBuilder sql = new StringBuilder();
                sql.append("insert into tb_otp   ");
                sql.append(" (i_bank , s_account_no ,s_sender, s_sms_datetime , s_message , s_flg_otp , s_refNo , s_otp , d_create) ");
                sql.append(" values (?,?,?,?,?,?,?,?,now() ) ");
                pre = conn.prepareStatement(sql.toString());
                pre.setString(1, d.getBankId());
                pre.setString(2, d.getAccountNo());
                pre.setString(3, d.getSenderName());
                pre.setString(4, d.getSmsDatetime());
                pre.setString(5, d.getMessage());
                pre.setString(6, d.getFlgOTP());
                pre.setString(7, d.getRefNo());
                pre.setString(8, d.getOtp());

                pre.executeUpdate();

                System.out.println("Insert SMS :  Sender->" + d.getSenderName() + "  : Message->" + d.getMessage());
                LogUtil.getLogService().info("Insert SMS :  Sender->" + d.getSenderName() + "  : Message->" + d.getMessage());
                conn.commit();
                responseSave = true;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {

            }
            System.out.println("SQLException : " + e);
            LogUtil.getLogService().error("SQLException : " + e);
        } catch (Exception inne) {
            try {
                conn.rollback();
            } catch (SQLException ex) {

            }
            System.out.println("Exception : " + inne);
            LogUtil.getLogService().error("Exception : " + inne);
        } finally {
            safeClose(rs);
            safeClose(pre);
            safeClose(conn);

        }

        return responseSave;

    }

    public String flgIn(String in) {
        String flg = "N";
        if (in != null) {
            if (!in.equals("")) {
                try {
                    in = in.replace(",", "");
                    in = in.replace("-", "");
                    double d = Double.parseDouble(in);
                    if (d > 0.00) {
                        flg = "Y";
                    }
                } catch (Exception e) {
                }
            }
        }

        return flg;
    }

    public String getUsernameBySCB(BankDetailBean d) {

        String userName = "";
        StringBuilder varname1 = new StringBuilder();
        if (d.getRef().length() >= 10) {
            varname1.append(" select * from tb_member_bank where s_account_no = ? ");
        } else {
            varname1.append(" select * from tb_member_bank where RIGHT( s_account_no, 6) = ? ");
        }
//        if(d.getBankId() != null && d.getBankId().length() > 0){
//            varname1.append(" and i_bank = ? ");
//        }
//        if(d.getBankShortName()!= null && d.getBankShortName().length() > 0){
//            varname1.append(" and s_bank_short_name = ? ");
//        }
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            int i = 1;
            conn = (Connection) connPool.borrowObject();
            pre = conn.prepareStatement(varname1.toString());
            pre.setString(i++, d.getRef());
//            if(d.getBankId() != null && d.getBankId().length() > 0){
//                pre.setString(i++, d.getBankId());
//            }
//            if(d.getBankShortName()!= null && d.getBankShortName().length() > 0){
//                pre.setString(i++, d.getBankShortName());
//            }
            rs = pre.executeQuery();
            while (rs.next()) {
                userName = rs.getString("s_username");
            }
        } catch (SQLException e) {
            LogUtil.getLogService().error("SQLException : " + e);
        } catch (Exception inne) {
            LogUtil.getLogService().error("Exception : " + inne);
        } finally {
            safeClose(rs);
            safeClose(pre);
            safeClose(conn);
        }
        return userName;
    }

    public String getUsernameByRefBankId(String ref) {

        String userName = "";
        StringBuilder varname1 = new StringBuilder();
        varname1.append(" select * from tb_member_bank where SUBSTRING(s_account_no, 4, 6) = ? and i_bank = 2 ");
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = (Connection) connPool.borrowObject();
            pre = conn.prepareStatement(varname1.toString());
            pre.setString(1, ref);

            rs = pre.executeQuery();

            while (rs.next()) {
                userName = rs.getString("s_username");
            }

        } catch (SQLException e) {
            LogUtil.getLogService().error("SQLException : " + e);
        } catch (Exception inne) {
            LogUtil.getLogService().error("Exception : " + inne);
        } finally {
            safeClose(rs);
            safeClose(pre);
            safeClose(conn);

        }

        return userName;

    }

    public String ConvertDateTimeMySQL(String datetime) {
        String tmp = "0000-00-00 00:00%";
        // 22/05/2018 14:43
        try {
            tmp = FormatUtil.dateMM88(datetime) + "%";
        } catch (ParseException e) {
        }

        return tmp;
    }

    public String ConvertRefNo(String bankAgent, String accountNoUser) {

        String accUser = accountNoUser.replaceAll("-", "");
        String tmp = accUser;
        if (accUser.length() >= 8) {
            if (bankAgent.equals(BankConstant.BANK_SCB)) {
                tmp = accUser.substring(accUser.length() - 4, accUser.length() - 1);
            } else if (bankAgent.equals(BankConstant.BANK_KBANK)) {
                tmp = accUser.substring(accUser.length() - 4, accUser.length() - 1);
            }
        }

        return '%' + tmp + "%";
    }

    private void safeClose(Connection conn) {
        if (conn != null) {
            try {
                connPool.returnObject(conn);
            } catch (Exception e) {
                LogUtil.getLogService().error("Failed to return the connection to the pool : " + e.getMessage());
            }
        }
    }

    private void safeClose(PreparedStatement pre) {
        if (pre != null) {
            try {
                pre.close();
            } catch (SQLException e) {
                LogUtil.getLogService().error("Failed to close databse resultset : " + e.getMessage());
            }
        }
    }

    private void safeClose(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                LogUtil.getLogService().error("Failed to close databse resultset : " + e.getMessage());
            }
        }
    }

    private void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LogUtil.getLogService().error("Failed to close databse statmen : " + e.getMessage());
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.bean;

/**
 *
 * @author 
 */
public class SMSDetail {

    private String senderName;
    private String bankId;
    private String accountNo;
    private String smsDatetime;
    private String message;
    private String flgOTP;
    private String refNo;
    private String otp;

    /**
     * @return the bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the accountNo
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * @param accountNo the accountNo to set
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * @return the smsDatetime
     */
    public String getSmsDatetime() {
        return smsDatetime;
    }

    /**
     * @param smsDatetime the smsDatetime to set
     */
    public void setSmsDatetime(String smsDatetime) {
        this.smsDatetime = smsDatetime;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the flgOTP
     */
    public String getFlgOTP() {
        return flgOTP;
    }

    /**
     * @param flgOTP the flgOTP to set
     */
    public void setFlgOTP(String flgOTP) {
        this.flgOTP = flgOTP;
    }

    /**
     * @return the refNo
     */
    public String getRefNo() {
        return refNo;
    }

    /**
     * @param refNo the refNo to set
     */
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    /**
     * @return the otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * @param otp the otp to set
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * @return the senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @param senderName the senderName to set
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

}

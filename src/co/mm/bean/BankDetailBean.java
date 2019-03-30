/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.bean;

import co.mm.util.FormatUtil;

/**
 *
 * @author 
 */
public class BankDetailBean {

    private String datetimeDeposit;
    private String in;
    private String out;
    private String ref;
    private String total;   
    private String AccountRefAgent;
    private String bankId;
    private String bankShortName;
    private String bankFlag;

    /**
     * @return the datetimeDeposit
     */
    public String getDatetimeDeposit() {
        return datetimeDeposit;
    }

    /**
     * @param datetimeDeposit the datetimeDeposit to set
     */
    public void setDatetimeDeposit(String datetimeDeposit) {
        this.datetimeDeposit = datetimeDeposit;
    }

    /**
     * @return the in
     */
    public String getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(String in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public String getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(String out) {
        this.out = out;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return the AccountRefAgent
     */
    public String getAccountRefAgent() {
        return AccountRefAgent;
    }

    /**
     * @param AccountRefAgent the AccountRefAgent to set
     */
    public void setAccountRefAgent(String AccountRefAgent) {
        this.AccountRefAgent = AccountRefAgent;
    }

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
     * @return the bankShortName
     */
    public String getBankShortName() {
        return bankShortName;
    }

    /**
     * @param bankShortName the bankShortName to set
     */
    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    /**
     * @return the bankFlag
     */
    public String getBankFlag() {
        return bankFlag;
    }

    /**
     * @param bankFlag the bankFlag to set
     */
    public void setBankFlag(String bankFlag) {
        this.bankFlag = bankFlag;
    }
    
    
    

}

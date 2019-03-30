/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.bean;

public class Statement {
	
	private String dateTime;
	private String in;
	private String out;
	private String detailAccount;
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getIn() {
		return in;
	}
	public void setIn(String in) {
		this.in = in;
	}
	public String getOut() {
		return out;
	}
	public void setOut(String out) {
		this.out = out;
	}
	public String getDetailAccount() {
		return detailAccount;
	}
	public void setDetailAccount(String detailAccount) {
		this.detailAccount = detailAccount;
	}
	@Override
	public String toString() {
		return "Statement [dateTime=" + dateTime + ", in=" + in + ", out=" + out + ", detailAccount=" + detailAccount
				+ "]";
	}
	
}

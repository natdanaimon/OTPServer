package co.mm.logs;

import java.io.Serializable;
import java.util.Date;

import co.mm.constant.BankConstant;




public class PortalAppLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logLevel;
	private String ipAddress;
	private String identityId;
	private String sessionId;
	private String instanceName;
	private String className;
	private String methodName;
	private String textMessage;
	private String errorMessage;
	private String errorStackTrace;
	private String interfaceMessage;
	private Date dateTime;
	
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getTextMessage() {
		return textMessage;
	}
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorStackTrace() {
		return errorStackTrace;
	}
	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}
	public String getInterfaceMessage() {
		return interfaceMessage;
	}
	public void setInterfaceMessage(String interfaceMessage) {
		this.interfaceMessage = interfaceMessage;
	}
	@Override
	public String toString() {
		 String prefix = className + BankConstant.LOG_SEPARATE_SYMBOL 
				  + methodName + BankConstant.LOG_SEPARATE_SYMBOL;
		 String identityInfo = identityId + BankConstant.LOG_SEPARATE_SYMBOL 
						+ ipAddress + BankConstant.LOG_SEPARATE_SYMBOL
						+ sessionId + BankConstant.LOG_SEPARATE_SYMBOL
						+ instanceName + BankConstant.LOG_SEPARATE_SYMBOL;
	
		 return prefix + identityInfo + textMessage;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}

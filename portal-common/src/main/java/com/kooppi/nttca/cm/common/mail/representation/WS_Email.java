package com.kooppi.nttca.cm.common.mail.representation;

import java.util.List;

public class WS_Email {
	private List<String> toAddressList;
	private List<String> bccAddressList;
	private int priority;
	private String mailSubject;
	private String mailBody;
	private Integer validMinutes;
	private String fromAddress;
	private String application;
	private boolean htmlContent;
	private String submitTime;
	
	public WS_Email(){
		
	}
	
	public List<String> getToAddressList() {
		return toAddressList;
	}
	public void setToAddressList(List<String> toAddressList) {
		this.toAddressList = toAddressList;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailBody() {
		return mailBody;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	public Integer getValidMinutes() {
		return validMinutes;
	}
	public void setValidMinutes(Integer validMinutes) {
		this.validMinutes = validMinutes;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public boolean isHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(boolean htmlContent) {
		this.htmlContent = htmlContent;
	}
	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public List<String> getBccAddressList() {
		return bccAddressList;
	}

	public void setBccAddressList(List<String> bccAddressList) {
		this.bccAddressList = bccAddressList;
	}
	
}

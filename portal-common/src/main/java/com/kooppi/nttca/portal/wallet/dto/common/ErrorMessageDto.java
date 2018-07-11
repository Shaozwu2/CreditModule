package com.kooppi.nttca.portal.wallet.dto.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorMessageDto {
	
	@XmlElement(name = "responseCode")
	private String responseCode;
	@XmlElement(name = "responseMsg")
	private String responseMsg;
	
	public ErrorMessageDto() {}

	public ErrorMessageDto(String responseCode, String responseMsg) {
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}

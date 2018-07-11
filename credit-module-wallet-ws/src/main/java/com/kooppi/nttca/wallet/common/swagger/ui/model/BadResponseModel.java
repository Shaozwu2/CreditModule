package com.kooppi.nttca.wallet.common.swagger.ui.model;

import javax.xml.bind.annotation.XmlElement;

public class BadResponseModel {
	@XmlElement(name = "responseCode")
	private String responseCode;
	
	@XmlElement(name = "responseMsg")
	private String responseMsg;

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}
	
}

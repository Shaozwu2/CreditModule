package com.kooppi.nttca.wallet.common.swagger.ui.model;

import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseContent;

public class GoodResponseModel {
	@XmlElement(name = "requestId")
	private String requestId;
	
	@XmlElement(name = "responseCode")
	private String responseCode;
	
	@XmlElement(name = "responseMsg")
	private String responseMsg;
	
	@XmlElement(name = "responseContent")
	private ResponseContent responseContent = new ResponseContent();

	public String getRequestId() {
		return requestId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public ResponseContent getResponseContent() {
		return responseContent;
	}
	
}

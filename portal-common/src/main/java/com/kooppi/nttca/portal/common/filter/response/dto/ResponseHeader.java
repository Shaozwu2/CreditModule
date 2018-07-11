package com.kooppi.nttca.portal.common.filter.response.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.constant.PortalConstant;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseHeader {

	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_REQUEST_ID)
	protected String requestId;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CODE)
	protected String responseCode;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_MESSAGE)
	protected String responseMsg;
	
	@XmlElement(name = PortalConstant.PORTAL_RESPONSE_HEADER_RESPONSE_CONTENT)
	protected ResponseContent responseContent = new ResponseContent();
//
//	public void setResponseContent(Object responseContent){
//		this.responseContent.setResponseContent(responseContent);
//	}
	
	public static ResponseHeader create(String requestId, String responseCode,String responseMsg,ResponseResult responseContent){
		ResponseHeader response = new ResponseHeader();
		response.requestId = requestId;
		response.responseCode = responseCode;
		response.responseMsg = responseMsg;
		response.responseContent.setResponseResult(responseContent);
		
		return response;
	}
}

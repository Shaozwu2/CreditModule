package com.kooppi.nttca.portal.common.filter.response.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.eclipse.persistence.oxm.annotations.XmlVariableNode;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseContent {

	
	@XmlVariableNode("RESULT_NAME")
	private ResponseResult result;
	
	
	public void setResponseResult(ResponseResult result){
		this.result = result;
	}

}

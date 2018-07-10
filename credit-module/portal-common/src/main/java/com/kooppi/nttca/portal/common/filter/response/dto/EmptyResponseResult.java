package com.kooppi.nttca.portal.common.filter.response.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class EmptyResponseResult extends ResponseResult{

	public static EmptyResponseResult create() {
		return new EmptyResponseResult();
	}
	@Override
	public String getResultName() {
		return "empty";
	}

}

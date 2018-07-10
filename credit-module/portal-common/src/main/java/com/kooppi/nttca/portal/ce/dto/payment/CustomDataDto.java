package com.kooppi.nttca.portal.ce.dto.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.kooppi.nttca.portal.common.filter.response.dto.ResponseResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomDataDto extends ResponseResult{

	@Override
	public String getResultName() {
		return "customData";
	}

	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "value")
	private String value;
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public static CustomDataDto create(String name,String value){
		CustomDataDto customData = new CustomDataDto();
		customData.name = name;
		customData.value = value;
		return customData;
	}
}
